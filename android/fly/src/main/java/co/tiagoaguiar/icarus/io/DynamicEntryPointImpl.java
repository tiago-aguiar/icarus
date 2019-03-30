package co.tiagoaguiar.icarus.io;

import android.graphics.Canvas;
import android.util.Log;

import co.tiagoaguiar.icarus.graphics.CanvasManager;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DynamicEntryPointImpl implements DynamicEntryPoint {

  @Override
  public void setup(Canvas canvas) {
      CanvasManager.newGlobalInstance(canvas);
  }

  @Override
  public void setSize(int width, int height) {
    CanvasManager.width = width;
    CanvasManager.height = height;
  }

  @Override
  public void tearDown() {
  }

  @Override
  public void init() {
    Log.i("Teste", "hello init super");
  }

  @Override
  public void draw() {
  }

  public void print(String msg) {
    Log.i("Teste", msg);
  }

}
