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

  static ProcessLogger processLogger = new ProcessLogger();
  static TabLogger tabLogger = new TabLogger();
  static DebugLogger debugLogger = new DebugLogger();

  public static void error(Throwable t) {
    error(t, false);
  }

  public static void error(Throwable t, boolean showDialog) {
    debugLogger.error(t);
    if (showDialog)
      new Dialogs.Builder()
              .title("Failed")
              .contentText(t.toString())
              .enableClose(true)
              .setResizableEnabled(true)
              .build()
              .showAndWait();
  }

  public static void loadProcess(Process process) {
    processLogger.setProcess(process);
  }

  public static void loadTextArea(TextArea textArea) {
    tabLogger.setTextAreaLogger(textArea);
  }

  public static String lineProcess() {
    return processLogger.read();
  }

  public static void infoProcess() {
    String val = null;
    while ((val = processLogger.read()) != null) {
      debugLogger.info(val);
      final String finalVal = val;
      Platform.runLater(() -> tabLogger.info(finalVal));
    }
  }

  public static void infoDebug(String message) {
    debugLogger.info(message);
  }

  public static void infoTab(String message) {
    Platform.runLater(() -> tabLogger.info(message));
  }

  public static void clear() {
    tabLogger.clear();
  }

}
