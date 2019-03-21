package co.tiagoaguiar.icarus.util;

import android.util.Log;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class ILog {

  private static final String TAG = "ILog";

  public static void i(String msg) {
    Log.i(TAG, msg);
  }

  public static void e(Exception e) {
    Log.e(TAG, e.getMessage(), e);
  }

}
