#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example usage:
# ./collect-charts.sh ~/work/charts ../../content/en/charts/7.2

./CollectMessages.sh $1 $2 web com.haulmont.charts.web $3
