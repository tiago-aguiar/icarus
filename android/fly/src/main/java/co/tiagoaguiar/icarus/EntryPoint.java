package co.tiagoaguiar.icarus;

import co.tiagoaguiar.icarus.io.DynamicEntryPointImpl;

import static co.tiagoaguiar.icarus.graphics.CanvasManager.*;
/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class EntryPoint extends DynamicEntryPointImpl {

  float left = 0;

  public void init() {
    print("hello");

    background(255);

  }

  public void draw() {
    color(200);
    rect(left,10,290,110);
    left += 1;
  }

}
