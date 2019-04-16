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
  private BufferedReader errInput;

  public void setProcess(Process process) {
    // TODO: 09/04/19 handler ErrorStream and InputStream 
    stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    errInput = new BufferedReader(new InputStreamReader(process.getErrorStream()));
  }

  public String read() {
    if (stdInput == null)
      throw new IllegalStateException("You must setProcess before");
    try {
      return stdInput.readLine();
    } catch (IOException e) {
      LoggerManager.error(e);
      return "Failed to read input stream";
    }
  }

  public String readError() {
    if (errInput == null)
      throw new IllegalStateException("You must setProcess before");
    try {
      return errInput.readLine();
    } catch (IOException e) {
      LoggerManager.error(e);
      return "Failed to read input stream";
    }
  }

}
