@echo off
call CollectMessages.bat %1 %2 core com.haulmont.fts.core %3
call CollectMessages.bat %1 %2 web com.haulmont.fts.web %3
