package co.tiagoaguiar.icarus;

import co.tiagoaguiar.icarus.core.graphics.CanvasManager;
import co.tiagoaguiar.icarus.io.DynamicEntryPointImpl;

import static co.tiagoaguiar.icarus.core.graphics.CanvasManager.color;


/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class EntryPoint extends DynamicEntryPointImpl {

  @Override
  public void draw() {

    CanvasManager.background(0,0,0);

    CanvasManager.color(11,189,109);
    CanvasManager.rect(0, 0, 100, 100);
  }
}