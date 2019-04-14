package co.tiagoaguiar.icarus.devenv;

import co.tiagoaguiar.icarus.devenv.controller.MainController;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      Settings.setup();

//    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);

      // TODO: 14/04/19 remove this (secondary monitor for while)
//      Screen.getScreens().forEach(screen -> {
//        Rectangle2D rect = screen.getVisualBounds();
//        double x = rect.getMinX();
//        double y = rect.getMinY();
//        primaryStage.setX(x);
//        primaryStage.setY(y);
//      });

//    WelcomeController mainController = fxmlLoader.getController();
    MainController mainController = fxmlLoader.getController();
    mainController.setRootDir("/home/tiago/icarus/HelloWorld");
    mainController.onSceneCreate(scene);

    primaryStage.setTitle(String.format("Icarus Project :: %s", Settings.ICARUS_VERSION));
    primaryStage.setScene(scene);
//    primaryStage.setResizable(false);
    primaryStage.setResizable(true);
//    primaryStage.setMaximized(true);
    primaryStage.show();

    } catch (Throwable e) {
      LoggerManager.error(e);
      e.printStackTrace();
      Dialog dialog = new Dialogs.Builder()
              .title("Failed")
              .contentText(e.toString())
              .enableClose(true)
              .setResizableEnabled(true)
              .build();

      dialog.showAndWait();

    }
  }

  public static void main(String[] args) {
    launch(args);
  }

}
