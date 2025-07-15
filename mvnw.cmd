@ECHO OFF

SETLOCAL

SET "JAVA_EXE=C:\Program Files\Java\jdk-24\bin\java.exe"
SET "JAVAC_EXE=C:\Program Files\Java\jdk-24\bin\javac.exe"

SET "MVNW_VERBOSE=false"
IF DEFINED MAVEN_DEBUG (
  SET "MVNW_VERBOSE=true"
)

SET "MAVEN_PROJECTBASEDIR=%~dp0"
IF "%MAVEN_PROJECTBASEDIR:~-1%" == "\" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

SET "MVNW_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
SET "MVNW_WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"
SET "MVNW_DOWNLOADER_SOURCE=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\MavenWrapperDownloader.java"
SET "MVNW_DOWNLOADER_CLASS=MavenWrapperDownloader"

IF NOT EXIST "%MVNW_WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper...
  IF NOT EXIST "%JAVAC_EXE%" (
    ECHO ERROR: Java compiler not found at %JAVAC_EXE%. Please check the path.
    GOTO :EOF
  )
  "%JAVAC_EXE%" -d "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" "%MVNW_DOWNLOADER_SOURCE%"
  "%JAVA_EXE%" -cp "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" %MVNW_DOWNLOADER_CLASS% "%MAVEN_PROJECTBASEDIR%"
  IF ERRORLEVEL 1 (
    ECHO ERROR: Failed to download Maven Wrapper.
    GOTO :EOF
  )
)

SET "MAVEN_OPTS="
IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config" (
  FOR /F "usebackq tokens=*" %%a IN ("%MAVEN_PROJECTBASEDIR%\.mvn\jvm.config") DO SET "MAVEN_OPTS=%%a"
)

ECHO Launching Maven...
"%JAVA_EXE%" %MAVEN_OPTS% -jar "%MVNW_WRAPPER_JAR%" %*
