package co.tiagoaguiar.icarus.graphics;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Março, 13 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

  private RendererHolder rendererHolder;

  public MainSurfaceView(Context context) {
    super(context);

    getHolder().setFormat(PixelFormat.TRANSLUCENT);
    getHolder().addCallback(this);
  }

  public void setRendererHolder(RendererHolder rendererHolder) {
    this.rendererHolder = rendererHolder;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (rendererHolder == null)
      throw new IllegalArgumentException("you must call setRendererHolder()");
    rendererHolder.onSurfaceCreated(holder);
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (rendererHolder == null)
      throw new IllegalArgumentException("you must call setRendererHolder()");
    rendererHolder.onSurfaceChanged(width, height);
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    if (rendererHolder == null)
      throw new IllegalArgumentException("you must call setRendererHolder()");
    rendererHolder.onSurfaceDestroyed(holder);
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);
    if (rendererHolder == null)
      throw new IllegalArgumentException("you must call setRendererHolder()");
    rendererHolder.onWindowFocusChanged(hasWindowFocus);
  }

}