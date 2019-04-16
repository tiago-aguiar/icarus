package co.tiagoaguiar.icarus.devenv.service;


import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;

import static co.tiagoaguiar.icarus.devenv.Settings.ANDROID_SDK_ROOT;
import static co.tiagoaguiar.icarus.devenv.Settings.ICARUS_SYSTEM_FLY_DIR;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AppService {

  private static final String APK_DEBUG = AppService.class.getResource("/config/app-debug.apk").getPath();
  private static final String ADB       = ANDROID_SDK_ROOT + "/platform-tools/adb";  // TODO: 09/04/19 add ADB in Settings

  private boolean compileFly() throws IOException {
    Process process = new ProcessBuilder(
            "./gradlew",
            ":fly:dynamicDex"
    ).directory(ICARUS_SYSTEM_FLY_DIR)
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess("Preparing to flying...");
    LoggerManager.errorProcess();

    String errorProcessLog = LoggerManager.getErrorProcessLog();
    LoggerManager.clearErrorProcessLog();

    return errorProcessLog == null;
  }

  private void deployFly() throws IOException {
    Process process = new ProcessBuilder(ADB,
            "push",
            "app/src/main/assets/dm.dex",
            "/sdcard/"
    ).directory(ICARUS_SYSTEM_FLY_DIR)
            .start();
  }

  private void notifyFly() throws IOException {
    Process process = new ProcessBuilder(ADB,
            "shell",
            "am",
            "broadcast",
            "-a",
            "co.tiagoaguiar.icarus.RELOAD",
            "--es",
            "file",
            "\"dm.dex\"",
            "-n",
            "co.tiagoaguiar.icarus/.io.AdbBroadcastReceiver"
    ).directory(ICARUS_SYSTEM_FLY_DIR)
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess("Changes applied...");
  }

  private void installApp() throws IOException {
    Process process = new ProcessBuilder(ADB,
            "install",
            "-r",
            APK_DEBUG
    ).start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  private void launchApp() throws IOException {
    Process process = new ProcessBuilder(ADB,
            "shell",
            "am",
            "start",
            "-n",
            "co.tiagoaguiar.icarus/co.tiagoaguiar.icarus.app.MainActivity"
    ).start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  public void run() {
    new Thread(() -> {
      try {

        LoggerManager.clear();
        installApp();
        launchApp();

      } catch (IOException e) {
        LoggerManager.error(e);
      }
    }, "Run-Thread").start();
  }

  public void applyChanges(final OnErrorListener errorListener) {
    new Thread(() -> {
      try {

        LoggerManager.clear();
        if (compileFly()) {
          deployFly();
          notifyFly();
        } else {
          errorListener.onError();
        }

      } catch (IOException e) {
        LoggerManager.error(e);
      }
    }, "ApplyChanges-Thread").start();
  }

  public interface OnErrorListener {
    void onError();
  }

}