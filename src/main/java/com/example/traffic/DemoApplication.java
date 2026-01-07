
package com.example.traffic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.traffic.core.IntersectionRegistry;
import com.example.traffic.model.*;

import java.util.EnumMap;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final IntersectionRegistry registry;

    public DemoApplication(IntersectionRegistry registry) {
        this.registry = registry;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Bootstrap demo sequence for intersection "int-001"
        Sequence seq = new Sequence();
        seq.setVersion(1);

        Phase p1 = new Phase();
        p1.setDurationMillis(25000);
        EnumMap<DirectionGroup, LightColor> L1 = new EnumMap<>(DirectionGroup.class);
        L1.put(DirectionGroup.NORTH_SOUTH, LightColor.GREEN);
        L1.put(DirectionGroup.EAST_WEST, LightColor.RED);
        p1.setLights(L1);

        Phase p2 = new Phase();
        p2.setDurationMillis(3000);
        EnumMap<DirectionGroup, LightColor> L2 = new EnumMap<>(DirectionGroup.class);
        L2.put(DirectionGroup.NORTH_SOUTH, LightColor.YELLOW);
        L2.put(DirectionGroup.EAST_WEST, LightColor.RED);
        p2.setLights(L2);

        Phase p3 = new Phase();
        p3.setDurationMillis(1000);
        EnumMap<DirectionGroup, LightColor> L3 = new EnumMap<>(DirectionGroup.class);
        L3.put(DirectionGroup.NORTH_SOUTH, LightColor.RED);
        L3.put(DirectionGroup.EAST_WEST, LightColor.RED);
        p3.setLights(L3);

        Phase p4 = new Phase();
        p4.setDurationMillis(25000);
        EnumMap<DirectionGroup, LightColor> L4 = new EnumMap<>(DirectionGroup.class);
        L4.put(DirectionGroup.NORTH_SOUTH, LightColor.RED);
        L4.put(DirectionGroup.EAST_WEST, LightColor.GREEN);
        p4.setLights(L4);

        Phase p5 = new Phase();
        p5.setDurationMillis(3000);
        EnumMap<DirectionGroup, LightColor> L5 = new EnumMap<>(DirectionGroup.class);
        L5.put(DirectionGroup.NORTH_SOUTH, LightColor.RED);
        L5.put(DirectionGroup.EAST_WEST, LightColor.YELLOW);
        p5.setLights(L5);

        Phase p6 = new Phase();
        p6.setDurationMillis(1000);
        EnumMap<DirectionGroup, LightColor> L6 = new EnumMap<>(DirectionGroup.class);
        L6.put(DirectionGroup.NORTH_SOUTH, LightColor.RED);
        L6.put(DirectionGroup.EAST_WEST, LightColor.RED);
        p6.setLights(L6);

        seq.setPhases(List.of(p1, p2, p3, p4, p5, p6));

        registry.create("int-001", seq).start();
    }
}
