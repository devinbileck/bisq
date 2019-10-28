:: This script will extract and overwrite necessary files for running Bitcoin core and Bisq in regtest mode.
:: Prior to running this script, ensure Bitcoin core and any regtest instances of Bisq are not running.

@echo off

title DAO setup

if exist "%TEMP%\dao-setup" (
    echo Removing existing %TEMP%\dao-setup
    rmdir /S /Q "%TEMP%\dao-setup"
)
echo Extracting dao-setup.zip to %TEMP%
powershell -Command "Expand-Archive dao-setup.zip -DestinationPath $env:temp -Force"

if exist "%AppData%\Bitcoin\regtest" (
    echo Removing existing regtest folder %AppData%\Bitcoin\regtest
    rmdir /S /Q "%AppData%\Bitcoin\regtest"
)
echo Moving %TEMP%\dao-setup\Bitcoin-regtest\regtest to %AppData%\Bitcoin
move "%TEMP%\dao-setup\Bitcoin-regtest\regtest" "%AppData%\Bitcoin"

if not exist "%AppData%\Bitcoin\bitcoin.conf" (
    echo Moving %TEMP%\dao-setup\Bitcoin-regtest\bitcoin.conf to %AppData%\Bitcoin
    move "%TEMP%\dao-setup\Bitcoin-regtest\bitcoin.conf" "%AppData%\Bitcoin"
    echo Updating %AppData%\Bitcoin\bitcoin.conf
    powershell -Command "(gc %AppData%\Bitcoin\bitcoin.conf) -replace 'blocknotify', '#blocknotify' | Out-File -encoding ASCII %AppData%\Bitcoin\bitcoin.conf"
    echo blocknotify="%%AppData%%\Bitcoin\blocknotify.bat" %%s>>"%AppData%\Bitcoin\bitcoin.conf"
)

if not exist "%AppData%\Bitcoin\blocknotify.bat" (
    echo Moving %TEMP%\dao-setup\Bitcoin-regtest\blocknotify.bat to %AppData%\Bitcoin
    move "%TEMP%\dao-setup\Bitcoin-regtest\blocknotify.bat" "%AppData%\Bitcoin"
)

echo Removing existing regtest application data directories (%AppData%\bisq-BTC_REGTEST_*)
for /f "delims=" %%i in ('dir /a:d /b %AppData%\bisq-BTC_REGTEST_*') do echo %AppData%\%%i && rmdir /S /Q %AppData%\%%i

echo Moving %TEMP%\dao-setup\bisq-BTC_REGTEST_Alice_dao to %AppData%\bisq-BTC_REGTEST_Alice_dao
move "%TEMP%\dao-setup\bisq-BTC_REGTEST_Alice_dao" "%AppData%\bisq-BTC_REGTEST_Alice_dao"

echo Moving %TEMP%\dao-setup\bisq-BTC_REGTEST_Bob_dao to %AppData%\bisq-BTC_REGTEST_Bob_dao
move "%TEMP%\dao-setup\bisq-BTC_REGTEST_Bob_dao" "%AppData%\bisq-BTC_REGTEST_Bob_dao"

echo Removing %TEMP%\dao-setup
rmdir /S /Q "%TEMP%\dao-setup"
