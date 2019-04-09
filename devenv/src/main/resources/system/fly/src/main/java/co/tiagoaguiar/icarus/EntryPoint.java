package co.tiagoaguiar.icarus;

import co.tiagoaguiar.icarus.io.DynamicEntryPointImpl;

import static co.tiagoaguiar.icarus.graphics.CanvasManager.*;

/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class EntryPoint extends DynamicEntryPointImpl {

  private float left = 0;

  public void draw() {
    background(0, 0, 0);

    color(255, 0, 255);
    rect(0, left, 50, 100);

    left += 5;
  }

}
