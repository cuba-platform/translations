@echo off
call CollectMessages.bat %1 %2 global com.haulmont.reports.global %3
call CollectMessages.bat %1 %2 core com.haulmont.reports.core %3
call CollectMessages.bat %1 %2 gui com.haulmont.reports.gui %3
call CollectMessages.bat %1 %2 web com.haulmont.reports.web %3
call CollectMessages.bat %1 %2 desktop com.haulmont.reports.desktop %3