package co.tiagoaguiar.icarus.io;

import android.graphics.Canvas;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public interface DynamicEntryPoint {

  void setup(Canvas canvas);

  void init();

  void draw();

  void drawBackground();

}
