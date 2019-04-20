package co.tiagoaguiar.icarus.devenv.util.logging;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import co.tiagoaguiar.icarus.devenv.Settings;

/**
 * Abril, 07 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
final class IcarusLogger {

  private final Logger logger;

  IcarusLogger() {
    try {
      LogManager.getLogManager().readConfiguration(Settings.SRC_LOGGING);
    } catch (IOException ex) {
      System.out.println("ERROR: Could not open configuration file");
      System.out.println("ERROR: Logging not configured (console output only)");
      System.err.println(ex.getMessage());
    }
    logger = Logger.getLogger("IcarusLogger");

    info("starting logging");
  }

  void debug(String message) {
    logger.config(message);
  }

  void info(String message) {
    logger.info(message);
  }

  void warning(String message) {
    logger.warning(message);
  }

  void error(String message) {
    logger.severe(message);
  }

  void error(Throwable t) {
    logger.log(Level.SEVERE, t.getMessage(), t);
  }

}