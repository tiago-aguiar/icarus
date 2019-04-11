package co.tiagoaguiar.icarus.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * adb shell am broadcast -a co.tiagoaguiar.icarus.RELOAD
 * -n co.tiagoaguiar.icarus/.io.AdbBroadcastReceiver
 * use next line for args
 * --es file "<filename>"
 * Março, 30 2019
 *
 * ./gradlew :fly:dynamicDex
 * adb push app/src/main/assets/dm.dex /sdcard/
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
