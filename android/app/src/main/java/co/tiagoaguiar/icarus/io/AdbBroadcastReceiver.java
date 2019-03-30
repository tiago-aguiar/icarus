package co.tiagoaguiar.icarus.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class AdbBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String url = intent.getExtras().getString("url");
    Bundle extras = intent.getExtras();
    Intent i = new Intent("broadCastName");
    // Data you need to pass to activity
    i.putExtra("message", url);

    context.sendBroadcast(i);
  }

}
