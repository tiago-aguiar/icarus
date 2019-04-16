package co.tiagoaguiar.icarus.devenv.controller;

import java.io.IOException;

import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Abril, 16 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Fx<T extends FxController> {

  private final String fxmlFilename;

  private FXMLLoader fxmlLoader;
  private Scene scene;

  public Fx(String fxmlFilename) {
    this.fxmlFilename = fxmlFilename;

    load();
  }

  private void load() {
    Parent root;
    try {
      fxmlLoader = new FXMLLoader(Fx.class.getResource("/fxml/" + fxmlFilename + ".fxml"));
      root = fxmlLoader.load();
      scene = new Scene(root);
      getController().onSceneCreate(scene);
    } catch (IOException e) {
      LoggerManager.error(e, true);
    }
  }

  public Scene getScene() {
    return scene;
  }

  public T getController() {
    return fxmlLoader.getController();
  }

}
