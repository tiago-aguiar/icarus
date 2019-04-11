package co.tiagoaguiar.icarus.devenv.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Abril, 11 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class ZipHelper {

  private static final int BUFFER_SIZE = 4096;

  private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException {
    byte[] buffer = new byte[BUFFER_SIZE];
    File file = new File(outdir, name);
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
    int count = -1;
    while ((count = in.read(buffer)) != -1)
      out.write(buffer, 0, count);
    out.close();

    Set<PosixFilePermission> perms = new HashSet<>();
    perms.add(PosixFilePermission.OWNER_READ);
    perms.add(PosixFilePermission.OWNER_WRITE);
    perms.add(PosixFilePermission.OWNER_EXECUTE);

    Files.setPosixFilePermissions(file.toPath(), perms);
  }

  private static void mkdirs(File outdir, String path) {
    File d = new File(outdir, path);
    if (!d.exists())
      d.mkdirs();
  }

  private static String dirpart(String name) {
    int s = name.lastIndexOf(File.separatorChar);
    return s == -1 ? null : name.substring(0, s);
  }

  public static void extract(File zipfile, File outdir) {
    try {
      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
      ZipEntry entry;
      String name, dir;
      while ((entry = zin.getNextEntry()) != null) {
        name = entry.getName();
        if (entry.isDirectory()) {
          mkdirs(outdir, name);
          continue;
        }
        /* this part is necessary because file entry can come before
         * directory entry where is file located
         * i.e.:
         *   /foo/foo.txt
         *   /foo/
         */
        dir = dirpart(name);
        if (dir != null)
          mkdirs(outdir, dir);

        extractFile(zin, outdir, name);
      }
      zin.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}