package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import co.tiagoaguiar.icarus.devenv.model.OS;
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

  private static final String OPERATION_SYSTEM = System.getProperty("os.name").toLowerCase();
  public static final String SRC_APK_DEBUG = "/config/app-debug.apk";

  public static final File ICARUS_DOT_DIR = new File(System.getProperty("user.home"), ".icarus" + ICARUS_VERSION);
  public static final File ICARUS_SYSTEM_FLY_DIR = new File(ICARUS_DOT_DIR, "system");
  public static final File ICARUS_LOG_DIR = new File(ICARUS_DOT_DIR, "log");
  public static final File ICARUS_LOG_FILE = new File(ICARUS_LOG_DIR, "application.log");
  public static final File ICARUS_CONFIG_DIR = new File(ICARUS_DOT_DIR, "config");
  public static final File ICARUS_SYSTEM_FLY_ZIP = new File(ICARUS_DOT_DIR, "system.zip");
  public static final File ICARUS_SDK_SCRIPT_INSTALL = new File(ICARUS_DOT_DIR, "sdk-script-install.sh");
  public static final File ICARUS_SDK_SCRIPT_INSTALL_WIN = new File(ICARUS_DOT_DIR, "sdk-command-line-win.bat");
  public static final File ICARUS_CONFIG_SETTINGS = new File(ICARUS_CONFIG_DIR, "settings.properties");
  public static final File ICARUS_CONFIG_BATCH_TEMP = new File(System.getProperty("user.home"), ".batch.temp");

  public static final String JAVA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-java-keywords.css").toExternalForm();
  public static final String CODE_AREA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-code-area.css").toExternalForm();
  public static final String THEME_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-theme.css").toExternalForm();

  public static final InputStream SRC_FLY = Settings.class.getResourceAsStream("/system.zip");
  public static final InputStream SRC_SDK_SCRIPT_INSTALL_LINUX = Settings.class.getResourceAsStream("/config/sdk-script-install.sh");
  public static final InputStream SRC_SDK_SCRIPT_INSTALL_WIN = Settings.class.getResourceAsStream("/config/sdk-command-line-win.bat");
  public static final InputStream SRC_LOGGING = Settings.class.getResourceAsStream("/config/logging.properties");


  // config

  public static final Font FONT_FIRA_CODE_REGULAR =
          Font.loadFont(Settings.class.getResource(BASE_FONT_DIR + "firacode/FiraCode-Medium.otf").toExternalForm(), 16);

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

      // main dot folder
      if (!ICARUS_DOT_DIR.exists())
        if (!ICARUS_DOT_DIR.mkdir())
          LoggerManager.error(new RuntimeException("Failed to create dir: " + ICARUS_DOT_DIR));

      // setup log
      if (!ICARUS_LOG_DIR.exists())
        if (!ICARUS_LOG_DIR.mkdir())
          System.out.println("Failed to crete a log folder");
        else if (!ICARUS_LOG_FILE.createNewFile())
          System.out.println("Failed to crete a log file");

      // setup fly system
      if (!ICARUS_SYSTEM_FLY_DIR.exists()) {
        FileHelper.copyFolder(Settings.SRC_FLY, Settings.ICARUS_SYSTEM_FLY_ZIP);
        ZipHelper.extract(ICARUS_SYSTEM_FLY_ZIP, ICARUS_DOT_DIR);
        if (ICARUS_SYSTEM_FLY_ZIP.delete())
          LoggerManager.info("fly configured!");
      }

      // TODO: 19/04/19 copy by operation system
      if (!ICARUS_SDK_SCRIPT_INSTALL.exists())
        FileHelper.copyFolder(Settings.SRC_SDK_SCRIPT_INSTALL_LINUX, Settings.ICARUS_SDK_SCRIPT_INSTALL);
      if (!ICARUS_SDK_SCRIPT_INSTALL_WIN.exists())
        FileHelper.copyFolder(Settings.SRC_SDK_SCRIPT_INSTALL_WIN, Settings.ICARUS_SDK_SCRIPT_INSTALL_WIN);

      // setup config
      if (!ICARUS_CONFIG_DIR.exists()) {
        if (!ICARUS_CONFIG_DIR.mkdir())
          LoggerManager.error(new RuntimeException("Failed to create dir: " + ICARUS_CONFIG_DIR));
        else if (!ICARUS_CONFIG_SETTINGS.createNewFile())
          LoggerManager.error(new RuntimeException("Failed to create file: " + ICARUS_CONFIG_SETTINGS));
      }

      LoggerManager.info("icarus is ready!");
    } catch (IOException e) {
      LoggerManager.errorDialog(e);
    }
  }

  void loadEnvironment() {
    if (getOperationSystem() == OS.WINDOWS) {
      LoggerManager.info("Windows environment load");
      if (ICARUS_CONFIG_BATCH_TEMP.exists()) {
        if (ICARUS_CONFIG_BATCH_TEMP.delete()) {
          LoggerManager.info("temp batch file deleted");
          setAndroidSdkRoot(new File(System.getProperty("user.home"), "android-sdk").getAbsolutePath());
        }
      }
    } else {
      LoggerManager.info("Linux environment load");
    }
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
      LoggerManager.errorDialog(e);
    }
    return properties;
  }

  private void commitProperties(Properties properties) {
    try {
      properties.store(new FileOutputStream(ICARUS_CONFIG_SETTINGS), null);
    } catch (Exception e) {
      LoggerManager.errorDialog(e);
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

  public String adb() {
    return androidSdkRoot + "/platform-tools/adb";
  }

  public OS getOperationSystem() {
    return OPERATION_SYSTEM.contains("win") ? OS.WINDOWS
            : OPERATION_SYSTEM.contains("mac") ? OS.MAC
            : OS.LINUX;
  }

  private static class Keys {
    private static final String ANDROID_ROOT_SDK_KEY = "android_sdk_root";
    private static final String PROJECT_ROOT_KEY = "project_root";
  }

}
