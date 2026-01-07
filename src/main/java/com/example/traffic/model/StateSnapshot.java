
package com.example.traffic.model;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

public class StateSnapshot {
    public enum Status { RUNNING, PAUSED }

    private String intersectionId;
    private Status status;
    private long sequenceVersion;
    private int currentPhaseIndex;
    private Instant updatedAt;
    private Instant phaseEndsAt; // optional
    private Map<DirectionGroup, LightColor> lights = new EnumMap<>(DirectionGroup.class);

    public String getIntersectionId() { return intersectionId; }
    public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public long getSequenceVersion() { return sequenceVersion; }
    public void setSequenceVersion(long sequenceVersion) { this.sequenceVersion = sequenceVersion; }

    public int getCurrentPhaseIndex() { return currentPhaseIndex; }
    public void setCurrentPhaseIndex(int currentPhaseIndex) { this.currentPhaseIndex = currentPhaseIndex; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getPhaseEndsAt() { return phaseEndsAt; }
    public void setPhaseEndsAt(Instant phaseEndsAt) { this.phaseEndsAt = phaseEndsAt; }

    public Map<DirectionGroup, LightColor> getLights() { return lights; }
    public void setLights(Map<DirectionGroup, LightColor> lights) { this.lights = lights; }
}
