package co.tiagoaguiar.icarus.devenv.service;


import java.io.File;
import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;

import static co.tiagoaguiar.icarus.devenv.Settings.ICARUS_DOT_DIR;
import static co.tiagoaguiar.icarus.devenv.Settings.ICARUS_SYSTEM_FLY_DIR;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AppService {

  private boolean compileFly() throws IOException {
    Process process = new ProcessBuilder(
            "./gradlew",
            ":fly:dynamicDex"
    ).directory(ICARUS_SYSTEM_FLY_DIR)
            .start();

    LoggerManager.loadProcess(process);
//    LoggerManager.infoProcess("Preparing to flying...");
    LoggerManager.infoProcess();
    LoggerManager.errorProcess();

    String errorProcessLog = LoggerManager.getErrorProcessLog();
    LoggerManager.clearErrorProcessLog();

    return errorProcessLog == null;
  }

  private void deployFly() throws IOException {
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
            "push",
            "app/src/main/assets/dm.dex",
            "/sdcard/"
    ).directory(ICARUS_SYSTEM_FLY_DIR)
            .start();

    LoggerManager.loadProcess(process);
//    LoggerManager.infoProcess("Deploy...");
    LoggerManager.infoProcess();
  }

  private void notifyFly() throws IOException {
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
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
//    LoggerManager.infoProcess("Changes applied...");
    LoggerManager.infoProcess();
//    LoggerManager.errorProcess();
  }

  private boolean installed() throws IOException {
    LoggerManager.debug("Adb path: " + Settings.getInstance().adb());
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
            "shell",
            "pm",
            "list",
            "packages",
            "|",
            "grep",
            "co.tiagoaguiar.icarus"
    ).redirectErrorStream(true).start();

    LoggerManager.loadProcess(process);
    String output;
    while ((output = LoggerManager.lineProcess()) != null) {
      LoggerManager.infoProcess(output);
      if (output.contains("package"))
        return true;
    }
    return false;
  }

  private boolean isRunning() throws IOException {
    LoggerManager.debug("Adb path: " + Settings.getInstance().adb());
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
            "shell",
            "pidof",
            "co.tiagoaguiar.icarus"
    ).redirectErrorStream(true).start();

    LoggerManager.loadProcess(process);
    String output;
    while ((output = LoggerManager.lineProcess()) != null) {
      LoggerManager.infoProcess(output);
      if (output.length() > 0)
        return true;
    }
    return false;
  }

  private void installApp() throws IOException {
    File apkDest = new File(ICARUS_DOT_DIR, System.currentTimeMillis() + "app-debug.apk");
    FileHelper.copyFolder(Settings.class.getResourceAsStream(Settings.SRC_APK_DEBUG), apkDest);

    LoggerManager.debug("Adb path: " + Settings.getInstance().adb());
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
            "install",
            "-r",
            apkDest.getAbsolutePath()
    ).redirectErrorStream(true).start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();

    if (apkDest.delete())
      LoggerManager.info("apk deleted");
  }

  private void launchApp() throws IOException {
    Process process = new ProcessBuilder(Settings.getInstance().adb(),
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
        if (!installed())
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
        if (isRunning()) {
          if (compileFly()) {
            deployFly();
            notifyFly();
          } else {
            errorListener.onError("error to compile");
          }
        } else {
          errorListener.onError("the app is not running");
        }

      } catch (IOException e) {
        LoggerManager.error(e);
      }
    }, "ApplyChanges-Thread").start();
  }

  public interface OnErrorListener {
    void onError(String message);
  }

}