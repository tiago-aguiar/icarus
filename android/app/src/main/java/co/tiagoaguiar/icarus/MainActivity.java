package co.tiagoaguiar.icarus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import co.tiagoaguiar.icarus.core.Icarus;
import co.tiagoaguiar.icarus.graphics.MainSurfaceView;
import co.tiagoaguiar.icarus.io.DexLoader;
import co.tiagoaguiar.icarus.io.DynamicEntryPoint;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
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

    FrameLayout frameLayout = new FrameLayout(this);
    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

    MainSurfaceView mainSurfaceView = new MainSurfaceView(this);
    mainSurfaceView.setRendererHolder(icarus.getRendererHolder());
    frameLayout.addView(mainSurfaceView);

    Button button = new Button(this);
    button.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
    button.setText("Teste");
    frameLayout.addView(button);
    
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
        icarus.onReload();
      }
    });

    setContentView(frameLayout);

//    final DynamicEntryPoint entryPoint = test();
//
//
//
//
//    final RendererImpl renderer = new RendererImpl(entryPoint);
//    UIWindow window = new UIWindow(this, renderer);
//    setContentView(window);





//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        DynamicEntryPoint entryPoint1 = test();
//        renderer.reload(entryPoint1);
//      }
//    }, 5000);
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

  // TODO: 20/03/19 lyfecycle

  private DynamicEntryPoint test() {
    DexLoader dexLoader = new DexLoader(this);
    Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
    DynamicEntryPoint entryPoint = dexLoader.load("dm.dex", getCacheDir().getAbsolutePath(),
            "co.tiagoaguiar.icarus.EntryPoint");
    return entryPoint;
  }

}