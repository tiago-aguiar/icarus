package co.tiagoaguiar.icarus.system;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import co.tiagoaguiar.icarus.EntryPoint;
import co.tiagoaguiar.icarus.util.ILog;

/**
 * Março, 30 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class WatchSystem implements Runnable {

  private static final String TAG = "WatchSystem";

  private final RenderSystem mRenderSystem;
  private final Context context;

  private Handler handler;
  private Thread mThread;

  private boolean mRunning;
  private Handler threadHandler;

  public WatchSystem(Context context, RenderSystem renderSystem) {
    this.context = context;
    this.mRenderSystem = renderSystem;
  }

  public void start() {
    if (!mRunning) {
      mThread = new Thread(this, "WatchThread");
      mThread.start();
      mRunning = true;
    }
  }

  // TODO: 30/03/19  
  public void stop() {
    if (mRunning) {
      try {
        mThread.join();
      } catch (InterruptedException e) {
        ILog.e(e);
      }
      mRunning = false;
    }
  }

  @SuppressLint("HandlerLeak")
  @Override
  public void run() {
    Looper.prepare();

    threadHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
//      File file = FileMemory.getFile(context, fileName);
        mRenderSystem.setEntryPoint(new EntryPoint());
      }
    };

    Looper.loop();
  }

}
