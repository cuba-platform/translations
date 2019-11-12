@echo off

rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <language> - optional

rem Example of copying English Charts messages to zz translation folder:
rem CollectChartsMessages.bat C:\work\translations\content\en\charts C:\work\translations\content\zz

call CollectMessages.bat %1 %2 web com.haulmont.charts.web %3
