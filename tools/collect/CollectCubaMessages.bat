rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <language> - optional

rem Example of copying English CUBA messages to zz translation folder:
rem CollectCubaMessages.bat C:\work\translations\content\en\cuba C:\work\translations\content\zz

@echo off
call CollectMessages.bat %1 %2 global com.haulmont.cuba.global %3
call CollectMessages.bat %1 %2 core com.haulmont.cuba.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.cuba.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.cuba.web %3
call CollectMessages.bat %1 %2 desktop com.haulmont.cuba.desktop %3