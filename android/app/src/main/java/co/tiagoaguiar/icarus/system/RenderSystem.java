package co.tiagoaguiar.icarus.system;

import android.graphics.Canvas;

import co.tiagoaguiar.icarus.graphics.RenderThread;
import co.tiagoaguiar.icarus.io.DynamicEntryPoint;
import co.tiagoaguiar.icarus.util.ILog;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class RenderSystem implements RenderThread.Renderer {

  private final Object entryLock = new Object();
  private final Object drawLock = new Object();

  private DynamicEntryPoint entryPoint;

  private boolean drawQueueChanged;
  private boolean drawing;

  @Override
  public void onSurfaceChanged(int width, int height) {
    ILog.i("RenderSystem: onSurface Changed :: w" + width + " x " + "h" + height);
    synchronized (entryLock) {
      if (entryPoint != null) {
        entryPoint.setSize(width, height);
      }
      entryLock.notify();
    }
  }

  public void swap() { // Logic Thread
    synchronized (drawLock) {
       drawQueueChanged = true;
       drawLock.notify();
    }
  }

  public void setEntryPoint(DynamicEntryPoint entryPoint) {
    synchronized (entryLock) {
      while (drawing) {
        try {
          entryLock.wait();
        } catch (InterruptedException e) {
          ILog.e(e);
        }
      }
      this.entryPoint = entryPoint;
    }
  }

  @Override
  public void onDraw(Canvas canvas) { // Render Thread
    synchronized (drawLock) {
      while (!drawQueueChanged) {
        try {
          drawLock.wait();
        } catch (InterruptedException e) {
          ILog.e(e);
        }
      }
      drawQueueChanged = false;
    }

    drawing = true;

    // TODO: 21/03/19 draw stuff
    synchronized (entryLock) {
      entryPoint.setup(canvas);
      entryPoint.draw();
      entryLock.notify();
    }
  }

  @Override
  public void endDraw() {
    synchronized (entryLock) {
      entryPoint.tearDown();
      drawing = false;
      entryLock.notify();
    }
  }

}
