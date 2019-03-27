package co.tiagoaguiar.icarus.devenv.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import javafx.scene.control.TreeItem;

/**
 * Março, 26 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class FileHelper {

  public static void mapTree(TreeItem<String> rootItem, Path rootPath, int depth) throws IOException {
    BasicFileAttributes attrs = Files.readAttributes(rootPath, BasicFileAttributes.class);

    if (attrs.isDirectory()) {
      DirectoryStream<Path> paths = Files.newDirectoryStream(rootPath);

      TreeItem<String> item = new TreeItem<>(rootPath.getFileName().toString());

      if (!rootPath.toFile().isHidden()) {
        rootItem.getChildren().add(item);
      }

      for (Path temp : paths)
        mapTree(item, temp, depth + 1);


    } else {
      if (!rootPath.toFile().isHidden())
        rootItem.getChildren().add(new TreeItem<>(rootPath.getFileName().toString()));
    }

  }

  public static File getCurrentDirTree(File file, TreeItem<String> selectedItem) {
    File f = file;
    TreeItem<String> parent = selectedItem.getParent();

    if (parent != null) {
      f = getCurrentDirTree(new File(parent.getValue(), file.getPath()), parent);
    }

    return f;
  }

  private FileHelper() {
  }


}
