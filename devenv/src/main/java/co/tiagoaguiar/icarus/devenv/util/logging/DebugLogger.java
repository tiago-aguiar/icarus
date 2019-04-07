package co.tiagoaguiar.icarus.devenv.util.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DebugLogger {

  private static Logger logger = null;

  static {
    try {
      URL resource = DebugLogger.class.getResource("/config/app.properties");
      FileInputStream configFile = new FileInputStream(resource.getPath());
      LogManager.getLogManager().readConfiguration(configFile);
      logger = Logger.getLogger("DebugLogger");
    } catch (IOException ex) {
      System.out.println("WARNING: Could not open configuration file");
      System.out.println("WARNING: Logging not configured (console output only)");
    }
    logger.info("starting myApp");
  }

  public static void info(String message) {
    logger.info(message);
  }

  public static void error(IOException e) {
    logger.log(Level.SEVERE, e.getMessage(), e);
  }

}