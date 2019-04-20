package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Abril, 01 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class WizardController extends FxController implements Initializable {

  @FXML private TextField textFieldProjectName;
  @FXML private TextField textFieldProjectDir;
  @FXML private Button buttonFinish;

  private String projectPath;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    textFieldProjectDir.setText(System.getProperty("user.home"));

    buttonFinish.setOnAction(event -> {
      projectPath = textFieldProjectDir.getText() + File.separatorChar + textFieldProjectName.getText();
      try {
        // TODO: 17/04/19 create a launcher icarus extension
        Files.createDirectory(Paths.get(projectPath));
        boolean isDirectory = Files.isDirectory(Paths.get(projectPath));

        if (!isDirectory)
          throw new IOException("Failed to create a project directory");

        Settings.getInstance().setProjectDir(projectPath);

        Fx<MainController> fx = new Fx<>("main");

        Stage stage = new Stage();
        stage.setTitle(projectPath);
        stage.setScene(fx.getScene());
        stage.setMaximized(true);
        stage.show();

        this.scene.getWindow().hide();

      } catch (IOException e) {
        LoggerManager.errorDialog(e);
      }
    });
  }

}
