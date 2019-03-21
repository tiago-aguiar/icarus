package co.tiagoaguiar.icarus.graphics;

import android.view.SurfaceHolder;

/**
 * Março, 13 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public interface RendererHolder {

  void onSurfaceCreated(SurfaceHolder holder);

  void onSurfaceChanged(int width, int height);

  void onSurfaceDestroyed(SurfaceHolder holder);

  void onWindowFocusChanged(boolean hasWindowFocus);

}