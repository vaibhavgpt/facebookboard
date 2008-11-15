@echo off

if exist %JAVA_HOME%\lib\endorsed\jaxb-api-2.1.jar goto EXIST

echo creation du repertoire endorsed
mkdir %JAVA_HOME%\lib\endorsed

echo copy du fichier
xcopy .\lib\jaxb-api-2.1.jar %JAVA_HOME%\lib\endorsed\jaxb-api-2.1.jar

goto EXIST

:EXIST
ant Launcher