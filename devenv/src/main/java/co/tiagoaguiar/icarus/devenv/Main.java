package co.tiagoaguiar.icarus.devenv;

import co.tiagoaguiar.icarus.devenv.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/co/tiagoaguiar/devenv/view/main.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 1280, 768);

    MainController mainController = fxmlLoader.getController();
    mainController.onSceneCreate(scene);

    primaryStage.setTitle("Icarus 0.0.1");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
