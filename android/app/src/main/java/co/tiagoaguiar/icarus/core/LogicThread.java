package co.tiagoaguiar.icarus.core;

import co.tiagoaguiar.icarus.system.RenderSystem;
import co.tiagoaguiar.icarus.util.ILog;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class LogicThread implements Runnable {

  private final RenderSystem mRenderSystem;
  private Thread mThread;

  private boolean mRunning = false;

  LogicThread(RenderSystem renderSystem) {
    mRenderSystem = renderSystem;
  }

  void start() {
    if (!mRunning) {
      mThread = new Thread(this, "LogicThread");
      mThread.start();
      mRunning = true;
    }
  }

  void stop() {
    if (mRunning) {
      try {
        mThread.join();
      } catch (InterruptedException e) {
        ILog.e(e);
      }
      mRunning = false;
    }
  }

  @Override
  public void run() {
    ILog.i("running: " + mThread.getName());

    while (mRunning) {

      // TODO: 21/03/19 objectManager.update()
      // TODO: 21/03/19 objectManager.draw()
      mRenderSystem.swap();

      // TODO: 09/04/19 FPS
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }


  }

}
