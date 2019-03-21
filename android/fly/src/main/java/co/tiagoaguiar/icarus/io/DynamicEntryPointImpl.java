package co.tiagoaguiar.icarus.io;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;

import co.tiagoaguiar.icarus.graphics.CanvasManager;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class DynamicEntryPointImpl implements DynamicEntryPoint {

  private CanvasManager canvasManager;

  @Override
  public void setup(Canvas canvas) {
      canvasManager = CanvasManager.newGlobalInstance(canvas);
  }

  @Override
  public void init() {
    Log.i("Teste", "hello init super");
  }

  @Override
  public void draw() {
  }

  @Override
  public void drawBackground() {
    canvasManager.drawBackground();
  }

  public void print(String msg) {
    Log.i("Teste", msg);
  }

}
