package co.tiagoaguiar.icarus.devenv.util.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DebugLogger {

  private final Logger logger;

  public DebugLogger() {
    URL resource = DebugLogger.class.getResource("/config/app.properties");
    try {
      FileInputStream configFile = new FileInputStream(resource.getPath());
      LogManager.getLogManager().readConfiguration(configFile);
    } catch (IOException ex) {
      System.out.println("WARNING: Could not open configuration file");
      System.out.println("WARNING: Logging not configured (console output only)");
    }
    logger = Logger.getLogger("DebugLogger");
    logger.info("starting logging");
  }

  public void info(String message) {
    logger.info(message);
  }

  public void error(Throwable t) {
    logger.log(Level.SEVERE, t.getMessage(), t);
  }

}