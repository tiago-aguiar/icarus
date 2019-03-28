package co.tiagoaguiar.icarus.devenv.util;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Março, 28 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public final class ShortCut {

  public static void ctrlS(Scene scene, Runnable r) {
    scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), r);
  }

}
