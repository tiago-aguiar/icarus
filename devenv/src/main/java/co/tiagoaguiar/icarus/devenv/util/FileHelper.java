package co.tiagoaguiar.icarus.devenv.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;
import javafx.scene.control.TreeItem;

/**
 * Março, 26 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class FileHelper {

  public static Optional<Path> findFileWith(File srcDir, String text) throws IOException {
    try (Stream<Path> stream = Files.walk(srcDir.toPath())) {
      return stream.filter(sourcePath -> {
        try {
          String textOfFile = getText(sourcePath.toFile());
          return textOfFile.contains(text);
        } catch (IOException e) {
          LoggerManager.error(e);
        }
        return false;
      }).findFirst();
    }
  }

  public static void copyFolder(InputStream src, File dest) throws IOException {
    Files.copy(src, dest.toPath());
  }

  public static void copyFolder(File src, File dest) throws IOException {
    copyFolder(src, dest, null);
  }

  public static void copyFolder(File src, File dest, Consumer<Path> destFilePath) throws IOException {
    try (Stream<Path> stream = Files.walk(src.toPath())) {
      stream.forEachOrdered(sourcePath -> {
        Path destPath = dest.toPath().resolve(src.toPath().relativize(sourcePath));
        LoggerManager.info("Copy from: " + sourcePath.toString() + " to: " + destPath);
        try {
          Files.copy( sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
          if (destFilePath != null)
            destFilePath.accept(destPath);
        } catch (Exception e) {
          LoggerManager.error(e);
        }
      });
    }
  }

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
      if (!rootPath.toFile().isHidden()) {
        String fileName = rootPath.getFileName().toString();
        Optional<String> _fileName = Optional.ofNullable(fileName)
              .filter(s -> s.contains("."))
              .map(s -> s.substring(0, s.lastIndexOf(".")));

        TreeItem<String> item = new TreeItem<>(_fileName.orElseThrow(() -> new RuntimeException("Failed to load file")));
        rootItem.getChildren().add(item);
      }
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

  public static String getText(File file) throws IOException {
    List<String> lines = Files.readAllLines(file.toPath());

    String[] lineArray = new String[lines.size()];
    String[] _lineArray = lines.toArray(lineArray);
    String text = String.join("\n", _lineArray);

    return text;
  }

  private FileHelper() {
  }

}
