rem Arguments:
rem 0: <srcDir> - project root dir
rem 1: <dstDir> - output dir
rem 2: <moduleName>
rem 3: <mainMessagePack>
rem 4: <language> - optional

rem Example:
rem CollectMessages.bat C:\work\translations\content\en\cuba C:\work\translations\content\zz global com.haulmont.cuba.global

java -cp .\lib\groovy-all-2.4.7.jar groovy.lang.GroovyShell CollectMessages.groovy %*