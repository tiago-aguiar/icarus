package co.tiagoaguiar.icarus.devenv.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import co.tiagoaguiar.icarus.devenv.util.FileHelper;
import co.tiagoaguiar.icarus.devenv.util.logging.LoggerManager;

import static co.tiagoaguiar.icarus.devenv.Settings.ICARUS_SYSTEM_FLY_DIR;

/**
 * Abril, 09 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DeployService {


  public void deploySourceCode(String rootDir) {
    try {
      // setup entry_point
      Optional<Path> main = findMain(rootDir);
      if (main.isPresent()) {
        // write main entry_point
        File icarusSrcDir = new File(ICARUS_SYSTEM_FLY_DIR, "fly/src/main/java/co/tiagoaguiar/icarus");
        File entryPoint = new File(icarusSrcDir, "EntryPoint.java");
        String sourceCode = FileHelper.getText(main.get().toFile());

        File srcEntryPoint = new File(ICARUS_SYSTEM_FLY_DIR, "EntryPoint.template");
        List<String> mainLines = Files.readAllLines(srcEntryPoint.toPath(), StandardCharsets.UTF_8);
        mainLines.add(mainLines.size() - 1, sourceCode);
        Files.write(entryPoint.toPath(), mainLines, StandardCharsets.UTF_8);

        // add other objects
        FileHelper.copyFolder(new File(rootDir), icarusSrcDir, destPath -> {
          if (destPath != null && destPath != main.get()) {
            List<String> lines;

            // write package co.xxx.yyy
            String _package = destPath.toString()
                    .replace(new File(ICARUS_SYSTEM_FLY_DIR, "fly/src/main/java/").toString(), "")
                    .replace("/", ".")
                    .replace(destPath.getFileName().toString(), "");

            _package = _package.substring(1, _package.length() - 1);
            _package = String.format("package %s;", _package);

            try {
              lines = Files.readAllLines(destPath, StandardCharsets.UTF_8);
              lines.add(0, _package);
              Files.write(destPath, lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
              LoggerManager.error(e);
            }

          }
        });

        // clear "main" file's project of system folder
        Files.deleteIfExists(new File(icarusSrcDir, main.get().getFileName().toString()).toPath());
      } else {
        // TODO: 09/04/19 does not have main draw(), shows log
      }
    } catch (IOException e) {
      LoggerManager.error(e);
    }
  }

  private Optional<Path> findMain(String rootDir) {
    try {
      File _rootDir = new File(rootDir);
      return FileHelper.findFileWith(_rootDir, "void draw()");
    } catch (IOException e) {
      LoggerManager.error(e);
      return Optional.empty();
    }
  }

}
