package co.tiagoaguiar.icarus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import co.tiagoaguiar.icarus.core.Icarus;
import co.tiagoaguiar.icarus.graphics.MainSurfaceView;
import co.tiagoaguiar.icarus.util.ILog;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {

  private Icarus icarus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

    icarus = new Icarus(this);
    icarus.onCreate();

    MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
    mainSurfaceView.setRendererHolder(icarus.getRendererHolder());
    setContentView(mainSurfaceView);

    registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));

  }

  @Override
  protected void onStart() {
    super.onStart();
    icarus.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    icarus.onStop();
  }

  // Add this inside your class
  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // TODO: 30/03/19 adb shell am broadcast -a co.tiagoaguiar.icarus.RELOAD --es url "hello\ world" -n co.tiagoaguiar.icarus/.io.AdbBroadcastReceiver
      Bundle b = intent.getExtras();
      String message = b.getString("message");
      ILog.i(message);
      icarus.onReload();
    }
  };


}