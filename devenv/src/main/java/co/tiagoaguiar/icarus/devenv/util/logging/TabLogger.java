package co.tiagoaguiar.icarus.devenv.util.logging;


import javafx.scene.control.TextArea;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class TabLogger {

  private TextArea consoleArea;
  private TextArea problemArea;
  private static TabLogger LOGGER;

  public void setConsoleArea(TextArea consoleArea) {
    this.consoleArea = consoleArea;
  }

  public void setProblemArea(TextArea problemArea) {
    this.problemArea = problemArea;
  }

  public void info(String message) {
    if (consoleArea == null)
      throw new IllegalArgumentException("You must setConsoleArea before");
    consoleArea.appendText(message + "\n");
  }

  public void clear() {
    if (consoleArea == null)
      throw new IllegalArgumentException("You must setConsoleArea before");
    consoleArea.clear();

    if (problemArea == null)
      throw new IllegalArgumentException("You must setConsoleArea before");
    problemArea.clear();
  }

  public void error(String message) {
    if (problemArea == null)
      throw new IllegalArgumentException("You must setProblemArea before");
    if (!message.contains("EntryPoint"))
      problemArea.appendText(message + "\n");
    if (message.contains("cannot find symbol"))
      problemArea.appendText("Cannot find symbol " + "\n");
    // TODO: 16/04/19 fix log source-code
  }

}
