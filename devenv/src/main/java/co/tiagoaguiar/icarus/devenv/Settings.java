package co.tiagoaguiar.icarus.devenv;

import java.io.File;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Settings {

  public static final String ICARUS_VERSION = "0.0.4";

  // TODO: 07/04/19 remove this
  public static final String ANDROID_SDK_ROOT = "/home/tiago/Android/Sdk";
  public static final String ICARUS_DOT_CONFIG = ".icarus" + ICARUS_VERSION;

  private static final String BASE_STYLE_DIR = "/css/";

  public static final String JAVA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-java-keywords.css").toExternalForm();
  public static final String CODE_AREA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-code-area.css").toExternalForm();

  public static final File SRC_FLY = new File(Settings.class.getResource("/system").getPath());
  public static final File SYSTEM_FOLDER_FLY = new File(new File(System.getProperty("user.home"), Settings.ICARUS_DOT_CONFIG), "system");
}
