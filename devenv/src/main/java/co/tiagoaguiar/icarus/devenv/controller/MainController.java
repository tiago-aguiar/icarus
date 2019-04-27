package co.tiagoaguiar.icarus.devenv.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.service.AppService;
import co.tiagoaguiar.icarus.devenv.service.DeployService;
import co.tiagoaguiar.icarus.devenv.service.EmulatorService;
import co.tiagoaguiar.icarus.devenv.ui.CodeEditor;
import co.tiagoaguiar.icarus.devenv.ui.TreeStringExplorer;
import co.tiagoaguiar.icarus.devenv.util.ShortCut;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;

public class MainController extends FxController implements Initializable {

  @FXML private TabPane tabPaneFile;
  @FXML private TabPane tabPaneConsole;
  @FXML private TextArea textAreaConsole;
  @FXML private TextArea textAreaProblem;
  @FXML private MenuItem menuItemNewFile;
  @FXML private MenuItem menuItemSaveFile;
  @FXML private MenuItem menuItemLoadTree;
  @FXML private TreeView<String> treeFileExplorer;
  @FXML private Button buttonApply;
  @FXML private Button buttonTurnStart;

  private TreeStringExplorer treeExplorer;
  private CodeEditor codeEditor;

  private EmulatorService emulatorService;
  private AppService appService;
  private DeployService deployService;

  @Override
  public void onSceneCreate(Scene scene) {
    super.onSceneCreate(scene);
    mapShortcuts(scene);

    treeExplorer = new TreeStringExplorer(treeFileExplorer);
    treeExplorer.load();
    codeEditor = new CodeEditor(tabPaneFile);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupUI();

    emulatorService = new EmulatorService(); // TODO: 07/04/19 inject
    appService = new AppService(); // TODO: 07/04/19 inject
    deployService = new DeployService(); // TODO: 07/04/19 inject

    LoggerManager.setConsoleArea(textAreaConsole);
    LoggerManager.setProblemArea(textAreaProblem);

    emulatorService.setEmulatorStatusListener(statusChanged -> {
      LoggerManager.info("emulatorStatus is: " + statusChanged);
      buttonApply.setDisable(statusChanged != EmulatorService.EmulatorStatus.DEVICE_ONLINE);
      buttonTurnStart.setText(statusChanged == EmulatorService.EmulatorStatus.NO_DEVICE ? "TURN ON" : "PLAY");
      buttonTurnStart.setDisable(statusChanged == EmulatorService.EmulatorStatus.DEVICE_OFFLINE);

//      if (statusChanged == EmulatorService.EmulatorStatus.DEVICE_ONLINE) {
//        appService.run();
//      }
    });

    menuItemNewFile.setOnAction(event -> {
      treeExplorer.createFile(FileExtension.JAVA, (fileCreated) -> {
        codeEditor.open(fileCreated, FileExtension.JAVA);
      });
    });

    treeFileExplorer.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        File file = treeExplorer.findCurrentFile(FileExtension.JAVA);
        if (file.isFile())
          codeEditor.open(file, FileExtension.JAVA);
      }
    });

    buttonTurnStart.setOnAction(event -> {
      if (emulatorService.getEmulatorStatus() != EmulatorService.EmulatorStatus.DEVICE_ONLINE) {
        emulatorService.start();
      } else {
        appService.run();
      }
    });

    buttonApply.setOnAction(event -> {
      if (emulatorService.getEmulatorStatus() == EmulatorService.EmulatorStatus.DEVICE_ONLINE) {
        deployService.deploySourceCode();
        appService.applyChanges((message) -> {
          Platform.runLater(() -> LoggerManager.infoTab(message));
        });
      } else {
        LoggerManager.infoTab("Emulador deve ser inicializado! Clique no botão \"Turn On\"");
      }
    });

    buttonApply.setDisable(emulatorService.getEmulatorStatus() != EmulatorService.EmulatorStatus.DEVICE_ONLINE);
  }

  private void setupUI() {
    textAreaConsole.setFont(Settings.FONT_FIRA_CODE_REGULAR);
    textAreaConsole.setEditable(false);

    textAreaProblem.setFont(Settings.FONT_FIRA_CODE_REGULAR);
    textAreaProblem.setEditable(false);
  }

  private void mapShortcuts(Scene scene) {
    ShortCut.ctrlS(scene, () -> codeEditor.save()); // CTRL+S: Save File
  }

}
