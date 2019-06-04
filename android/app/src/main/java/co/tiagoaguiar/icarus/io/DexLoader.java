package co.tiagoaguiar.icarus.io;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DexLoader {

  private final Activity activity;

  public DexLoader(Activity activity) {
    this.activity = activity;
    // TODO: 21/03/19 remove permission from here
  }

  public DynamicEntryPoint load(String name, String cacheDir, String cls) {
    try {
      File dex = copyDexFile(name);
      if (dex == null) {
          InputStream is = activity.getAssets().open("dm.dex");

          File file = FileMemory.getFile(activity, name);
          FileUtils.copyToFile(is, file);

          dex = file;
      }

      DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(),
              cacheDir, null, getClass().getClassLoader());

      Class<?> clazz = dexClassLoader.loadClass(cls);

      if (DynamicEntryPoint.class.isAssignableFrom(clazz)) {
        return (DynamicEntryPoint) clazz.newInstance();
      }
      return null;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private File copyDexFile(String name) {

    try {
      File file = FileMemory.getFile(activity, name);

      String sdCardPath = Environment.getExternalStorageDirectory().getPath();
      File source = new File(sdCardPath + "/" + name);
      FileInputStream is = new FileInputStream(source);

      FileUtils.copyToFile(is, file);
      return file;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
