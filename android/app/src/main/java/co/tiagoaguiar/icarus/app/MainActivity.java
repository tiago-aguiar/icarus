package co.tiagoaguiar.icarus.app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import co.tiagoaguiar.icarus.graphics.MainSurfaceView;
import co.tiagoaguiar.icarus.io.AdbBroadcastReceiver;
import co.tiagoaguiar.icarus.util.ILog;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.widget.FrameLayout.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

  private ProgressBar progressView;
  private Icarus icarus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        return;
      }
    }

    onPermissionGranted();
  }

  private void onPermissionGranted() {
    icarus = new Icarus(this);
    icarus.onCreate();

    FrameLayout frameLayout = new FrameLayout(this);
    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));


    MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
    mainSurfaceView.setRendererHolder(icarus.getRendererHolder());

    frameLayout.addView(mainSurfaceView);

    setContentView(frameLayout);

    registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        ILog.i("code onReload");

        progressView = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.topMargin = -16;
        progressView.setLayoutParams(layoutParams);
        progressView.setIndeterminate(true);

        // TODO: 30/03/19 remove this???
        frameLayout.addView(progressView);

        icarus.onReload(intent.getExtras().getString(AdbBroadcastReceiver.ADB_FILE));

        new Handler().postDelayed(() -> frameLayout.removeView(progressView), 500);
      }
    }, new IntentFilter(AdbBroadcastReceiver.ADB_ACTION));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted();
        icarus.onStart();
      } else {
        finish();
      }
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (icarus != null)
      icarus.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (icarus != null)
      icarus.onStop();
  }

}