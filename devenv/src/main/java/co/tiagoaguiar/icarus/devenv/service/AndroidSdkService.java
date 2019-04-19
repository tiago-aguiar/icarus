package co.tiagoaguiar.icarus.devenv.service;

import java.io.File;
import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Platform;

/**
 * Abril, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class AndroidSdkService implements Runnable {

  private final Thread thread;
  private InstallationListener listener;
  private Process process;

  // TODO: 19/04/19 refactor this class
  public AndroidSdkService() {
    thread = new Thread(this, "AndroidSDK-Thread");
  }

  public synchronized void install(final InstallationListener listener) {
    this.listener = listener;
    thread.start();
  }

  public synchronized void stop() {
    if (this.process.isAlive())
      this.process.destroy();
    try { thread.join(); } catch (InterruptedException e) {}
  }

  @Override
  public void run() {
    try {
        Process process = new ProcessBuilder(
                "bash",
                Settings.SDK_SCRIPT_INSTALL
        ).redirectErrorStream(true)
                .directory(new File(System.getProperty("user.home")))
                .start();

        this.process = process;
        LoggerManager.loadProcess(process);

        String output;
        while ((output = LoggerManager.lineProcess()) != null) {
          LoggerManager.infoDebug(output);
          final String finalOutput = output;
          Platform.runLater(() -> listener.println(finalOutput));
        }

        Platform.runLater(listener::onCompleteListener);

      } catch (IOException e) {
        LoggerManager.error(e, true);
      }
  }

  public interface InstallationListener {
    void println(String line);
    void onCompleteListener();
  }

}
