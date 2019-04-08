package co.tiagoaguiar.icarus.devenv.service;

import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.DebugLogger;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import co.tiagoaguiar.icarus.devenv.util.logging.ProcessLogger;
import co.tiagoaguiar.icarus.devenv.util.logging.TabLogger;
import javafx.application.Platform;

import static co.tiagoaguiar.icarus.devenv.Settings.ANDROID_SDK_ROOT;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 *
 * emulator -list-avds
 * emulator -avd avd_name
 */
public class EmulatorService {

  private boolean bootCompleted;

  public boolean isBootCompleted() {
    return bootCompleted;
  }

  public void start(final OnStartListener startListener) {
    new Thread(() -> {
      Process process;
      try {
        process = new ProcessBuilder(
                ANDROID_SDK_ROOT + "/emulator/emulator",
                "-avd",
                // TODO: 07/04/19 add emulator parameter to choose
                "Pixel_2_API_28"
        ).start();

        LoggerManager.loadProcess(process);
        String output = null;
        while ((output = LoggerManager.lineProcess()) != null) {
          LoggerManager.infoDebug(output);
          LoggerManager.infoTab(output);
          if (output.contains("completed")) {
            this.bootCompleted = true;
            break;
          }
        }

        startListener.onStart(bootCompleted);
      } catch (IOException e) {
        LoggerManager.error(e);
      }

    }, "Emulator-Thread").start();
  }

  public interface OnStartListener {
    void onStart(boolean bootCompleted);
  }

}
