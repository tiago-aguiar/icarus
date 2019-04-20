package co.tiagoaguiar.icarus.devenv.util.logging;

import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class LoggerManager {

  private static ProcessLogger processLogger = new ProcessLogger();
  private static TabLogger tabLogger = new TabLogger();
  private static IcarusLogger icarusLogger = new IcarusLogger();

  private static String errorProcessLog;

  public static void setConsoleArea(TextArea textArea) {
    tabLogger.setConsoleArea(textArea);
  }

  public static void setProblemArea(TextArea textArea) {
    tabLogger.setProblemArea(textArea);
  }

  public static void error(Throwable t) {
    icarusLogger.error(t);
  }

  public static void errorDialog(Throwable t) {
    icarusLogger.error(t);
    new Dialogs.Builder()
            .title("Failed")
            .contentText(t.toString())
            .enableClose(true)
            .resizableEnabled(true)
            .build()
            .showAndWait();
  }

  public static void loadProcess(Process process) {
    processLogger.setProcess(process);
  }

  public static String lineProcess() {
    return processLogger.read();
  }

  public static void infoProcess(String message) {
    if (message != null)
      Platform.runLater(() -> tabLogger.info(message));

    String val;
    while ((val = processLogger.read()) != null) {
      icarusLogger.info(val);
      final String finalVal = val;
      if (message == null)
        Platform.runLater(() -> tabLogger.info(finalVal));
    }
  }

  public static void infoProcess() {
    infoProcess(null);
  }

  public static String getErrorProcessLog() {
    return errorProcessLog;
  }

  public static void clearErrorProcessLog() {
    LoggerManager.errorProcessLog = null;
  }

  public static void errorProcess() {
    String val;
    while ((val = processLogger.readError()) != null) {
      icarusLogger.warning(val);
      errorProcessLog += val;
      final String finalVal = val;
      Platform.runLater(() -> tabLogger.error(finalVal));
    }
  }

  public static void info(String message) {
    icarusLogger.info(message);
  }

  public static void debug(String message) {
    icarusLogger.debug(message);
  }

  public static void infoTab(String message) {
    Platform.runLater(() -> tabLogger.info(message));
  }

  public static void clear() {
    tabLogger.clear();
  }

}
