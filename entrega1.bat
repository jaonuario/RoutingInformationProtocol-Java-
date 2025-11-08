@echo off

set SRC_DIR=src
set BIN_DIR=bin
set JAR_NAME=Entrega1.jar
set MAIN_CLASS=entrega1.Main
set MANIFEST_FILE=MANIFEST.MF

if not exist %BIN_DIR% (
    mkdir %BIN_DIR%
    echo Pasta %BIN_DIR% criada.
)

echo Iniciando a compilacao...
echo.

javac -sourcepath %SRC_DIR% -d %BIN_DIR% %SRC_DIR%\entrega1\Main.java

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO: Falha na compilacao!
    echo.
    goto :cleanup_manifest
)

echo.
echo Compilacao concluida com sucesso!
echo.

echo Criando o arquivo manifest temporario...
(
    echo Main-Class: %MAIN_CLASS%
    echo.
) > %MANIFEST_FILE%

echo Criando o JAR: %JAR_NAME%...
"C:\Program Files\Java\jdk-21\bin\jar" cvfm %JAR_NAME% %MANIFEST_FILE% -C %BIN_DIR% .

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO: Falha ao criar o arquivo JAR.
    echo.
    goto :cleanup_manifest
)

echo.
echo Arquivo JAR criado com sucesso! (%JAR_NAME%)
echo.

:cleanup_manifest
if exist %MANIFEST_FILE% (
    del %MANIFEST_FILE%
)

:end
pause