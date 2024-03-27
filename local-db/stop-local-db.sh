#!/bin/bash

DB_NAME='spring_boot_core_local'

# Drop DB and user.
dropdb $DB_NAME --force;
dropuser $DB_NAME;

# Terminate PSQL instance, gracefully.
pg_ctl -D /opt/homebrew/var/postgres stop;
