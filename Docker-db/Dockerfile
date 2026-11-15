FROM mysql:5.7.25

ENV MYSQL_ROOT_PASSWORD="devopspassword"
ENV MYSQL_DATABASE="accounts"

# Copy the init SQL into the MySQL startup folder so it runs on first container start.
COPY db_backup.sql /docker-entrypoint-initdb.d/01-init.sql

EXPOSE 3306
