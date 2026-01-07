
package com.example.traffic.core;

import com.example.traffic.model.*;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class IntersectionController {
    private final String id;
    private final ScheduledExecutorService executor;
    private final ReentrantLock lock = new ReentrantLock();

    private volatile StateSnapshot.Status status = StateSnapshot.Status.PAUSED;
    private Sequence sequence;
    private int idx = 0;
    private ScheduledFuture<?> currentTask;

    private final int HISTORY_LIMIT = 500;
    private final Deque<HistoryEvent> history = new ArrayDeque<>(HISTORY_LIMIT);
    private final Map<DirectionGroup, LightColor> lights = new EnumMap<>(DirectionGroup.class);

    private Instant phaseStartAt;
    private long phaseDurationMs;

    public IntersectionController(String id, Sequence sequence, ScheduledExecutorService executor) {
        this.id = id;
        this.executor = executor;
        SequenceValidator.validate(sequence);
        this.sequence = sequence;
        for (DirectionGroup d : DirectionGroup.values()) lights.put(d, LightColor.RED);
        record(HistoryEvent.Reason.PAUSE, -1, 0);
    }

    public String id() { return id; }

    public void start() {
        lock.lock();
        try {
            if (status == StateSnapshot.Status.RUNNING) return;
            status = StateSnapshot.Status.RUNNING;
            record(HistoryEvent.Reason.START, idx, 0);
            scheduleNextPhase(0);
        } finally { lock.unlock(); }
    }

    public void pause() {
        lock.lock();
        try {
            if (status == StateSnapshot.Status.PAUSED) return;
            status = StateSnapshot.Status.PAUSED;
            cancelTask();
            setAllRed();
            record(HistoryEvent.Reason.PAUSE, -1, 0);
        } finally { lock.unlock(); }
    }

    public void resume() {
        lock.lock();
        try {
            if (status == StateSnapshot.Status.RUNNING) return;
            status = StateSnapshot.Status.RUNNING;
            record(HistoryEvent.Reason.RESUME, idx, 0);
            scheduleNextPhase(0);
        } finally { lock.unlock(); }
    }

    public void updateSequence(Sequence newSeq) {
        SequenceValidator.validate(newSeq);
        lock.lock();
        try {
            this.sequence = newSeq;
        } finally { lock.unlock(); }
    }

    public StateSnapshot snapshot() {
        lock.lock();
        try {
            StateSnapshot snap = new StateSnapshot();
            snap.setIntersectionId(id);
            snap.setStatus(status);
            snap.setSequenceVersion(sequence.getVersion());
            snap.setCurrentPhaseIndex(status == StateSnapshot.Status.RUNNING ? idx : -1);
            snap.setUpdatedAt(Instant.now());
            snap.setLights(new EnumMap<>(lights));
            if (status == StateSnapshot.Status.RUNNING && phaseStartAt != null) {
                snap.setPhaseEndsAt(phaseStartAt.plusMillis(phaseDurationMs));
            }
            return snap;
        } finally { lock.unlock(); }
    }

    public List<HistoryEvent> history(int limit) {
        lock.lock();
        try {
            List<HistoryEvent> copy = new ArrayList<>(history);
            int size = copy.size();
            int from = Math.max(0, size - limit);
            List<HistoryEvent> tail = copy.subList(from, size);
            List<HistoryEvent> result = new ArrayList<>();
            for (HistoryEvent ev : tail) result.add(cloneEvent(ev));
            return result;
        } finally { lock.unlock(); }
    }

    private void scheduleNextPhase(long initialDelayMs) {
        cancelTask();
        Runnable r = () -> {
            lock.lock();
            try {
                if (status != StateSnapshot.Status.RUNNING) return;
                Phase p = sequence.getPhases().get(idx);
                applyPhase(p);
                phaseStartAt = Instant.now();
                phaseDurationMs = p.getDurationMillis();
                record(HistoryEvent.Reason.PHASE_ADVANCE, idx, p.getDurationMillis());
                int next = (idx + 1) % sequence.getPhases().size();
                idx = next;
                scheduleNextPhase(p.getDurationMillis());
            } catch (Exception e) {
                setAllRed();
                record(HistoryEvent.Reason.ERROR, idx, 0);
                status = StateSnapshot.Status.PAUSED;
            } finally {
                lock.unlock();
            }
        };
        currentTask = executor.schedule(r, initialDelayMs, TimeUnit.MILLISECONDS);
    }

    private void cancelTask() {
        Optional.ofNullable(currentTask).ifPresent(t -> t.cancel(false));
        currentTask = null;
    }

    private void applyPhase(Phase p) {
        for (Map.Entry<DirectionGroup, LightColor> e : p.getLights().entrySet()) {
            lights.put(e.getKey(), e.getValue());
        }
    }

    private void setAllRed() {
        for (DirectionGroup d : DirectionGroup.values()) lights.put(d, LightColor.RED);
    }

    private void record(HistoryEvent.Reason reason, int phaseIndex, long durationMs) {
        HistoryEvent ev = new HistoryEvent();
        ev.setTimestamp(Instant.now());
        ev.setPhaseIndex(phaseIndex);
        ev.setDurationMillis(durationMs);
        ev.setReason(reason);
        ev.setLights(new EnumMap<>(lights));
        if (history.size() == HISTORY_LIMIT) history.removeFirst();
        history.addLast(ev);
    }

    private HistoryEvent cloneEvent(HistoryEvent ev) {
        HistoryEvent c = new HistoryEvent();
        c.setTimestamp(ev.getTimestamp());
        c.setPhaseIndex(ev.getPhaseIndex());
        c.setDurationMillis(ev.getDurationMillis());
        c.setReason(ev.getReason());
        c.setLights(new EnumMap<>(ev.getLights()));
        return c;
    }
}
