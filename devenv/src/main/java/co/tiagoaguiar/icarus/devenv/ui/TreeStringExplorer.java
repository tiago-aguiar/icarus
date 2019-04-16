package co.tiagoaguiar.icarus.devenv.ui;

import com.sun.webkit.dom.TreeWalkerImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import co.tiagoaguiar.icarus.devenv.Settings;
import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class TreeStringExplorer {

  private final TreeView<String> rootTreeView;

  public TreeStringExplorer(TreeView<String> rootTreeView) {
    this.rootTreeView = rootTreeView;
  }

  public void load() {
    try {
      TreeItem<String> rootItem = new TreeItem<>("Base");
      rootItem.setExpanded(true);

      Path rootPath = Paths.get(new File(Settings.ROOT_DIR).toURI());

      FileHelper.mapTree(rootItem, rootPath, 0);

      TreeItem<String> rootTree = rootItem.getChildren().remove(0);

      // stylesheet
      rootTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
        @Override
        public TreeCell<String> call(TreeView<String> param) {
          return new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
              super.updateItem(item, empty);

              getStylesheets().add(Settings.THEME_CSS);
              getStyleClass().add("theme-mode");
              getStyleClass().add("tree-cell");
              setText(item);
            }
          };
        }
      });

      rootTreeView.setRoot(rootTree);
    } catch (IOException e) {
      LoggerManager.error(e);
    }
  }

  public void createFile(FileExtension fileExtension, Consumer<File> consumer) {
    TreeItem<String> selectedItem = Optional.ofNullable(rootTreeView.getSelectionModel().getSelectedItem()).orElse(rootTreeView.getRoot());
    File currentDirTree = FileHelper.getCurrentDirTree(new File(selectedItem.getValue()), selectedItem);

    Path root = Paths.get(Settings.ROOT_DIR).getParent();
    final File dir = new File(root.toFile(), currentDirTree.toString());

    File parentDir = dir;
    if (!dir.isDirectory())
      parentDir = dir.getParentFile();

    Optional<String> result = new Dialogs.TextInputBuilder()
            .title("New File") // TODO: 28/03/19 multilanguage
            .contentText("Digit a Class")
            .build()
            .showAndWait();

    final File finalDir = parentDir;
    result.ifPresent(filename -> {
      try {
        File file = new File(finalDir, filename + "." + fileExtension.name().toLowerCase());
        if (file.createNewFile()) {
          if (dir.isDirectory()) {
            selectedItem.getChildren().add(new TreeItem<>(filename));
            selectedItem.getChildren().sort(Comparator.comparing(TreeItem::getValue));
          } else {
            selectedItem.getParent().getChildren().add(new TreeItem<>(filename));
            selectedItem.getParent().getChildren().sort(Comparator.comparing(TreeItem::getValue));
          }
          consumer.accept(file);
        }
      } catch (IOException e) {
        LoggerManager.error(e);
      }
    });
  }

  public File findCurrentFile(FileExtension fileExtension) {
    TreeItem<String> selectedItem = rootTreeView.getSelectionModel().getSelectedItem();

    File file = null;
    switch (fileExtension) {
      case JAVA:
        file = new File(selectedItem.getValue() + "." + fileExtension.name().toLowerCase());
    }
    File currentDirTree = FileHelper.getCurrentDirTree(file, selectedItem);

    Path root = Paths.get(Settings.ROOT_DIR).getParent();

    return new File(root.toFile(), currentDirTree.toString());
  }

}
