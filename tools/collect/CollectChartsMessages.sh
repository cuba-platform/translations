#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example of copying English Charts messages to zz translation folder:
# ./CollectChartsMessages.sh /Users/me/translations/content/en/charts /Users/me/translations/content/zz

./CollectMessages.sh $1 $2 web com.haulmont.charts.web $3
