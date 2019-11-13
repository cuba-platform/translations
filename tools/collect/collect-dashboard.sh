#!/usr/bin/env bash

# Arguments:
# 1: <srcDir> - project root dir
# 2: <version> - e.g. 1.0

# For example:
# ./collect-dashboard.sh ~/work/addons/dashboard-addon 3.1

./CollectMessages.sh $1 ../../content/en/dashboard/$2/ web com.haulmont.addon.dashboard.web
./CollectMessages.sh $1 ../../content/en/dashboard/$2/ global com.haulmont.addon.dashboard
