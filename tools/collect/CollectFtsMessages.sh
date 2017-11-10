#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <language> - optional

# Example of copying English FTS messages to zz translation folder:
# ./CollectFtsMessages.sh /Users/me/translations/content/en/fts /Users/me/translations/content/zz

./CollectMessages.sh $1 $2 core com.haulmont.fts.core $3
./CollectMessages.sh $1 $2 web com.haulmont.fts.web $3
