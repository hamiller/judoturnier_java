# Deployment der Anwendung wird durch ansible durchgeführt

### Deployment ausführen

`ansible-playbook -i inventory.yml playbook.yml`

ACHTUNG: auf dem Zielsystem wird ein nginx benötigt um Anfragen an die App weiterzuleiten. Siehe "Vorbereitung Zielsystem" im `provisioning`-Verzeichnis.
