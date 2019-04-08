package co.tiagoaguiar.icarus.devenv.util.logging;


import javafx.scene.control.TextArea;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class TabLogger {

  private TextArea textAreaLogger;
  private static TabLogger LOGGER;

  public void setTextAreaLogger(TextArea textAreaLogger) {
    this.textAreaLogger = textAreaLogger;
  }

  public void info(String message) {
    if (textAreaLogger == null)
      throw new IllegalArgumentException("You must setTextAreaLogger before");
    textAreaLogger.appendText(message + "\n");
  }

  public void clear() {
    if (textAreaLogger == null)
      throw new IllegalArgumentException("You must setTextAreaLogger before");
    textAreaLogger.clear();
  }
  
}
