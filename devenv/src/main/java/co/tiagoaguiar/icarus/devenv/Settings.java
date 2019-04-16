package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipFile;

import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.ZipHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.scene.text.Font;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Settings {

  public static final String ICARUS_VERSION = "0.0.4";

  private static final String BASE_STYLE_DIR  = "/css/";
  private static final String BASE_FONT_DIR   = "/font/";

  public static final String ANDROID_SDK_ROOT = "/home/tiago/Android/Sdk"; // FIXME: 07/04/19 remove this
  public static final String ROOT_DIR         = "/home/tiago/icarus/HelloWorld"; // FIXME: 16/04/19 remove this

  public static final File ICARUS_DOT_CONFIG_DIR  = new File(System.getProperty("user.home"), ".icarus" + ICARUS_VERSION);
  public static final File ICARUS_SYSTEM_FLY_DIR  = new File(ICARUS_DOT_CONFIG_DIR, "system");
  public static final File ICARUS_SYSTEM_FLY_ZIP  = new File(ICARUS_DOT_CONFIG_DIR, "system.zip");
  public static final InputStream SRC_FLY         = Settings.class.getResourceAsStream("/system.zip");

  public static final String JAVA_CSS       = Settings.class.getResource(BASE_STYLE_DIR + "icarus-java-keywords.css").toExternalForm();
  public static final String CODE_AREA_CSS  = Settings.class.getResource(BASE_STYLE_DIR + "icarus-code-area.css").toExternalForm();
  public static final String THEME_CSS      = Settings.class.getResource(BASE_STYLE_DIR + "icarus-theme.css").toExternalForm();

  public static final Font FONT_FIRA_CODE_REGULAR = 
    Font.loadFont(Settings.class.getResource(BASE_FONT_DIR + "firacode/FiraCode-Medium.otf").toExternalForm(), 16);

  static void setup() {
    try {
      // (hack) configure anti-aliased for fonts
      System.setProperty("prism.lcdtext", "false");

      // setup fly system
      if (!ICARUS_DOT_CONFIG_DIR.exists())
        if (!ICARUS_DOT_CONFIG_DIR.mkdir())
          LoggerManager.error(new RuntimeException("Failed to create dir: " + ICARUS_DOT_CONFIG_DIR));

      if (!ICARUS_SYSTEM_FLY_DIR.exists()) {
        FileHelper.copyFolder(Settings.SRC_FLY, Settings.ICARUS_SYSTEM_FLY_ZIP);
        ZipHelper.extract(ICARUS_SYSTEM_FLY_ZIP, ICARUS_DOT_CONFIG_DIR);
        if (ICARUS_SYSTEM_FLY_ZIP.delete())
          LoggerManager.infoDebug("system fly configured!");
      }

      LoggerManager.infoDebug("system is ready!");
    } catch (IOException e) {
      LoggerManager.error(e, true);
    }
  }

}
