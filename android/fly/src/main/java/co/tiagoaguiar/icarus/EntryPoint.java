package co.tiagoaguiar.icarus;

import co.tiagoaguiar.icarus.io.DynamicEntryPointImpl;

import static co.tiagoaguiar.icarus.graphics.CanvasManager.color;
import static co.tiagoaguiar.icarus.graphics.CanvasManager.rect;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class EntryPoint extends DynamicEntryPointImpl {

  float left = 0;

  public void draw() {
    color(255); // create paint

    rect(left,left,290,110); // add to list
    left += 1;

 //   mock.setColor(0xFFFF0000);

 //   canvas.drawRect(10, 10, 200, 200, mock);
  }

}
