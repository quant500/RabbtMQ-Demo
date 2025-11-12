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
# EMPFOHLEN: Unix Socket (funktioniert zuverlässig in WSL2)
podman system service --time=0 unix:///run/user/$(id -u)/podman/podman.sock &

# Alternative: TCP (funktioniert nicht immer in WSL2)
podman system service --time=0 tcp://0.0.0.0:2376 &

# Podman Service stoppen
pkill -f "podman system service"

# Prüfen, ob der Service läuft
ps -ef | grep "podman system service" | grep -v grep
```

### 2. DOCKER_HOST setzen:
```bash
# Für Unix Socket (empfohlen):
export DOCKER_HOST=unix:///run/user/$(id -u)/podman/podman.sock

# Für TCP (falls Unix Socket nicht funktioniert):
export DOCKER_HOST=tcp://0.0.0.0:2376
```

### Warum Unix Socket besser ist:

1. **WSL2-kompatibel**: Keine Netzwerk-Probleme zwischen Windows und WSL2
2. **Sicherer**: Lokale Unix-Domain-Socket-Verbindung
3. **Zuverlässiger**: Funktioniert immer innerhalb von WSL2
4. **Schneller**: Kein TCP-Overhead

**TCP hat Probleme in WSL2:**
- Netzwerk-Kommunikation zwischen Windows und WSL2 ist komplex
- Firewall- und Routing-Probleme möglich
- Funktioniert nicht zuverlässig

### 3. Test ausführen:
```bash
# DOCKER_HOST muss in WSL2 gesetzt sein (für Unix Socket):
export DOCKER_HOST=unix:///run/user/$(id -u)/podman/podman.sock

# Dann Tests ausführen:
./mvnw test -Dtest=KafkaIntegrationTest
./mvnw test -Dtest=KafkaContainerTest
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

### Command Line:
```bash
# Alle Tests
./mvnw test

# Nur grundlegende Tests (ohne Container)
./mvnw test -Dtest=KafkaIntegrationTest

# Nur Container-Tests (mit Podman)
./mvnw test -Dtest=KafkaContainerTest
```

### IntelliJ IDEA:

#### Grundlegende Tests (funktionieren immer):
- Rechtsklick auf `KafkaIntegrationTest.java` → Run Tests
- Testen Spring-Kontext und DOCKER_HOST Konfiguration

#### Container-Tests (nur mit Podman):

**Problem**: IntelliJ läuft in Windows, Podman in WSL2 → Netzwerk-Kommunikation problematisch.

### Lösung A: WSL2-IP verwenden
1. **WSL2-IP herausfinden**:
   ```bash
   # In WSL2 Terminal:
   ip addr show eth0 | grep 'inet ' | awk '{print $2}' | cut -d/ -f1
   # Beispiel: 172.25.1.100
   ```

2. **Podman mit WSL2-IP starten**:
   ```bash
   podman system service --time=0 tcp://0.0.0.0:2376 &
   ```

3. **VM Option mit WSL2-IP setzen**:
   - Help → Edit Custom VM Options
   - Füge hinzu: `-DDOCKER_HOST=tcp://172.25.1.100:2376`
   - IntelliJ neu starten

### Lösung B: IntelliJ in WSL2 ausführen
1. **IntelliJ in WSL2 installieren**:
   ```bash
   # In WSL2:
   sudo apt update
   sudo apt install intellij-idea-community
   idea.sh &
   ```

2. **Projekt in WSL2 öffnen**:
   - Das Projekt liegt bereits in WSL2 unter `/mnt/d/IntelliJWorkspaces/RabbtMQ-Demo`

### Lösung C: Maven in IntelliJ auf WSL2 umstellen
1. **Settings → Build, Execution, Deployment → Build Tools → Maven**
2. **Maven home path**: `D:\IntelliJWorkspaces\RabbtMQ-Demo\mvnw`
3. **User settings file**: `D:\IntelliJWorkspaces\RabbtMQ-Demo\.mvn\settings.xml` (falls vorhanden)
4. **Local repository**: Leer lassen (verwendet Standard)
5. **IntelliJ neu starten**
6. **Tests laufen lassen** - Maven verwendet WSL2 Terminal

**Hinweis**: Wenn das nicht funktioniert, verwenden Sie einfach den Terminal für Container-Tests.

#### Run-Konfigurationen:
Vorkonfigurierte Run-Konfigurationen sind verfügbar:
- `KafkaIntegrationTest.run.xml` - Grundlegende Tests
- `KafkaContainerTest.run.xml` - Container-Tests

**Wichtig**: Container-Tests laufen nur, wenn `DOCKER_HOST=tcp://localhost:2376` gesetzt ist.

## Architektur

- **MessageProducer**: Sendet Nachrichten an Kafka-Topic
- **MessageConsumer**: Empfängt Nachrichten vom Kafka-Topic
- **KafkaConfig**: Konfiguriert Kafka-Topics
- **KafkaIntegrationTest**: Integrationstest mit Testcontainers
