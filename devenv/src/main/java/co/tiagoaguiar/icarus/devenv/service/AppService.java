package co.tiagoaguiar.icarus.devenv.service;

import java.io.File;
import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;

import static co.tiagoaguiar.icarus.devenv.Settings.ANDROID_SDK_ROOT;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AppService {

  private final String androidFolder = getClass().getResource("/android/").getPath();

  public void compile() throws IOException {
    Process process = new ProcessBuilder(
            "./gradlew",
            "clean",
            "build"
    ).directory(new File(androidFolder))
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  public void genDebugApk() throws IOException {
    Process process = new ProcessBuilder(
            "./gradlew",
            "assembleDebug"
    ).directory(new File(androidFolder))
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  private void install() throws IOException {
    Process process = new ProcessBuilder(
            ANDROID_SDK_ROOT + "/platform-tools/adb",
            "install",
            "-r",
            androidFolder + "/app/build/outputs/apk/debug/app-debug.apk"
    ).directory(new File(androidFolder))
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  private void launch() throws IOException {
    Process process = new ProcessBuilder(
            ANDROID_SDK_ROOT + "/platform-tools/adb",
            "shell",
            "am",
            "start",
            "-n",
            "co.tiagoaguiar.icarus/co.tiagoaguiar.icarus.MainActivity"
    ).directory(new File(androidFolder))
            .start();

    LoggerManager.loadProcess(process);
    LoggerManager.infoProcess();
  }

  public void run() {
    new Thread(() -> {
      try {

        LoggerManager.clear();
        genDebugApk();
        install();
        launch();

      } catch (IOException e) {
        LoggerManager.error(e);
      }
    }, "Run-Thread").start();
  }

}