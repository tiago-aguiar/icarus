package co.tiagoaguiar.icarus.devenv.service;

import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Platform;


/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 * <p>
 * emulator -list-avds
 * emulator -avd avd_name
 */
public class EmulatorService {

  private final Object watcherLock = new Object();

  private Thread emulatorStatusWatcher;
  private EmulatorStatusListener emulatorStatusListener;
  private EmulatorStatus status;

  public EmulatorService() {
    startStatusWatcher();
  }

  private void setEmulatorStatus(EmulatorStatus status) {
    synchronized (watcherLock) {
      this.status = status;
    }
  }

  public EmulatorStatus getEmulatorStatus() {
    synchronized (watcherLock) {
      return this.status;
    }
  }

  public void setEmulatorStatusListener(EmulatorStatusListener emulatorStatusListener) {
    this.emulatorStatusListener = emulatorStatusListener;
  }

  private void startStatusWatcher() {
    emulatorStatusWatcher = new Thread(() -> {
      while (true) {
        try {
          Process process = new ProcessBuilder(Settings.getInstance().adb(),
                  "shell",
                  "getprop",
                  "sys.boot_completed",
                  "|",
                  "tr",
                  "-d",
                  "'\\r'")
                  .redirectErrorStream(true)
                  .start();

          LoggerManager.loadProcess(process);
          String output;
          while ((output = LoggerManager.lineProcess()) != null) {
            if (output.contains("no devices") && status != EmulatorStatus.NO_DEVICE) {
              setEmulatorStatus(EmulatorStatus.NO_DEVICE);
              LoggerManager.infoTab("Dispositivo Desligado.");
              LoggerManager.info("no device found yet, " + output);
              if (emulatorStatusListener != null)
                Platform.runLater(() -> emulatorStatusListener.onChanged(EmulatorStatus.NO_DEVICE));
            } else if (output.contains("offline") && status != EmulatorStatus.DEVICE_OFFLINE) {
              setEmulatorStatus(EmulatorStatus.DEVICE_OFFLINE);
              LoggerManager.infoTab("Dispositivo encontrado, mas offline...");
              LoggerManager.info("device is offline for while, " + output);
              if (emulatorStatusListener != null)
                Platform.runLater(() -> emulatorStatusListener.onChanged(EmulatorStatus.DEVICE_OFFLINE));
            } else if (output.contains("1") && status != EmulatorStatus.DEVICE_ONLINE) {
              setEmulatorStatus(EmulatorStatus.DEVICE_ONLINE);
              LoggerManager.infoTab("Dispositivo está online!");
              LoggerManager.info("device is online!, " + output);
              if (emulatorStatusListener != null)
                Platform.runLater(() -> emulatorStatusListener.onChanged(EmulatorStatus.DEVICE_ONLINE));
            }
          }
        } catch (IOException e) {
          LoggerManager.error(e);
        } finally {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {
          }
        }
      }
    }, "EmulatorstatusWatcher-Thread");

    emulatorStatusWatcher.start();
  }

  public void stopStatusWatcher() {
    try {
      emulatorStatusWatcher.join();
    } catch (InterruptedException e) {
      LoggerManager.error(e);
    }
  }

  public void start() {
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
        LoggerManager.infoProcess();

      } catch (IOException e) {
        LoggerManager.error(e);
      }

    }, "Emulator-Thread").start();
  }

  public interface EmulatorStatusListener {
    void onChanged(EmulatorStatus status);
  }

  public enum EmulatorStatus {
    NO_DEVICE,
    DEVICE_OFFLINE,
    DEVICE_ONLINE
  }

}
