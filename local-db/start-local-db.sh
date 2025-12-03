#!/bin/bash
source ./.settings.env

echo "Check if postgresql is installed"
echo -e "\033[33mIf postgres was not yet installed add the following to the your ~/.zshrc.\033[0m"
echo -e "echo 'export PATH="$(brew --prefix $POSTGRES_VERSION)/bin:\$PATH"' >> ~/.zshrc \n"
echo -e "\033[33mIf datebase does not exist then create the database using the following:\033[0m"
echo -e "initdb --locale=en_US.UTF-8 -E UTF-8 /opt/homebrew/var/$POSTGRES_VERSION \n"
brew list $POSTGRES_VERSION > /dev/null 2>&1 || brew install ${POSTGRES_VERSION}

echo -e "Now starting the database \n"

sql_file='dump.sql'
while getopts f: flag
do
    case "${flag}" in
        f) sql_file=${OPTARG};;
    esac
done

# Start PSQL instance. Since Postgresql@15, native language support must be set.
LC_ALL="en_US.UTF-8" pg_ctl -D /opt/homebrew/var/${POSTGRES_VERSION} start;

# Drop DB and user, to ensure only the specified data is loaded.
dropdb $DB_NAME --force;
dropuser $DB_NAME;

# Recreate the user and DB.
createuser $DB_NAME;
createdb $DB_NAME;

# Assign privileges to newly-created user.
psql $DB_NAME -c "GRANT ALL ON SCHEMA public TO ${DB_NAME};";
psql $DB_NAME -c "grant all privileges on database ${DB_NAME} to ${DB_NAME}";
# Import data from dump.
psql -U $DB_NAME -f ./$sql_file;
psql -h localhost -p 5432 -U $DB_NAME -f ./post-dump.sql;
