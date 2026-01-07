
# Traffic Light Controller API

A Spring Boot API for controlling traffic light sequences across one or more intersections.

## Features
- Manage state changes of lights (RED, YELLOW, GREEN) for direction groups (NORTH_SOUTH, EAST_WEST).
- Commands to start, pause (failsafe ALL_RED), and resume operation.
- Update sequences safely (validated: no conflicting GREENs, min duration).
- Current state and timing history endpoints.
- Concurrency-safe controller per intersection; registry supports many intersections.
- Ready for future expansion: persistence, distributed scheduling, OpenAPI docs.

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Run
```bash
mvn spring-boot:run
```
OpenAPI UI at: http://localhost:8080/swagger-ui.html

### Example cURL
```bash
# Create intersection with default demo sequence (already created on startup as int-001)
curl http://localhost:8080/api/v1/intersections

# Get state
curl http://localhost:8080/api/v1/intersections/int-001/state

# Pause
curl -X POST http://localhost:8080/api/v1/intersections/int-001/pause

# Resume
curl -X POST http://localhost:8080/api/v1/intersections/int-001/resume

# History
curl http://localhost:8080/api/v1/intersections/int-001/history?limit=50
```

## Notes
- On pause and errors, the controller sets ALL_RED and stops scheduling.
- Sequence validation rejects conflicting GREENs and short phases (< 500ms).
- History is kept in-memory (up to 500 events). Consider persisting for production.
- For multi-intersection, the registry is the anchor; add persistence & leader election for HA.

