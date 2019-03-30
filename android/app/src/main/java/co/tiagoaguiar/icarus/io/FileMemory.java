package co.tiagoaguiar.icarus.io;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
final class FileMemory {

  static File getFile(Context context, String filename) {
    File file = null;
    try {
      String sourceFile = context.getPackageManager().getPackageInfo(context.getPackageName(),
              0).applicationInfo.dataDir + "/files/" + filename;

      file = new File(sourceFile);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    return file;
  }

}
