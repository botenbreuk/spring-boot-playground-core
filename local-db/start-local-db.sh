#!/bin/bash

DB_NAME='spring_boot_core_local'

sql_file='dump.sql'
while getopts f: flag
do
    case "${flag}" in
        f) sql_file=${OPTARG};;
    esac
done

# Start PSQL instance.
pg_ctl -D /opt/homebrew/var/postgres start;

# Drop DB and user, to ensure only the specified data is loaded.
dropdb $DB_NAME --force;
dropuser $DB_NAME;

# Recreate the user and DB.
createuser $DB_NAME;
createdb $DB_NAME;

# Assign privileges to newly-created user.
psql $DB_NAME -c "grant all privileges on database ${DB_NAME} to ${DB_NAME}";
# Import data from dump.
psql -U $DB_NAME -f ./$sql_file;
psql -h localhost -p 5432 -U $DB_NAME -f ./post-dump.sql;
