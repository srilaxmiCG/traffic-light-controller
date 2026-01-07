
package com.example.traffic.core;

import com.example.traffic.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.List;

public class SequenceValidatorTest {
    @Test
    void conflictingGreensShouldFail() {
        Phase p = new Phase();
        p.setDurationMillis(1000);
        EnumMap<DirectionGroup, LightColor> L = new EnumMap<>(DirectionGroup.class);
        L.put(DirectionGroup.NORTH_SOUTH, LightColor.GREEN);
        L.put(DirectionGroup.EAST_WEST, LightColor.GREEN);
        p.setLights(L);
        Sequence seq = new Sequence();
        seq.setVersion(1);
        seq.setPhases(List.of(p));
        assertThrows(IllegalArgumentException.class, () -> SequenceValidator.validate(seq));
    }
}
