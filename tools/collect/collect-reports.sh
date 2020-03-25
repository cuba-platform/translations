#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example usage:
# ./collect-reports.sh ~/work/reports ../../content/en/reports/7.2

./CollectMessages.sh $1 $2 global com.haulmont.reports.global $3
./CollectMessages.sh $1 $2 core com.haulmont.reports.core $3
./CollectMessages.sh $1 $2 gui com.haulmont.reports.gui $3
./CollectMessages.sh $1 $2 web com.haulmont.reports.web $3