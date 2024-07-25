# Use the official certbot image as the base
FROM certbot/certbot:latest

# Install certbot and the certbot-dns-hetzner plugin
RUN pip install certbot certbot-dns-hetzner

# Add a volume for certbot data
VOLUME /etc/letsencrypt

# Add a volume for certbot logs
VOLUME /var/log/letsencrypt

# Add credentials file
ADD ./.hetzner-credentials ./hetzner-credentials

ENTRYPOINT ["sh",  "-c", "certbot certonly --authenticator dns-hetzner --dns-hetzner-credentials ./hetzner-credentials --agree-tos --no-eff-email --email $EMAIL -d $DOMAIN"]
