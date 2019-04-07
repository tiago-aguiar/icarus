package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Abril, 01 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public
class WizardController extends FxController implements Initializable {

  @FXML
  TextField textFieldProjectName;
  @FXML
  TextField textFieldProjectDir;
  @FXML
  Button buttonFinish;

  private String projectPath;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    textFieldProjectDir.setText(System.getProperty("user.home"));

    buttonFinish.setOnAction(event -> {
      projectPath = textFieldProjectDir.getText() + File.separatorChar + textFieldProjectName.getText();
      try {
        Files.createDirectory(Paths.get(projectPath));

        // TODO: 07/04/19 WELCOME CONTROLLER AND THIS OBJECT USES THIS PROCESS... WRAP IT
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        root = fxmlLoader.load();

        Scene scene = new Scene(root);

        MainController controller = fxmlLoader.getController();
        controller.setRootDir(projectPath);
        controller.onSceneCreate(scene);

        Stage stage = new Stage();
        stage.setTitle(projectPath);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        this.scene.getWindow().hide();
      } catch (IOException e) {
        e.printStackTrace();
        // TODO: 01/04/19 logger
      }
    });
  }

}
