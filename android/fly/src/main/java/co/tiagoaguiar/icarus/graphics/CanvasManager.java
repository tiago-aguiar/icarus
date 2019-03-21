package co.tiagoaguiar.icarus.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class CanvasManager {

  private static CanvasManager INSTANCE;

  private Canvas canvas;
  private Paint currentPaint;

  public static int backgroundColor;


  public static void background(int color) {
    backgroundColor = Color.argb(255, color, color, color);
  }

  public static void color(int color) {
    INSTANCE.currentPaint.setColor(Color.argb(255, color, color, color));
  }

  public static void rect(float left, float top, float width, float height) {
    INSTANCE.canvas.drawRect(left, top, left + width, top + height, INSTANCE.currentPaint);
  }

  public static CanvasManager newGlobalInstance(Canvas canvas) {
    INSTANCE = new CanvasManager(canvas);
    return INSTANCE;
  }

  public void drawBackground() {
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    canvas.drawColor(backgroundColor);
  }

  private CanvasManager(Canvas canvas) {
    this.canvas = canvas;
    this.currentPaint = new Paint();
  }

}