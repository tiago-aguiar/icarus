package co.tiagoaguiar.icarus.app;

import android.app.Activity;

import co.tiagoaguiar.icarus.graphics.RenderThread;
import co.tiagoaguiar.icarus.graphics.RendererHolder;
import co.tiagoaguiar.icarus.system.RenderSystem;

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

  public Icarus(Activity activity) {
    this.activity = activity;
  }

  public void onCreate() {
    mRenderSystem = new RenderSystem();
    mRenderSystem.setEntryPoint(EntryPointLoader.getEntryPoint(activity, "dm.dex"));

    if (mLogicThread == null)
      mLogicThread = new LogicThread(mRenderSystem);

    if (mRenderThread == null)
      mRenderThread = new RenderThread(mRenderSystem);
  }

  public void onStart() {
    mLogicThread.start();
    mRenderThread.start();
  }

  public void onReload(String filename) {
    mRenderSystem.setEntryPoint(EntryPointLoader.getEntryPoint(activity, filename));
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
