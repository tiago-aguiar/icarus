package co.tiagoaguiar.icarus.devenv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Icarus 0.0.1");
        primaryStage.setScene(new Scene(root, 1280, 768));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
