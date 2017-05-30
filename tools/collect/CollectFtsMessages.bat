rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <language> - optional

rem Example of copying English FTS messages to zz translation folder:
rem CollectFtsMessages.bat C:\work\translations\content\en\fts C:\work\translations\content\zz

@echo off
call CollectMessages.bat %1 %2 core com.haulmont.fts.core %3
call CollectMessages.bat %1 %2 web com.haulmont.fts.web %3
