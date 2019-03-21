package co.tiagoaguiar.icarus.graphics;


/**
 * Março, 21 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
public class ShapeLibrary {

  private static final int DEFAULT_SIZE = 512;

  Shape[] shapeHash;

  public ShapeLibrary() {
    shapeHash = new Shape[DEFAULT_SIZE];
    for (int i = 0; i < shapeHash.length; i++) {
      shapeHash[i] = new Shape();
    }
  }



}
