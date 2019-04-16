package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.util.Optional;

import co.tiagoaguiar.icarus.devenv.controller.Fx;
import co.tiagoaguiar.icarus.devenv.controller.MainController;
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

    if (Settings.getInstance().getAndroidSdkRoot() == null) {
      showConfigSdk();
    } else {
      Fx<MainController> fx = new Fx<>("main");

      // TODO: 14/04/19 remove this (secondary monitor for while)
//      Screen.getScreens().forEach(screen -> {
//        Rectangle2D rect = screen.getVisualBounds();
//        double x = rect.getMinX();
//        double y = rect.getMinY();
//        primaryStage.setX(x);
//        primaryStage.setY(y);
//      });

      primaryStage.setTitle(String.format("Icarus Project :: %s", Settings.ICARUS_VERSION));
      primaryStage.setScene(fx.getScene());
//      primaryStage.setMaximized(true);
      primaryStage.show();
    }
  }

  private void showConfigSdk() {
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
          alertError.setHeaderText("Look, an Error Dialog");
          alertError.setContentText("Ooops, there was an error!");

          alertError.showAndWait();
          showConfigSdk();
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
