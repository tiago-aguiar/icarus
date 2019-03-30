package co.tiagoaguiar.icarus.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * adb shell am broadcast -a co.tiagoaguiar.icarus.RELOAD
 * -n co.tiagoaguiar.icarus/.io.AdbBroadcastReceiver
 * <p>
 * use next line for args
 * --es url "hello\ world"
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AdbBroadcastReceiver extends BroadcastReceiver {

  public static final String ADB_ACTION = "AdbBroadCast";
  public static final String ADB_FILE = "AdbBroadCastFile";

  @Override
  public void onReceive(Context context, Intent intent) {
    Intent i = new Intent(ADB_ACTION);
    i.putExtra(ADB_FILE, intent.getExtras().getString("file"));
    context.sendBroadcast(i);
  }

}
