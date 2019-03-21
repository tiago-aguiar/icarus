package co.tiagoaguiar.icarus.graphics;

import android.graphics.Rect;

/**
 * Março, 21 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class RectWrapper {

  public static void rect(int x, int y, int width, int height) {
    Rect rect = new Rect(x, y, x + width, y + height);

  }

}
