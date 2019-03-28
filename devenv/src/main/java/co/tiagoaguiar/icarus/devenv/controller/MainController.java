package co.tiagoaguiar.icarus.devenv.controller;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.ui.CodeEditor;
import co.tiagoaguiar.icarus.devenv.ui.TreeStringExplorer;
import co.tiagoaguiar.icarus.devenv.util.ShortCut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {

  @FXML private TabPane tabPaneFile;
  @FXML private MenuItem menuItemNewFile;
  @FXML private MenuItem menuItemSaveFile;
  @FXML private MenuItem menuItemLoadTree;
  @FXML private TreeView<String> treeFileExplorer;

  private TreeStringExplorer treeExplorer;
  private CodeEditor codeEditor;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    treeExplorer = new TreeStringExplorer(treeFileExplorer);
    treeExplorer.load();

    codeEditor = new CodeEditor(tabPaneFile);

    menuItemNewFile.setOnAction(event -> {
      // TODO: 28/03/19 reaload treeExplorer
      treeExplorer.createFile(FileExtension.JAVA, (fileCreated) -> {
        codeEditor.open(fileCreated, FileExtension.JAVA);
      });
    });

    treeFileExplorer.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        File file = treeExplorer.findCurrentFile();

        if (file.isFile()) {
          codeEditor.open(file, FileExtension.JAVA);
        }

      }
    });
  }

  public void onSceneCreate(Scene scene) {
    mapShortcuts(scene);
  }

  private void mapShortcuts(Scene scene) {
    // CTRL + S -> Save File
    ShortCut.ctrlS(scene, () -> {
      File currentFile = treeExplorer.findCurrentFile();
      codeEditor.save(currentFile);
    });
  }

}
