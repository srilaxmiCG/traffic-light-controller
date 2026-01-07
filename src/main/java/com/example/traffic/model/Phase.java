
package com.example.traffic.model;

import java.util.EnumMap;
import java.util.Map;

public class Phase {
    private long durationMillis;
    private Map<DirectionGroup, LightColor> lights = new EnumMap<>(DirectionGroup.class);

    public long getDurationMillis() { return durationMillis; }
    public void setDurationMillis(long durationMillis) { this.durationMillis = durationMillis; }

    public Map<DirectionGroup, LightColor> getLights() { return lights; }
    public void setLights(Map<DirectionGroup, LightColor> lights) { this.lights = lights; }
}
