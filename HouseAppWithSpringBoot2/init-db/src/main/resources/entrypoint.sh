#!/bin/bash

ARG1=$1
if [ "--help" = "${ARG1}" -o -z "${ARG1}" ]; then
    mysql --help
    exit 0
fi

if [ -f "${ARG1}" ]; then
    mysql -u "${DB_USERNAME}" -p"${DB_PASSWD}" -h "${DB_HOST}" -P "${DB_PORT}" < "${ARG1}"
    exit $?
else
    echo "${ARG1}" > /tmp/tmp.sql
    mysql -u "${DB_USERNAME}" -p"${DB_PASSWD}" -h "${DB_HOST}" -P "${DB_PORT}" < "/tmp/tmp.sql"
    exit $?
fi