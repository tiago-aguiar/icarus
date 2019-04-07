package co.tiagoaguiar.icarus.devenv;

import co.tiagoaguiar.icarus.devenv.controller.WelcomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);

    WelcomeController mainController = fxmlLoader.getController();
    mainController.onSceneCreate(scene);

    primaryStage.setTitle("Icarus Project :: 0.0.3");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
