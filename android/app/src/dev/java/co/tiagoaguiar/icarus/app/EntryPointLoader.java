package co.tiagoaguiar.icarus.app;

import android.app.Activity;

import co.tiagoaguiar.icarus.EntryPoint;
import co.tiagoaguiar.icarus.io.DynamicEntryPoint;

/**
 * Abril, 11 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class EntryPointLoader {

  static DynamicEntryPoint getEntryPoint(Activity activity, String filename) {
    return new EntryPoint();
  }

}