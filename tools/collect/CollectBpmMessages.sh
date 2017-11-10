#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example of copying English BPM messages to zz translation folder:
# ./CollectBpmMessages.sh /Users/me/translations/content/en/bpm /Users/me/translations/content/zz

./CollectMessages.sh $1 $2 global com.haulmont.bpm.global $3
./CollectMessages.sh $1 $2 core com.haulmont.bpm.core $3
./CollectMessages.sh $1 $2 gui com.haulmont.bpm.gui $3
./CollectMessages.sh $1 $2 web com.haulmont.bpm.web $3
