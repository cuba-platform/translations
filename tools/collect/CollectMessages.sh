#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - project root dir
# 1: <dstDir> - output dir
# 2: <moduleName>
# 3: <mainMessagePack>
# 4: <language> - optional

# Example:
# ./CollectMessages.sh /Users/me/translations/content/en/cuba /Users/me/translations/content/zz global com.haulmont.cuba.global

java -cp ./lib/groovy-all-2.4.7.jar groovy.lang.GroovyShell CollectMessages.groovy "$@"