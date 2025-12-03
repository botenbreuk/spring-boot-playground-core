#!/bin/bash
source ./.settings.env

# Drop DB and user.
dropdb $DB_NAME --force;
dropuser $DB_NAME;

# Terminate PSQL instance, gracefully.
pg_ctl -D /opt/homebrew/var/$POSTGRES_VERSION stop;
