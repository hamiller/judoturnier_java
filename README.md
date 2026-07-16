
# Build project

Das Projekt wird mit `./gradlew clean bootJar` erstellt.
Dies erstellt eine `judoturnier.jar` Datei im Unterverzeichnis `./build/libs`.

## Provisioning

Für das Vorbereiten des Zielsystems siehe README.md in `provisioning`.

## Deployment

Für das Deployment der Anwendung auf dem Zielsystem siehe README.md in `deployment`.

## Development

Einmal den Service starten:
`./gradlew bootRun --console=plain --args='--spring.profiles.active=local`

Einmal die Überwachen mit hotreload starten:
`./gradlew compileJava processResources --continuous --console=plain`


## Lokales Testen

Zum lokalen testen muss das Testbed gestartet sein (also keycloak, Datenbank).
Dazu `docker compose -f docker-compose-testbed.yml up -d` ausführen.
Beim ersten Start importiert Keycloak automatisch den Realm `judoturnier` aus
`keycloak-realm-export.json`. Die Datei wird im Container als
`/opt/keycloak/data/import/judoturnier-realm.json` eingebunden und mit
`--import-realm` geladen.

Falls Keycloak bereits mit einem bestehenden Docker-Volume gestartet wurde,
wird ein vorhandener Realm beim Start nicht überschrieben. Für ein frisches
lokales Testbed die Container und Volumes entfernen:
`docker compose -f docker-compose-testbed.yml down -v`

Danach kann die Anwendung in der Entwicklungsumgebung gestartet werden, bzw.
`./gradlew clean bootRun --console=plain --args='--spring.profiles.active=local'`
ACHTUNG: Spring-Profile `local` ist erforderlich.


Der Service ist dann unter `localhost:8080` erreichbar.


## Hints

### Keycloak

Falls Keycloak mit einer Fehlermeldung antwortet, sicherstellen dass Email, Vorname und Nachname gesetzt sind.
Es muss ausserdem sichergestellt sein, das "microprofile-jwt" in den "Client Scopes" aktiviert ist. -> das wird benötigt um die Rollen aus dem Token zu extrahieren.

### DB Probleme

auf dem remote host im DB image anmelden:
`docker exec -it 32c9df391fc9 /bin/bash`
Postgres CLI starten:
`psql -U username -d judo`
Anzeige der Datenbanken:
`\list`
