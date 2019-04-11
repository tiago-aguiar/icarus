package co.tiagoaguiar.icarus;

import co.tiagoaguiar.icarus.core.graphics.CanvasManager;
import co.tiagoaguiar.icarus.io.DynamicEntryPointImpl;


/**
 * Março, 19 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class EntryPoint extends DynamicEntryPointImpl {

  @Override
  public void draw() {
    CanvasManager.background(255,0,0);
  }
}