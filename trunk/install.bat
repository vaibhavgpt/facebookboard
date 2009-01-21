@echo off
cd bin
java -classpath .;..\lib\activation-1.1.jar;..\lib\commons-codec-1.3.jar;..\lib\commons-httpclient-3.1.jar;..\lib\commons-logging-1.0.4.jar;..\lib\facebook-java-api-2.0.5-SNAPSHOT.jar;..\lib\facebook-java-api-schema-2.0.5-SNAPSHOT.jar;..\lib\jaxb-api-2.1.jar;..\lib\jaxb-impl-2.1.3.jar;..\lib\json-20070829.jar;..\lib\json_simple.jar;..\lib\log4j-1.2.11.jar;..\lib\looks-2.1.4.jar;..\lib\stax-api-1.0-2.jar;..\lib\swing-layout-1.0.jar;..\lib\swingx-0.9.4.jar;..\lib\swingx-beaninfo-0.9.4.jar;..\lib\swingx-ws-2008_11_02.jar;..\lib\commons-lang-2.2.jar;..\lib\runtime-0.4.1.3.jar -splash:img/splashScreen.png  org.pihen.facebook.main.Setup

pause
