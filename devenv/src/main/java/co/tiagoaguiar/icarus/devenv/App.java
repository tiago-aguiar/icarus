package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.util.Optional;

import co.tiagoaguiar.icarus.devenv.controller.Fx;
import co.tiagoaguiar.icarus.devenv.controller.WelcomeController;
import co.tiagoaguiar.icarus.devenv.service.AndroidSdkService;
import co.tiagoaguiar.icarus.devenv.ui.Splash;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;



public class App extends Application {

  @Override
  public void start(Stage primaryStage) {

    if (Settings.getInstance().getAndroidSdkRoot() == null)
      loadAndroidSdk(primaryStage);
    else
      loadProject(primaryStage);
  }

  private void loadProject(Stage primaryStage) {
    Fx<WelcomeController> fx = new Fx<>("welcome");
    primaryStage.setTitle(String.format("Icarus Project :: %s", Settings.ICARUS_VERSION));
    primaryStage.setScene(fx.getScene());
//      primaryStage.setMaximized(true);
    primaryStage.show();
  }

  private void loadAndroidSdk(Stage primaryStage) {
    final ButtonType buttonExists = new ButtonType("Diretorio Existente Android SDK");

    Alert alert = new Dialogs.ConfirmationBuilder()
            .title("Configure o Android SDK")
            .headerText("Nenhum Kit de Desenvolvimento Android foi encontrado.")
            .contentText("Escolha um diretorio onde se encontra o Android SDK.")
            .buttons(buttonExists)
            .enableClose(true)
            .build();

    Optional<ButtonType> res = alert.showAndWait();
    res.ifPresent(buttonType -> {
      if (buttonType == buttonExists) {

        // load android sdk folder
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        File android = new File(directory, "/tools/android");

        // keep looping if directory is wrong
        if (!android.exists() || !android.isFile()) {
          Alert alertError = new Alert(Alert.AlertType.ERROR);
          alertError.setTitle("Error");
          alertError.setHeaderText("Ops! Falha ao carregar Android SDK");
          alertError.setContentText("Esta pasta não é um Android SDK válido");

          alertError.showAndWait();
          loadAndroidSdk(primaryStage);
          return;
        }

        final AndroidSdkService androidSdkService = new AndroidSdkService();

        Alert alertScript = new Alert(Alert.AlertType.INFORMATION);
        Label label = new Label("Baixando o Android SDK: Isso pode levar muito tempo (chegando à 60min).");
        alertScript.setTitle("Setup Android SDK");
        alertScript.setHeaderText("Configurando o Android SDK");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.setMaxHeight(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alertScript.getDialogPane().setContent(expContent);
        alertScript.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        alertScript.setOnCloseRequest(event -> {
          androidSdkService.stop();
        });

        androidSdkService.install(new AndroidSdkService.InstallationListener() {
          @Override
          public void onPrintln(String line) {
            if (line.contains("%"))
              textArea.setText(line + "\n");
            else
              textArea.appendText(line + "\n");
          }

          @Override
          public void onCompleteListener(String androidSdkPath) {
            alertScript.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            Settings.getInstance().setAndroidSdkRoot(androidSdkPath);
            LoggerManager.info("Dir SDK Loaded: " + androidSdkPath);
          }
        });

        alertScript.showAndWait();
        loadProject(primaryStage);
      }
    });
  }

  public static void main(String[] args) {
    Splash splash = new Splash();

    splash.render("/ build environment");
    Settings.getInstance().buildEnvironmentIfNeeded(splash);

    splash.render("Prepare to fly");
    splash.render("load environment");
    Settings.getInstance().loadEnvironment(splash);

    splash.dispose();

    System.gc();

    try {
      launch(args);
    } catch (Throwable t) {
      t.printStackTrace();
      try {
        LoggerManager.errorDialog(t);
      } catch (Exception e) {
        e.printStackTrace();
      }
      LoggerManager.error(t);
    }
  }

}
