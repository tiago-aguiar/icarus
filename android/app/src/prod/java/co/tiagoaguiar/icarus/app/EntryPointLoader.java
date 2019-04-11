package co.tiagoaguiar.icarus.app;

import android.app.Activity;

import co.tiagoaguiar.icarus.io.DexLoader;
import co.tiagoaguiar.icarus.io.DynamicEntryPoint;

/**
 * Abril, 11 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class EntryPointLoader {

  static DynamicEntryPoint getEntryPoint(Activity activity, String filename) {
    DexLoader dexLoader = new DexLoader(activity);
    return dexLoader.load(filename,
            activity.getCacheDir().getAbsolutePath(),
            "co.tiagoaguiar.icarus.EntryPoint");
  }

}
