# Bereitstellen der Laufzeitumgebung

Auf dem Zielsystem muss ein nginx und ein certbot vorhanden sein um Anfragen an die Anwendung weiterzuleiten und https zu terminieren.


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