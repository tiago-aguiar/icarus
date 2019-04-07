package co.tiagoaguiar.icarus.devenv.util.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class ProcessLogger {

  private final BufferedReader stdInput;

  public ProcessLogger(Process process) {
    stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
  }

  public String read() {
    try {
      String line = stdInput.readLine();
      return line;
    } catch (IOException e) {
      DebugLogger.error(e);
      return "Failed to read input stream";
    }
  }

  public void output() {
    String s = null;
    while ((s = read()) != null)
      DebugLogger.info(s);
  }

}
