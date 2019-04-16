package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class WelcomeController extends FxController implements Initializable {

  @FXML
  Hyperlink linkCreateProject;
  @FXML
  Hyperlink linkOpenProject;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    linkOpenProject.setOnAction(event -> {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      File directory = directoryChooser.showDialog(scene.getWindow());

      // TODO: 07/04/19 WIZARD CONTROLLER AND THIS OBJECT USES THIS PROCESS... WRAP IT
      if (directory != null) {
        try {
          Parent root;
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
          root = fxmlLoader.load();

          Scene scene = new Scene(root);

          MainController controller = fxmlLoader.getController();
          controller.onSceneCreate(scene);

          Stage stage = new Stage();
          stage.setTitle(directory.getAbsolutePath());
          stage.setScene(scene);
          stage.setMaximized(true);
          stage.show();

          this.scene.getWindow().hide();
        } catch (IOException e) {
          // TODO: 07/04/19 logger
        }
      }

    });

    linkCreateProject.setOnAction(event -> {
      Parent root;
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wizard.fxml"));
        root = fxmlLoader.load();

        Scene scene = new Scene(root);

        WizardController controller = fxmlLoader.getController();
        controller.onSceneCreate(scene);

        Stage stage = new Stage();
        stage.setTitle("Create new project");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        this.scene.getWindow().hide();
      } catch (IOException e) {
        // TODO: 01/04/19 logger
      }
    });
  }

}
