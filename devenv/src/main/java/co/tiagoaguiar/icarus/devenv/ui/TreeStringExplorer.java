package co.tiagoaguiar.icarus.devenv.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

import co.tiagoaguiar.icarus.devenv.model.FileExtension;
import co.tiagoaguiar.icarus.devenv.util.Dialogs;
import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class TreeStringExplorer {

  private final String rootDir;
  private final TreeView<String> rootTreeView;

  public TreeStringExplorer(String rootDir, TreeView<String> rootTreeView) {
    this.rootDir = rootDir;
    this.rootTreeView = rootTreeView;
  }

  public void load() {
    try {
      TreeItem<String> rootItem = new TreeItem<>("Base");
      rootItem.setExpanded(true);

      Path rootPath = Paths.get(new File(rootDir).toURI());

      FileHelper.mapTree(rootItem, rootPath, 0);

      TreeItem<String> rootTree = rootItem.getChildren().remove(0);

      rootTreeView.setRoot(rootTree);

    } catch (IOException e) {
      // TODO: 28/03/19 Logger
    }
  }

  public void createFile(FileExtension fileExtension, Consumer<File> consumer) {
    TreeItem<String> selectedItem = rootTreeView.getSelectionModel().getSelectedItem();
    File currentDirTree = FileHelper.getCurrentDirTree(new File(selectedItem.getValue()), selectedItem);

    Path root = Paths.get(rootDir).getParent();
    File dir = new File(root.toFile(), currentDirTree.toString());

    if (!dir.isDirectory())
      dir = dir.getParentFile();

    Optional<String> result = new Dialogs.TextInputBuilder()
            .title("New File") // TODO: 28/03/19 multilanguage
            .contentText("Digit a Class")
            .build()
            .showAndWait();

    File finalDir = dir;
    result.ifPresent(filename -> {
      try {
        File file = new File(finalDir, filename + "." + fileExtension.name().toLowerCase());
        if (file.createNewFile())
          consumer.accept(file);
      } catch (IOException e) {
        // TODO: 28/03/19 Logger
      }
    });
  }

  public File findCurrentFile() {
    TreeItem<String> selectedItem = rootTreeView.getSelectionModel().getSelectedItem();

    File currentDirTree = FileHelper.getCurrentDirTree(new File(selectedItem.getValue()), selectedItem);

    Path root = Paths.get(rootDir).getParent();

    return new File(root.toFile(), currentDirTree.toString());
  }

}
