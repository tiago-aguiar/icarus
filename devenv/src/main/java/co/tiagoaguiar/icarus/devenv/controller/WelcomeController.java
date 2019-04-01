package co.tiagoaguiar.icarus.devenv.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class WelcomeController implements Initializable {

  private Scene scene;

  @FXML
  Hyperlink linkCreateProject;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    linkCreateProject.setOnAction(event -> {
      Parent root;
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/co/tiagoaguiar/devenv/view/wizard.fxml"));
        root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        // Hide this current window
        scene.getWindow().hide();
      } catch (IOException e) {
        // TODO: 01/04/19 logger
      }
    });
  }

  public void onSceneCreate(Scene scene) {
    this.scene = scene;
  }

}
