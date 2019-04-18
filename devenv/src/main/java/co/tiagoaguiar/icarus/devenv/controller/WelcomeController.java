package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class WelcomeController extends FxController implements Initializable {

  @FXML private Hyperlink linkCreateProject;
  @FXML private Hyperlink linkOpenProject;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // open existing project
    linkOpenProject.setOnAction(event -> {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      File directory = directoryChooser.showDialog(scene.getWindow());

      if (directory != null) {
        Settings.getInstance().setProjectDir(directory.getAbsolutePath());

        Fx<MainController> fx = new Fx<>("main");
        Stage stage = new Stage();
        stage.setTitle(directory.getAbsolutePath());
        stage.setScene(fx.getScene());
        stage.setMaximized(true);
        stage.show();

        this.scene.getWindow().hide();
      }
    });

    // create a new project
    linkCreateProject.setOnAction(event -> {
      Fx<WizardController> fx = new Fx<>("wizard");

      Stage stage = new Stage();
      stage.setTitle("Create new project");
      stage.setScene(fx.getScene());
      stage.setResizable(false);
      stage.show();

      this.scene.getWindow().hide();
    });
  }

}
