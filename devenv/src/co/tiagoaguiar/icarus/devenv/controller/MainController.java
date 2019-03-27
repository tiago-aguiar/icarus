package co.tiagoaguiar.icarus.devenv.controller;

import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainController implements Initializable {

  private static final String ROOT = "/home/tiago/android/icarus"; // TODO: 26/03/19

  @FXML
  private TabPane tabPaneFile;

  @FXML
  private MenuItem menuItemNewFile;

  @FXML
  private TreeView<String> treeFileExplorer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initFileExplorer();

    menuItemNewFile.setOnAction(event -> {
      TreeItem<String> selectedItem = treeFileExplorer.getSelectionModel().getSelectedItem();
      File currentDirTree = FileHelper.getCurrentDirTree(new File(selectedItem.getValue()), selectedItem);

      Path root = Paths.get(ROOT).getParent();
      File file = new File(root.toFile(), currentDirTree.toString());

      System.out.println(file);
    });

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

  private void initFileExplorer() {
    try {
      TreeItem<String> rootItem = new TreeItem<>("Base");
      rootItem.setExpanded(true);

      Path rootPath = Paths.get(new File(ROOT).toURI());

      FileHelper.mapTree(rootItem, rootPath, 0);

      TreeItem<String> rootTree = rootItem.getChildren().remove(0);

      treeFileExplorer.setRoot(rootTree);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
