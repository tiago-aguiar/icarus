package co.tiagoaguiar.icarus.devenv.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane tabPaneFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Tab> tabs = tabPaneFile.getTabs();

        for (int i = 0; i < 4; i++) {

            Tab tab = new Tab("tabe " + i);
            tabs.add(tab);

            StackPane anchorPane = new StackPane();
            TextArea textArea = new TextArea();

            anchorPane.getChildren().addAll(textArea);
            tab.setContent(anchorPane);
        }


        System.out.println(location);
    }
}
