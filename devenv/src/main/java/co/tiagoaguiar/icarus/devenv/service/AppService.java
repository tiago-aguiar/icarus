package co.tiagoaguiar.icarus.devenv.service;


import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;

import static co.tiagoaguiar.icarus.devenv.Settings.ANDROID_SDK_ROOT;
import static co.tiagoaguiar.icarus.devenv.Settings.SYSTEM_FOLDER_FLY;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AppService {

  private final String APK_DEBUG =
          getClass().getResource("/config/app-debug.apk").getPath();

  public void compileFly() throws IOException {
    Process process = new ProcessBuilder(
            "./gradlew",
            ":fly:dynamicDex"
    ).directory(SYSTEM_FOLDER_FLY)
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  private void installApp() throws IOException {
    Process process = new ProcessBuilder(
            ANDROID_SDK_ROOT + "/platform-tools/adb",
            "install",
            "-r",
            APK_DEBUG
    ).start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  private void launchApp() throws IOException {
    Process process = new ProcessBuilder(
            ANDROID_SDK_ROOT + "/platform-tools/adb",
            "shell",
            "am",
            "start",
            "-n",
            "co.tiagoaguiar.icarus/co.tiagoaguiar.icarus.MainActivity"
    ).start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  public void run() {
    new Thread(() -> {
      try {

        LoggerManager.clear();
//        installApp();
//        launchApp();
        compileFly();

      } catch (IOException e) {
        LoggerManager.error(e);
      }
    }, "Run-Thread").start();
  }

}