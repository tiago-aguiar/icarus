package co.tiagoaguiar.icarus.devenv.util;

import javafx.scene.control.TextInputDialog;

/**
 * Março, 26 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Dialogs {

  public static class TextInputBuilder {

    private String title;
    private String headerText;
    private String contentText;

    public TextInputBuilder title(String title) {
      this.title = title;
      return this;
    }

    public TextInputBuilder headerText(String headerText) {
      this.headerText = headerText;
      return this;
    }

    public TextInputBuilder contentText(String contentText) {
      this.contentText = contentText;
      return this;
    }

    public TextInputDialog build() {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle(title);
      dialog.setHeaderText(headerText);
      dialog.setContentText(contentText);
      return dialog;
    }

  }

}
