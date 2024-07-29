
# Build project

Das Projekt wird mit `./gradlew clean bootJar` erstellt.
Dies erstellt eine `judoturnier.jar` Datei im Unterverzeichnis `./build/libs`.

## Provisioning

Für das Vorbereiten des Zielsystems siehe README.md in `provisioning`.

## Deployment

Für das Deployment der Anwendung auf dem Zielsystem siehe README.md in `deployment`.


## Lokales Testen

Zum lokalen testen muss das Testbed gestartet sein (also keycloak, Datenbank).
Dazu `docker compose -f docker-compose-testbed.yml up -d` ausführen.
Danach kann die Anwendung in der Entwicklungsumgebung gestartet werden.
ACHTUNG: Spring-Profile `local` ist erforderlich.


## Hints

### Keycloak

Falls Keycloak mit einer Fehlermeldung antwortet, sicherstellen dass Email, Vorname und Nachname gesetzt sind.

### DB Probleme

auf dem remote host im DB image anmelden:
`docker exec -it 32c9df391fc9 /bin/bash`
Postgres CLI starten:
`psql -U username -d judo`
Anzeige der Datenbanken:
`\list`