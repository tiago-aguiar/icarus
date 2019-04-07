package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.ui.CodeEditor;
import co.tiagoaguiar.icarus.devenv.ui.TreeStringExplorer;
import co.tiagoaguiar.icarus.devenv.util.ShortCut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

public class MainController extends FxController implements Initializable {

  @FXML private TabPane tabPaneFile;
  @FXML private MenuItem menuItemNewFile;
  @FXML private MenuItem menuItemSaveFile;
  @FXML private MenuItem menuItemLoadTree;
  @FXML private TreeView<String> treeFileExplorer;

  private TreeStringExplorer treeExplorer;
  private CodeEditor codeEditor;
  private String rootDir;

  @Override
  public void onSceneCreate(Scene scene) {
    super.onSceneCreate(scene);
    mapShortcuts(scene);

    treeExplorer = new TreeStringExplorer(rootDir, treeFileExplorer);
    treeExplorer.load();

    codeEditor = new CodeEditor(tabPaneFile);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuItemNewFile.setOnAction(event -> {
      // TODO: 28/03/19 reload treeExplorer
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

  void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  private void mapShortcuts(Scene scene) {
    // CTRL + S -> Save File
    ShortCut.ctrlS(scene, () -> {
      File currentFile = treeExplorer.findCurrentFile();
      codeEditor.save(currentFile);
    });
  }

}
