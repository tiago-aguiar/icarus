package co.tiagoaguiar.icarus.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

import co.tiagoaguiar.icarus.util.ILog;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class RenderThread implements Runnable, RendererHolder {

  private final Renderer mRenderer;
  private Thread mThread;
  private SurfaceHolder mHolder;

  private boolean mRunning;
  private boolean mSurfaceChanged;

  private int mDeviceWidth;
  private int mDeviceHeight;

  private boolean mHasWindowFocus;
  private boolean mHasSurface;

  public RenderThread(RenderThread.Renderer renderer) {
    mRenderer = renderer;
  }

  public void start() {
    if (!mRunning) {
      mThread = new Thread(this, "RenderThread");
      mThread.start();
      mRunning = true;
    }
  }

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

  // TODO: 20/03/19 sync
  @Override
  public void onSurfaceCreated(SurfaceHolder holder) { // from mainThread, sync variables
    mHolder = holder;
    mHasSurface = true;
  }

  // TODO: 20/03/19 sync
  @Override
  public void onSurfaceChanged(int width, int height) { // from mainThread, sync variables
    mDeviceWidth = width;
    mDeviceHeight = height;
    mSurfaceChanged = true;
  }

  // TODO: 20/03/19 sync
  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) { // from mainThread, sync variables
    mHasWindowFocus = hasWindowFocus;
  }

  // TODO: 20/03/19 sync
  @Override
  public void onSurfaceDestroyed(SurfaceHolder holder) { // from mainThread, sync variables

  }

  @Override
  public void run() {
    ILog.i("starts run(): " + mThread.getName());
    while (mRunning) {
      // TODO: 20/03/19 synchronize a ThreadManager in while(true) for waiting surface has been presented


      if (mSurfaceChanged) {
        mRenderer.onSurfaceChanged(mDeviceWidth, mDeviceHeight);
        mSurfaceChanged = false;
      }


      if (mHasWindowFocus) {
        if (mHasSurface) {

          Canvas canvas = mHolder.lockCanvas();

          mRenderer.onDraw(canvas);

          mHolder.unlockCanvasAndPost(canvas);
        }

        mRenderer.endDraw();
      }


    }
  }

  public interface Renderer {

    void onSurfaceChanged(int width, int height);

    void onDraw(Canvas canvas);

    void endDraw();

  }

}
