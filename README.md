
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

### Build Jar
```bash
mvn -q -DskipTests package
java -jar target/traffic-light-controller-0.1.0.jar
```

### Docker (optional)
```bash
# Build image
docker build -t traffic-light-controller:0.1 .

# Run
docker run --rm -p 8080:8080 traffic-light-controller:0.1
```

## Project Structure
```
traffic-light-controller/
├── pom.xml
├── README.md
├── Dockerfile
├── src
│   ├── main
│   │   ├── java/com/example/traffic
│   │   │   ├── DemoApplication.java
│   │   │   ├── api/ApiController.java
│   │   │   ├── core/IntersectionController.java
│   │   │   ├── core/IntersectionRegistry.java
│   │   │   ├── core/SequenceValidator.java
│   │   │   ├── model/DirectionGroup.java
│   │   │   ├── model/LightColor.java
│   │   │   ├── model/Phase.java
│   │   │   ├── model/Sequence.java
│   │   │   ├── model/StateSnapshot.java
│   │   │   └── model/HistoryEvent.java
│   │   └── resources
│   │       └── application.yml
│   └── test
│       └── java/com/example/traffic
│           └── core/SequenceValidatorTest.java
└── .gitignore
```

## Notes
- On pause and errors, the controller sets ALL_RED and stops scheduling.
- Sequence validation rejects conflicting GREENs and short phases (< 500ms).
- History is kept in-memory (up to 500 events). Consider persisting for production.
- For multi-intersection, the registry is the anchor; add persistence & leader election for HA.

## License
MIT
