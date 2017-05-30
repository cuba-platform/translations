rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <language> - optional

rem Example of copying English BPM messages to zz translation folder:
rem CollectBpmMessages.bat C:\work\translations\content\en\bpm C:\work\translations\content\zz

@echo off
call CollectMessages.bat %1 %2 global com.haulmont.bpm.global %3
call CollectMessages.bat %1 %2 core com.haulmont.bpm.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.bpm.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.bpm.web %3
