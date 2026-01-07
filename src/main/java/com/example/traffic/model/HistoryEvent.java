
package com.example.traffic.model;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

public class HistoryEvent {
    public enum Reason { PHASE_ADVANCE, START, PAUSE, RESUME, OVERRIDE, ERROR }

    private Instant timestamp;
    private int phaseIndex;
    private long durationMillis;
    private Reason reason;
    private Map<DirectionGroup, LightColor> lights = new EnumMap<>(DirectionGroup.class);

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public int getPhaseIndex() { return phaseIndex; }
    public void setPhaseIndex(int phaseIndex) { this.phaseIndex = phaseIndex; }

    public long getDurationMillis() { return durationMillis; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    public Reason getReason() { return reason; }
    public void setReason(Reason reason) { this.reason = reason; }

    public Map<DirectionGroup, LightColor> getLights() { return lights; }
    public void setLights(Map<DirectionGroup, LightColor> lights) { this.lights = lights; }
}
