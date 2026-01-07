
package com.example.traffic.core;

import com.example.traffic.model.Sequence;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class IntersectionRegistry {
    private final Map<String, IntersectionController> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() / 2));

    public IntersectionController create(String id, Sequence seq) {
        IntersectionController ctrl = new IntersectionController(id, seq, scheduler);
        var prev = map.putIfAbsent(id, ctrl);
        if (prev != null) throw new IllegalArgumentException("Intersection already exists: " + id);
        return ctrl;
    }

    public Optional<IntersectionController> get(String id) {
        return Optional.ofNullable(map.get(id));
    }

    public Map<String, IntersectionController> all() { return Map.copyOf(map); }
}
