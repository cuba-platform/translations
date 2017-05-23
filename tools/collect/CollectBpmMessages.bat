@echo off
call CollectMessages.bat %1 %2 global com.haulmont.bpm.global %3
call CollectMessages.bat %1 %2 core com.haulmont.bpm.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.bpm.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.bpm.web %3
