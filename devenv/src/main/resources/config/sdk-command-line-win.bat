@echo off
REM echo Downloading files...
REM powershell.exe -Command (new-object System.Net.WebClient).DownloadFile('https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip','android-sdk-win.zip')
REM powershell.exe -Command Expand-Archive android-sdk-win.zip -DestinationPath %userprofile%/android-sdk

REM SET PATH=%PATH%;C:\"Program Files (x86)"\Icarus\app\jre1.8.0_211\bin

copy /y nul %userprofile%\.android\repositories.cfg

echo "Start android platform tools"
call %userprofile%\android-sdk\tools\android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter platform-tools
echo "Start android 28"
call %userprofile%\android-sdk\tools\android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter android-28
echo "Start sdkmanager sources"
call %userprofile%\android-sdk\tools\bin\sdkmanager --no_https --verbose "sources;android-28"
echo "Start sdkmanager images"
call %userprofile%\android-sdk\tools\bin\sdkmanager --no_https --verbose "system-images;android-28;google_apis_playstore;x86_64"
echo "Create avd"
call %userprofile%\android-sdk\tools\bin\avdmanager -v create avd -n icarus_emulator -k "system-images;android-28;google_apis_playstore;x86_64" -g "google_apis_playstore" -d 19 --force

SET ANDROID_HOME=%userprofile%/android-sdk
SET PATH=%PATH%;%ANDROID_HOME%/tools;%ANDROID_HOME%/platform-tools


REM notify application that daemon cmd end
@echo done > %userprofile%\.batch.temp
start C:\"Program Files (x86)"\Icarus\icarus-ide.exe
exit
