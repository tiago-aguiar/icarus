package co.tiagoaguiar.icarus.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import co.tiagoaguiar.icarus.EntryPoint;
import co.tiagoaguiar.icarus.graphics.RenderThread;
import co.tiagoaguiar.icarus.graphics.RendererHolder;
import co.tiagoaguiar.icarus.io.DynamicEntryPoint;
import co.tiagoaguiar.icarus.system.RenderSystem;
import co.tiagoaguiar.icarus.system.WatchSystem;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Icarus {

  private final Activity activity;

  private RenderThread mRenderThread;
  private LogicThread mLogicThread;

  private RenderSystem mRenderSystem;
  private WatchSystem mWatchSystem;

  public Icarus(Activity activity) {
    this.activity = activity;
  }

  public void onCreate() {
    mRenderSystem = new RenderSystem();
    mRenderSystem.setEntryPoint(getEntryPoint());

    if (mWatchSystem == null )
      mWatchSystem = new WatchSystem(activity, mRenderSystem);

    if (mLogicThread == null)
      mLogicThread = new LogicThread(mRenderSystem);

    if (mRenderThread == null)
      mRenderThread = new RenderThread(mRenderSystem);

//    mWatchSystem.setFileNameToWatch("dm.dex");
//    mWatchSystem.setHandler(new Handler(Looper.getMainLooper()) {
//      @Override
//      public void handleMessage(Message msg) {
//        Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
//      }
//    });
  }

  public void onStart() {
    mLogicThread.start();
    mRenderThread.start();
    mWatchSystem.start();
  }

  public void onReload() {
    mRenderSystem.setEntryPoint(getEntryPoint());
  }

  public void onStop() {
    if (mRenderThread != null)
      mRenderThread.stop();

    if (mLogicThread != null)
      mLogicThread.stop();
  }

  public RendererHolder getRendererHolder() {
    return mRenderThread;
  }

  private DynamicEntryPoint getEntryPoint() {
    return new EntryPoint();
//    DexLoader dexLoader = new DexLoader(activity);
//    DynamicEntryPoint entryPoint = dexLoader.load("dm.dex",
//            activity.getCacheDir().getAbsolutePath(), "co.tiagoaguiar.icarus.EntryPoint");
//    return entryPoint;
  }

}
