#!/bin/bash

if [[ "$OSTYPE" == "linux-gnu" ]]; then
	echo "linux-gnu!"

  ## Install Oracle JDK 8
  ## add-apt-repository ppa:webupd8team/java
  ## apt-get update
  ## apt-get install -y oracle-java8-installer

  ## apt-get install -y unzip make expect curl # NDK stuff

  # wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip

  ## Get NDK (https://developer.android.com/ndk/downloads/index.html)
  ## wget https://dl.google.com/android/repository/android-ndk-r15c-linux-x86_64.zip
  ## unzip android-ndk*.zip >> /dev/null

  # mkdir ${HOME}/android-sdk-linux
  # unzip sdk*.zip -d ${HOME}/android-sdk-linux

  # echo "[ICARUS] android sdk downloaded!"

    ACCEPT_LICENSES_URL=https://gist.githubusercontent.com/tiago-aguiar/30700d475a7a501dc3ec3c83c172a5fb/raw/328eb6925099df5aae3e76790f8232f0fc378f8b/accept-licenses
    ACCEPT_LICENSES_ITEM="android-sdk-license-bcbbd656|intel-android-sysimage-license-1ea702d1|android-sdk-license-2742d1c5"

    cd ${HOME}/android-sdk/tools
    curl -L -o accept-licenses $ACCEPT_LICENSES_URL
    chmod +x accept-licenses

    ./accept-licenses "./android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter platform-tools" $ACCEPT_LICENSES_ITEM  >/dev/null
    ./accept-licenses "./android update sdk --use-sdk-wrapper --all --no-ui --no-https --filter android-28" $ACCEPT_LICENSES_ITEM  >/dev/null

	echo "[ICARUS] sdkmanager start"
    cd bin
    ./sdkmanager --no_https --verbose "sources;android-28"
	echo "[ICARUS] sdkmanager end"

	echo "[ICARUS] sdkmanager images end"
    cd ../
    bin/./sdkmanager --no_https "system-images;android-28;google_apis_playstore;x86_64"
	echo "[ICARUS] sdkmanager images end"

    cd bin
    ./avdmanager -v create avd -n icarus_emulator -k "system-images;android-28;google_apis_playstore;x86_64" -g "google_apis_playstore" -d 19 --force
	echo "[ICARUS] avd created"

    echo 'export ANDROID_HOME=$HOME/android-sdk' >> ~/.bashrc
    echo 'export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools' >> ~/.bashrc
    echo 'export ANDROID_NDK_HOME=$NDK_HOME' >> ~/.bashrc

    source ~/.bashrc

elif [[ "$OSTYPE" == "darwin"* ]]; then
	echo "darwin!\n"

elif [[ "$OSTYPE" == "win32" ]]; then
	echo "windows!\n"
fi
