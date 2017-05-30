rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <language> - optional

rem Example of copying English Reports messages to zz translation folder:
rem CollectCubaMessages.bat C:\work\translations\content\en\reports C:\work\translations\content\zz

@echo off
call CollectMessages.bat %1 %2 global com.haulmont.reports.global %3
call CollectMessages.bat %1 %2 core com.haulmont.reports.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.reports.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.reports.web %3
call CollectMessages.bat %1 %2 desktop com.haulmont.reports.desktop %3