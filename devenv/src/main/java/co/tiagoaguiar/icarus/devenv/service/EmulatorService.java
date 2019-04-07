package co.tiagoaguiar.icarus.devenv.service;

import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.DebugLogger;
import co.tiagoaguiar.icarus.devenv.util.logging.ProcessLogger;

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

  // TODO: 07/04/19 add emulator parameter to choose
  public void start(final OnStartListener startListener) {
    new Thread(() -> {
      Process process = null;
      try {
        process = new ProcessBuilder(
                ANDROID_SDK_ROOT + "/emulator/emulator",
                "-avd",
                "Pixel_2_API_28"
        ).start();

        ProcessLogger processLogger = new ProcessLogger(process);
        String output = null;
        boolean bootCompleted = false;
        while ((output = processLogger.read()) != null) {
          DebugLogger.info(output);
          if (output.contains("completed")) {
            bootCompleted = true;
            break;
          }
        }

        startListener.onStart(bootCompleted);
      } catch (IOException e) {
        DebugLogger.error(e);
      }

    }, "Emulator-Thread").start();
  }

  public interface OnStartListener {
    void onStart(boolean bootCompleted);
  }

}
