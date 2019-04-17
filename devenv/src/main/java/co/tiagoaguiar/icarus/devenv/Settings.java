package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import co.tiagoaguiar.icarus.devenv.ui.DisplayMonitor;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.ZipHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Settings {

  static final String ICARUS_VERSION = "0.0.4";

  private static final String BASE_STYLE_DIR = "/css/";
  private static final String BASE_FONT_DIR = "/font/";

  public static final File ICARUS_DOT_DIR = new File(System.getProperty("user.home"), ".icarus" + ICARUS_VERSION);
  public static final File ICARUS_SYSTEM_FLY_DIR = new File(ICARUS_DOT_DIR, "system");
  public static final File ICARUS_CONFIG_DIR = new File(ICARUS_DOT_DIR, "config");
  public static final File ICARUS_SYSTEM_FLY_ZIP = new File(ICARUS_DOT_DIR, "system.zip");

  public static final File ICARUS_CONFIG_SETTINGS = new File(ICARUS_CONFIG_DIR, "settings.properties");

  public static final String JAVA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-java-keywords.css").toExternalForm();
  public static final String CODE_AREA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-code-area.css").toExternalForm();
  public static final String THEME_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-theme.css").toExternalForm();
  public static final InputStream SRC_FLY = Settings.class.getResourceAsStream("/system.zip");

  public static final Font FONT_FIRA_CODE_REGULAR = Font.loadFont(Settings.class.getResource(BASE_FONT_DIR + "firacode/FiraCode-Medium.otf").toExternalForm(), 16);

  private String androidSdkRoot;
  private String projectDir = "/home/tiago/icarus/HelloWorld"; // FIXME: 16/04/19 remove this

  private static Settings INSTANCE;

  private Settings() {
  }

  public static Settings getInstance() {
    if (INSTANCE == null)
      INSTANCE = new Settings();
    return INSTANCE;
  }

  void buildEnvironmentIfNeeded() {
    try {
      // (hack) configure anti-aliased for fonts
      System.setProperty("prism.lcdtext", "false");

      // setup fly system
      if (!ICARUS_DOT_DIR.exists())
        if (!ICARUS_DOT_DIR.mkdir())
          LoggerManager.error(new RuntimeException("Failed to create dir: " + ICARUS_DOT_DIR));

      if (!ICARUS_SYSTEM_FLY_DIR.exists()) {
        FileHelper.copyFolder(Settings.SRC_FLY, Settings.ICARUS_SYSTEM_FLY_ZIP);
        ZipHelper.extract(ICARUS_SYSTEM_FLY_ZIP, ICARUS_DOT_DIR);
        if (ICARUS_SYSTEM_FLY_ZIP.delete())
          LoggerManager.infoDebug("system fly configured!");
      }

      // setup config
      if (!ICARUS_CONFIG_DIR.exists()) {
        if (!ICARUS_CONFIG_DIR.mkdir())
          LoggerManager.error(new RuntimeException("Failed to create dir: " + ICARUS_CONFIG_DIR));
        else
          ICARUS_CONFIG_SETTINGS.createNewFile();
      }

      LoggerManager.infoDebug("system is ready!");
    } catch (IOException e) {
      LoggerManager.error(e, true);
    }
  }

  void loadEnvironment() {
    androidSdkRoot = loadProperties().getProperty(Keys.ANDROID_ROOT_SDK_KEY);
  }

  void loadMonitor(Stage stage, DisplayMonitor displayMonitor) {
    // TODO: 17/04/19 identify monitor
    if (displayMonitor == DisplayMonitor.SECONDARY) {
      Screen.getScreens().forEach(screen -> {
        Rectangle2D rect = screen.getVisualBounds();
        double x = rect.getMinX();
        double y = rect.getMinY();
        stage.setX(x);
        stage.setY(y);
      });
    }
  }

  private Properties loadProperties() {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(ICARUS_CONFIG_SETTINGS));
    } catch (IOException e) {
      LoggerManager.error(e, true);
    }
    return properties;
  }

  private void commitProperties(Properties properties) {
    try {
      properties.store(new FileOutputStream(ICARUS_CONFIG_SETTINGS), null);
    } catch (Exception e) {
      LoggerManager.error(e, true);
    }
  }

  public String getAndroidSdkRoot() {
    return androidSdkRoot;
  }

  void setAndroidSdkRoot(String androidSdkRoot) {
    this.androidSdkRoot = androidSdkRoot;
    Properties properties = loadProperties();
    properties.setProperty(Keys.ANDROID_ROOT_SDK_KEY, androidSdkRoot);
    commitProperties(properties);
  }

  public String getProjectDir() {
    return projectDir;
  }

  public void setProjectDir(String projectDir) {
    this.projectDir = projectDir;
    Properties properties = loadProperties();
    properties.setProperty(Keys.PROJECT_ROOT_KEY, projectDir);
    commitProperties(properties);
  }

  private static class Keys {
    private static final String ANDROID_ROOT_SDK_KEY = "android_sdk_root";
    private static final String PROJECT_ROOT_KEY = "project_root";
  }

}
