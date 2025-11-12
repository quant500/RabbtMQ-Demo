# Kafka-Demo

Spring Boot Anwendung mit Kafka-Integration und Testcontainers.

## Voraussetzungen

- Java 21
- Podman (für Testcontainers)

## Podman-Konfiguration für WSL2

### Wichtige Vorbereitung:
Testcontainers verwendet automatisch die `DOCKER_HOST` Environment-Variable.

### 1. Podman Service starten:
```bash
# In WSL2 Terminal:
podman system service --time=0 tcp://localhost:2376 &
```

### 2. DOCKER_HOST setzen:
```bash
# In WSL2 Terminal:
export DOCKER_HOST=tcp://localhost:2376
```

### 3. Test ausführen:
```bash
# Von IntelliJ IDEA oder Command Line:
./mvnw test -Dtest=KafkaIntegrationTest
```

### Fehlerbehebung:
- **"Could not find a valid Docker environment"**: `DOCKER_HOST` ist nicht gesetzt
- **Verbindung fehlgeschlagen**: Podman-Service läuft nicht
- **Port belegt**: Anderer Service verwendet Port 2376

### Alternative Rootless-Konfiguration:
```bash
# Podman ohne Root-Rechte
podman system service --time=0 unix:///run/user/$(id -u)/podman/podman.sock &
export DOCKER_HOST=unix:///run/user/$(id -u)/podman/podman.sock
```

## Tests ausführen

```bash
# Alle Tests
./mvnw test

# Nur Kafka-Integrationstest mit Testcontainers
./mvnw test -Dtest=KafkaIntegrationTest
```

## Architektur

- **MessageProducer**: Sendet Nachrichten an Kafka-Topic
- **MessageConsumer**: Empfängt Nachrichten vom Kafka-Topic
- **KafkaConfig**: Konfiguriert Kafka-Topics
- **KafkaIntegrationTest**: Integrationstest mit Testcontainers
