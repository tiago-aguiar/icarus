package co.tiagoaguiar.icarus.devenv;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Settings {

  private static final String BASE_STYLE_DIR = "/co/tiagoaguiar/devenv/style/";

  public static final String JAVA_CSS = Settings.class.getResource(BASE_STYLE_DIR + "java-keywords.css").toExternalForm();

}
