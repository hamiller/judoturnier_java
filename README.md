
# Build project

Das Projekt wird mit `./gradlew bootJar` erstellt

## Provisioning

Für das Vorbereiten des Zielsystems siehe README.md in `provisioning`.

## Deployment

Für das Deployment der Anwendung auf dem Zielsystem siehe README.md in `deployment`.



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