package co.tiagoaguiar.icarus.devenv.ui;

/**
 * Abril, 27 2019
 *
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

public class Splash {

  private final SplashScreen splash;
  private Graphics2D g;

  private void renderSplashFrame(Graphics2D g, String message) {
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(80,100,200,400);
    g.setPaintMode();
    g.setColor(Color.WHITE);
    g.drawString("Loading " + message + "...", 80, 300);
  }

  public Splash() {
    splash = SplashScreen.getSplashScreen();
    if (splash == null) {
      System.out.println("SplashScreen.getSplashScreen() returned null");
    } else {
      g = splash.createGraphics();
      if (g == null) {
        System.out.println("g is null");
      }
    }

  }

  public void render(String message) {
    if (splash != null) {
      renderSplashFrame(g, message);
      splash.update();
      try {
        Thread.sleep(120);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void dispose() {
    if (splash != null) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      splash.close();
    }
  }

}
