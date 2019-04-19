package co.tiagoaguiar.icarus.devenv.service;

import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;


/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 * <p>
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
      LoggerManager.clear();
      Process process;
      try {
        // FIXME: 15/04/19 logging when KVM is busy (Virtual box running)
        // FIXME: 19/04/19 saved state not turn on again
        process = new ProcessBuilder(
                Settings.getInstance().getAndroidSdkRoot() + "/emulator/emulator",
                "-avd",
                // TODO: 07/04/19 add emulator parameter to choose
                "icarus_emulator"
        ).redirectErrorStream(true)
                .start();

        LoggerManager.loadProcess(process);
        String output;
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
