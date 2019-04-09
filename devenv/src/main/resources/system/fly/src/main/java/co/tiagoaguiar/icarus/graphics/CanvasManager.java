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

  public static int width;
  public static int height;

  private CanvasManager(Canvas canvas) {
    this.canvas = canvas;
    this.currentPaint = new Paint();
  }

  public static CanvasManager newGlobalInstance(Canvas canvas) {
    INSTANCE = new CanvasManager(canvas);
    return INSTANCE;
  }

  public static void background(int r, int g, int b) {
    background(r, g, b, PorterDuff.Mode.CLEAR);
  }

  public static void background(int r, int g, int b, PorterDuff.Mode mode) {
    INSTANCE.canvas.drawColor(Color.GREEN, mode);
    INSTANCE.canvas.drawColor(Color.argb(255, r, g, b));
  }

  public static void color(int r, int g, int b) {
    INSTANCE.currentPaint.setColor(Color.argb(255, r, g, b));
  }

  public static void rect(float x, float y, float width, float height) {
    INSTANCE.canvas.drawRect(x, y, x + width, y + height, INSTANCE.currentPaint);
  }

}