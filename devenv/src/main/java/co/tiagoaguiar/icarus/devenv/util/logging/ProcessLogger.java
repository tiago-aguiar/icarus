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

  private BufferedReader stdInput;

  public void setProcess(Process process) {
    // TODO: 09/04/19 handler ErrorStream and InputStream 
    stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
  }

  public String read() {
    if (stdInput == null)
      throw new IllegalStateException("You must setProcess before");
    try {
      String line = stdInput.readLine();
      return line;
    } catch (IOException e) {
      LoggerManager.error(e);
      return "Failed to read input stream";
    }
  }

}
