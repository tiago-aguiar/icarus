package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.service.AppService;
import co.tiagoaguiar.icarus.devenv.service.DeployService;
import co.tiagoaguiar.icarus.devenv.service.EmulatorService;
import co.tiagoaguiar.icarus.devenv.ui.CodeEditor;
import co.tiagoaguiar.icarus.devenv.ui.TreeStringExplorer;
import co.tiagoaguiar.icarus.devenv.util.ShortCut;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;

public class MainController extends FxController implements Initializable {

  @FXML
  private TabPane tabPaneFile;
  @FXML
  private TextArea textAreaEmulator;
  @FXML
  private MenuItem menuItemNewFile;
  @FXML
  private MenuItem menuItemSaveFile;
  @FXML
  private MenuItem menuItemLoadTree;
  @FXML
  private TreeView<String> treeFileExplorer;
  @FXML
  private Button buttonPlay;
  @FXML
  private Button buttonStart;

  private TreeStringExplorer treeExplorer;
  private CodeEditor codeEditor;
  private String rootDir;

  private EmulatorService emulatorService;
  private AppService appService;
  private DeployService deployService;

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
    emulatorService = new EmulatorService(); // TODO: 07/04/19 inject
    appService = new AppService(); // TODO: 07/04/19 inject
    deployService = new DeployService(); // TODO: 07/04/19 inject

    LoggerManager.loadTextArea(textAreaEmulator);

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


    buttonStart.setOnAction(event -> {
      if (!emulatorService.isBootCompleted()) {
        emulatorService.start(bootCompleted -> {
          if (bootCompleted) {
            appService.run();
          }
        });
      } else {
        appService.run();
      }
    });

    buttonPlay.setOnAction(event -> {
      deployService.deploySourceCode(rootDir);
      appService.applyChanges();
    });
  }

  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  private void mapShortcuts(Scene scene) {
    // CTRL + S -> Save File
    ShortCut.ctrlS(scene, () -> {
      codeEditor.save();
    });
  }

}
