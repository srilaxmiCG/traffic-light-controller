
package com.example.traffic.api;

import com.example.traffic.core.IntersectionRegistry;
import com.example.traffic.core.IntersectionController;
import com.example.traffic.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final IntersectionRegistry registry;

    public ApiController(IntersectionRegistry registry) { this.registry = registry; }

    @PostMapping("/intersections")
    public ResponseEntity<?> create(@RequestParam String id, @RequestBody Sequence seq) {
        registry.create(id, seq);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/intersections")
    public List<StateSnapshot> list() {
        return registry.all().values().stream().map(IntersectionController::snapshot).collect(Collectors.toList());
    }

    @GetMapping("/intersections/{id}")
    public ResponseEntity<StateSnapshot> get(@PathVariable String id) {
        return registry.get(id)
                .map(IntersectionController::snapshot)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/intersections/{id}/start")
    public ResponseEntity<?> start(@PathVariable String id) {
        return registry.get(id).map(c -> { c.start(); return ResponseEntity.ok().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/intersections/{id}/pause")
    public ResponseEntity<?> pause(@PathVariable String id) {
        return registry.get(id).map(c -> { c.pause(); return ResponseEntity.ok().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/intersections/{id}/resume")
    public ResponseEntity<?> resume(@PathVariable String id) {
        return registry.get(id).map(c -> { c.resume(); return ResponseEntity.ok().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/intersections/{id}/sequence")
    public ResponseEntity<?> updateSeq(@PathVariable String id, @RequestBody Sequence seq) {
        return registry.get(id).map(c -> { c.updateSequence(seq); return ResponseEntity.ok().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/intersections/{id}/state")
    public ResponseEntity<StateSnapshot> state(@PathVariable String id) {
        return get(id);
    }

    @GetMapping("/intersections/{id}/history")
    public ResponseEntity<List<HistoryEvent>> history(@PathVariable String id,
                                                      @RequestParam(defaultValue = "100") int limit) {
        return registry.get(id)
                .map(c -> ResponseEntity.ok(c.history(limit)))
                .orElse(ResponseEntity.notFound().build());
    }
}
