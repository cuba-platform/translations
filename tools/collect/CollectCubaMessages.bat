@echo off
call CollectMessages.bat %1 %2 global com.haulmont.cuba.global %3
call CollectMessages.bat %1 %2 core com.haulmont.cuba.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.cuba.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.cuba.web %3
call CollectMessages.bat %1 %2 desktop com.haulmont.cuba.desktop %3