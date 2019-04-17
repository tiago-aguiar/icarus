package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.util.Optional;

import co.tiagoaguiar.icarus.devenv.controller.Fx;
import co.tiagoaguiar.icarus.devenv.controller.WelcomeController;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) {
    Settings.getInstance().buildEnvironmentIfNeeded();
    Settings.getInstance().loadEnvironment();

    if (Settings.getInstance().getAndroidSdkRoot() == null)
      loadAndroidSdk();

    loadProject(primaryStage);
  }

  private void loadProject(Stage primaryStage) {
    Fx<WelcomeController> fx = new Fx<>("welcome");
    primaryStage.setTitle(String.format("Icarus Project :: %s", Settings.ICARUS_VERSION));
    primaryStage.setScene(fx.getScene());
//      primaryStage.setMaximized(true);
    primaryStage.show();
  }

  private void loadAndroidSdk() {
    final ButtonType buttonExists = new ButtonType("Diretório Existente Android SDK");
    final ButtonType buttonInstall = new ButtonType("Instalar o Android SDK");

    Alert alert = new Dialogs.ConfirmationBuilder()
            .title("Configuração do Android SDK")
            .headerText("Nenhum Kit de Desenvolvimento Android foi encontrado.")
            .contentText("Escolhe um diretório onde se encontra o Android SDK ou Instale um novo.")
            .buttons(buttonExists, buttonInstall)
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
          loadAndroidSdk();
          return;
        }

        Settings.getInstance().setAndroidSdkRoot(directory.getAbsolutePath());
        LoggerManager.infoDebug("Dir SDK Loaded: " + directory.getAbsolutePath());

      } else if (buttonType == buttonInstall) {

      }
    });
  }

  public static void main(String[] args) {
    launch(args);
  }

}
