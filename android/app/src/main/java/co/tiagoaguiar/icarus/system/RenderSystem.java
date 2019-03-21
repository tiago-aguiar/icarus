package co.tiagoaguiar.icarus.system;

import android.graphics.Canvas;
import android.graphics.Paint;

import co.tiagoaguiar.icarus.graphics.RenderThread;
import co.tiagoaguiar.icarus.util.ILog;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class RenderSystem implements RenderThread.Renderer {

  private Object drawLock = new Object();
  private boolean drawQueueChanged;

  Paint mock = new Paint();

  @Override
  public void onSurfaceChanged(int width, int height) {
    ILog.i("RenderSystem: onSurface Changed :: w" + width + " x " + "h" + height);
  }

  public void swap() {
    synchronized (drawLock) {
       drawQueueChanged = true;
       drawLock.notify();
    }
  }

  @Override
  public void onDraw(Canvas canvas) {


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

    // TODO: 21/03/19 draw stuff
    canvas.drawColor(0xFFFF00FF);
    mock.setColor(0xFFFF0000);

    canvas.drawRect(10, 10, 200, 200, mock);

  }

}
