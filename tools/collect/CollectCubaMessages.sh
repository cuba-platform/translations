#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example of copying English CUBA messages to zz translation folder:
# ./CollectCubaMessages.sh /Users/me/translations/content/en/cuba /Users/me/translations/content/zz

./CollectMessages.sh $1 $2 global com.haulmont.cuba $3
./CollectMessages.sh $1 $2 core com.haulmont.cuba.core $3
./CollectMessages.sh $1 $2 gui com.haulmont.cuba.gui $3
./CollectMessages.sh $1 $2 web com.haulmont.cuba.web $3
./CollectMessages.sh $1 $2 desktop com.haulmont.cuba.desktop $3