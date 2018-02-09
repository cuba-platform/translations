#!/usr/bin/env bash

# Arguments:
# 0: <srcDir> - src dir
# 1: <dstDir> - dest dir

# Example:
# ./CopyMessages.sh /Users/me/translations/content/en /Users/me/translations/content/zz

java -cp ./lib/groovy-all-2.4.7.jar groovy.lang.GroovyShell CopyMessages.groovy "$@"