#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example usage:
# ./collect-fts.sh ~/work/fts ../../content/en/fts/7.2

./CollectMessages.sh $1 $2 core com.haulmont.fts.core $3
./CollectMessages.sh $1 $2 web com.haulmont.fts.web $3
