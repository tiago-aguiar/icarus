package co.tiagoaguiar.icarus.core;

import co.tiagoaguiar.icarus.graphics.RenderThread;
import co.tiagoaguiar.icarus.graphics.RendererHolder;
import co.tiagoaguiar.icarus.system.RenderSystem;

/**
 * Março, 20 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class Icarus {

  private RenderThread mRenderThread;
  private LogicThread mLogicThread;

  private RenderSystem mRenderSystem;

  public void onCreate() {
    mRenderSystem = new RenderSystem();

    if (mLogicThread == null)
      mLogicThread = new LogicThread(mRenderSystem);

    if (mRenderThread == null)
      mRenderThread = new RenderThread(mRenderSystem);
  }

  public void onStart() {
      mLogicThread.start();
      mRenderThread.start();
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

}
