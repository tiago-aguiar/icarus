package co.tiagoaguiar.icarus.devenv.service;

import java.io.File;
import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.OS;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Platform;

/**
 * Abril, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class AndroidSdkService implements Runnable {
  private static final String ANDROID_SDK_DIR = "/android-sdk-linux";

  private final Thread thread;
  private Process process;

  private InstallationListener listener;
  private boolean running = true;

  public AndroidSdkService() {
    thread = new Thread(this, "AndroidSDK-Thread");
  }

  public synchronized void install(final InstallationListener listener) {
    this.listener = listener;
    thread.start();
  }

  public synchronized void stop() {
    running = false;

    if (this.process.isAlive())
      this.process.destroy();
    try {
      thread.join();
    } catch (InterruptedException ignored) {
    }
  }

  @Override
  public void run() {
    try {
      OS os = Settings.getInstance().getOperationSystem();
      File script;
      switch (os) {
        case MAC:
          script = null;
          break;

        case WINDOWS:
          script = null;
          break;

        default:
          script = Settings.ICARUS_SDK_SCRIPT_INSTALL;
          break;
      }

      String homeDir = System.getProperty("user.home");
      Process process = new ProcessBuilder("bash", script.getAbsolutePath())
              .redirectErrorStream(true)
              .directory(new File(homeDir))
              .start();

      this.process = process;
      LoggerManager.loadProcess(process);

      String output;
      while ((output = LoggerManager.lineProcess()) != null && running) {
        LoggerManager.infoDebug(output);
        final String finalOutput = output;
        Platform.runLater(() -> listener.onPrintln(finalOutput));
      }

      if (running)
        Platform.runLater(() -> listener.onCompleteListener(homeDir + ANDROID_SDK_DIR));

      running = false;
    } catch (IOException e) {
      LoggerManager.error(e, true);
    }
  }

  public interface InstallationListener {

    void onPrintln(String line);

    void onCompleteListener(String androidSdkPath);

  }

}
