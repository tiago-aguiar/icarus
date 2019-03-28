package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.ui.CodeEditor;
import co.tiagoaguiar.icarus.devenv.ui.TreeStringExplorer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

public class MainController implements Initializable {

  @FXML private TabPane tabPaneFile;
  @FXML private MenuItem menuItemNewFile;
  @FXML private MenuItem menuItemLoadTree;
  @FXML private TreeView<String> treeFileExplorer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TreeStringExplorer treeExplorer = new TreeStringExplorer(treeFileExplorer);
    treeExplorer.load();

    CodeEditor codeEditor = new CodeEditor(tabPaneFile);

    menuItemNewFile.setOnAction(event -> {
      // TODO: 28/03/19 reaload treeExplorer
      treeExplorer.createFile(FileExtension.JAVA, (fileCreated) -> {
        codeEditor.open(fileCreated, FileExtension.JAVA);
      });
    });

    treeFileExplorer.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        File file = treeExplorer.findCurrentFile();

        if (file.isFile())
          codeEditor.open(file, FileExtension.JAVA);

      }
    });
  }

}
