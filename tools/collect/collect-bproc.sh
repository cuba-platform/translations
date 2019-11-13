#!/usr/bin/env bash

# Arguments:
# 1: <srcDir> - project root dir
# 2: <version> - e.g. 1.0

# For example:
# ./collect-bproc.sh ~/work/addons/bproc 1.0

./CollectMessages.sh $1 ../../content/en/bproc/$2/ web com.haulmont.addon.bproc.web
./CollectMessages.sh $1 ../../content/en/bproc/$2/ global com.haulmont.addon.bproc
