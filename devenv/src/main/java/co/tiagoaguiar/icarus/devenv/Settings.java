package co.tiagoaguiar.icarus.devenv;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Settings {

  // TODO: 07/04/19 remove this
  public static final String ANDROID_SDK_ROOT = "/home/tiago/Android/Sdk";

  private static final String BASE_STYLE_DIR = "/css/";

  public static final String JAVA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-java-keywords.css").toExternalForm();
  public static final String CODE_AREA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "icarus-code-area.css").toExternalForm();

}
