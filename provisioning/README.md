# Bereitstellen der Laufzeitumgebung

Auf dem Zielsystem muss ein nginx und ein certbot vorhanden sein um Anfragen an die Anwendung weiterzuleiten und https zu terminieren.
Die Einrichtung erfolgt einmalig.


### Vorgehen
(übernommen von https://phoenixnap.com/kb/letsencrypt-docker)

- Im Home-Verzeichnis ein Unterverzeichnis `webserver` erstellen.
- Die `docker-compose.yml` dorthin kopieren
- für Nginx die Datei `./webserver/nginx/conf.d/app.conf` angelegt (diese wird R/O in den Client gemounted)

- für das spätere Deployment muss das Verzeichnis `/opt` durch Ansible beschreibbar sein.
  (Das kann zB erreicht werden, indem der ssh-Nutzer den ansible verwendet einer schreibberechtigten Gruppe hinzugefügt wird:
```
  # user1:
  sudo groupadd optgroup
  sudo usermod -aG optgroup user1
  sudo chown root:optgroup /opt
  sudo chmod 775 /opt
```
)

## Certbot

Für Certbot muss ein entsprechendes Image bereitgestellt werden (und im `docker-compose.yaml` entsprechend verwendet werden).
Zum Bauen muss ein Credentials-File vorhanden sein (hier `../.hetzner-credentials`):
ACHTUNG: Es kann nicht auf Dateien im Parent zugegriffen werden, daher muss das Erstellen des Images direkt im Parent erfolgen!

(im Parent-Verzeichnis mit `.hetzner-credentials`)
`docker build -f provisioning/Dockerfile -t sinnix/hetzner-certbot:latest .`

Das Image muss anschließend auf dem Host bereitgestellt werden:
```
docker save -o /tmp/hetzner-certbot.tar sinnix/hetzner-certbot:latest

scp /tmp/hetzner-certbot.tar <HOST>:/tmp/

# auf dem Host laden:
docker load -i /tmp/hetzner-certbot.tar
```

## Keycloak
Keycloak muss die entsprechende Redirect URI erlaubt sein.

Das hinterlegte client-secret in application-local.yml und im Keycloak selbst muss gleich sein.

Es muss ausserdem sichergestellt sein, das "microprofile-jwt" in den "Client Scopes" aktiviert ist. -> das wird benötigt um die Rollen aus dem Token zu extrahieren.
