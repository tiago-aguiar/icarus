package co.tiagoaguiar.icarus.devenv.util;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;

/**
 * Março, 26 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Dialogs {

  public static class ConfirmationBuilder {

    private String title;
    private String headerText;
    private String contentText;
    private ButtonType[] buttons;
    private boolean enableClose;

    public ConfirmationBuilder title(String title) {
      this.title = title;
      return this;
    }

    public ConfirmationBuilder headerText(String headerText) {
      this.headerText = headerText;
      return this;
    }

    public ConfirmationBuilder contentText(String contentText) {
      this.contentText = contentText;
      return this;
    }

    public ConfirmationBuilder buttons(ButtonType... buttons) {
      this.buttons = buttons;
      return this;
    }

    public ConfirmationBuilder enableClose(boolean enableClose) {
      this.enableClose = enableClose;
      return this;
    }

    public Alert build() {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(title);
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.getButtonTypes().clear();
      alert.getButtonTypes().addAll(buttons);
      if (enableClose) {
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = alert.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
      }
      return alert;
    }

  }

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

  public static class Builder {

    private String title;
    private String headerText;
    private String contentText;
    private boolean enableClose;
    private boolean resizableEnabled;

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder headerText(String headerText) {
      this.headerText = headerText;
      return this;
    }

    public Builder contentText(String contentText) {
      this.contentText = contentText;
      return this;
    }

    public Builder enableClose(boolean enableClose) {
      this.enableClose = enableClose;
      return this;
    }

    public Builder resizableEnabled(boolean resizableEnabled) {
      this.resizableEnabled = resizableEnabled;
      return this;
    }

    public Dialog build() {
      Dialog<String> stringDialog = new Dialog<>();
      stringDialog.setTitle(title);
      stringDialog.setContentText(contentText);
      stringDialog.setResizable(resizableEnabled);
      stringDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      if (enableClose) {
        stringDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = stringDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
      }
      return stringDialog;
    }

  }

}
