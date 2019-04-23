@echo off
echo Downloading files...

REM powershell.exe -Command (new-object System.Net.WebClient).DownloadFile('https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip','android-sdk-win.zip')
REM powershell.exe -Command Expand-Archive android-sdk-win.zip -DestinationPath %userprofile%/android-sdk
timeout /t 10

REM TODO será preciso baixar o JRE e empacotar junto do projeto
REM SET PATH=%PATH%;c:\Users\tiago\jre1.8.0_211\bin

copy /y nul %userprofile%\.android\repositories.cfg

echo "Start android platform tools"
REM call %userprofile%\android-sdk\tools\android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter platform-tools
echo "Start android 28"
REM call %userprofile%\android-sdk\tools\android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter android-28
echo "Start sdkmanager sources"
REM call %userprofile%\android-sdk\tools\bin\sdkmanager --no_https --verbose "sources;android-28"
echo "Start sdkmanager images"
REM call %userprofile%\android-sdk\tools\bin\sdkmanager --no_https --verbose "system-images;android-28;google_apis_playstore;x86_64"
echo "Create avd"
REM call %userprofile%\android-sdk\tools\bin\avdmanager -v create avd -n icarus_emulator -k "system-images;android-28;google_apis_playstore;x86_64" -g "google_apis_playstore" -d 19 --force

REM SET PATH=%PATH%;%ANDROID_HOME%/tools;%ANDROID_HOME%/platform-tools

REM notify application that daemon cmd end
@echo done > %userprofile%\.batch.temp
start %userprofile%\AppData\Local\icarus-ide\icarus-ide.exe
exit
