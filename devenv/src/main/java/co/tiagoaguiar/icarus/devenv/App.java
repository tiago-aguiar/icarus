package co.tiagoaguiar.icarus.devenv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import co.tiagoaguiar.icarus.devenv.controller.Fx;
import co.tiagoaguiar.icarus.devenv.controller.WelcomeController;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Application;
import javafx.application.Platform;
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
        // TODO: 17/04/19 check if Linux, Windows or Mac

        Alert alertScript = new Alert(Alert.AlertType.INFORMATION);
        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alertScript.getDialogPane().setExpandableContent(expContent);


        // TODO: 17/04/19 ask for user install unzip make expect and curl in linux
        new Thread(() -> {
          try {
            Process process = new ProcessBuilder(
                    "bash",
                    "sdk-command-line.sh"
            ).redirectErrorStream(true)
                    .directory(new File(System.getProperty("user.home")))
                    .start();
            LoggerManager.loadProcess(process);
//            LoggerManager.errorProcess();

//          alertScript.setTitle("Exception Dialog");
//          alertScript.setHeaderText("Look, an Exception Dialog");
//          alertScript.setContentText("Could not find file blabla.txt!");


            String output;
            while ((output = LoggerManager.lineProcess()) != null) {
//            LoggerManager.infoDebug(output);
//            LoggerManager.infoTab(output);
              final String finalOutput = output;
              System.out.println(output);
              Platform.runLater(() -> textArea.appendText(finalOutput + "\n"));
            }

          } catch (IOException e) {
            LoggerManager.error(e, true);
          }

        }).start();

        alertScript.showAndWait();
      }
    });
  }

  public static void main(String[] args) {
    launch(args);
  }

}
