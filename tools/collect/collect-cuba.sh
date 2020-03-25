#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example usage:
# ./collect-cuba.sh ~/work/cuba ../../content/en/cuba/7.2

./CollectMessages.sh $1 $2 global com.haulmont.cuba $3
./CollectMessages.sh $1 $2 core com.haulmont.cuba.core $3
./CollectMessages.sh $1 $2 gui com.haulmont.cuba.gui $3
./CollectMessages.sh $1 $2 web com.haulmont.cuba.web $3