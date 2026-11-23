FROM mysql:latest@sha256:3819b2b23835c4a73c8b022fafc33c8095c0f81c929a8b7a84613d2fba6c52f5

RUN microdnf -y upgrade && microdnf clean all

# Copy the init SQL into the MySQL startup folder so it runs on first container start.
COPY db_backup.sql /docker-entrypoint-initdb.d/01-init.sql

EXPOSE 3306
