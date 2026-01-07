
package com.example.traffic.core;

import com.example.traffic.model.*;

import java.util.List;
import java.util.Map;

public final class SequenceValidator {
    private SequenceValidator() {}

    public static void validate(Sequence seq) {
        List<Phase> phases = seq.getPhases();
        if (phases == null || phases.isEmpty()) {
            throw new IllegalArgumentException("Sequence must contain at least one phase.");
        }

        for (int i = 0; i < phases.size(); i++) {
            Phase p = phases.get(i);
            if (p.getDurationMillis() < 500) {
                throw new IllegalArgumentException("Phase " + i + " duration too short (>= 500ms).");
            }
            Map<DirectionGroup, LightColor> L = p.getLights();
            ensureKey(L, DirectionGroup.NORTH_SOUTH);
            ensureKey(L, DirectionGroup.EAST_WEST);

            if (L.get(DirectionGroup.NORTH_SOUTH) == LightColor.GREEN &&
                L.get(DirectionGroup.EAST_WEST) == LightColor.GREEN) {
                throw new IllegalArgumentException("Phase " + i + " has conflicting greens.");
            }
        }
    }

    private static void ensureKey(Map<DirectionGroup, LightColor> map, DirectionGroup k) {
        if (map == null || !map.containsKey(k)) {
            throw new IllegalArgumentException("Missing light for " + k);
        }
    }
}
