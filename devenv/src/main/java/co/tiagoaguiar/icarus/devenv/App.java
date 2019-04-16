package co.tiagoaguiar.icarus.devenv;

import co.tiagoaguiar.icarus.devenv.controller.Fx;
import co.tiagoaguiar.icarus.devenv.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage primaryStage) {
      Settings.setup();

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

  public static void main(String[] args) {
    launch(args);
  }

}
