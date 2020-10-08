package reaction;

import graphicsLib.G;
import graphicsLib.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import music.UC;

public class ShapeTrainer extends Window {
  public static String UNKNOWN = "<--Unknown shape name.";
  public static String ILLEGAL = "<--Illegal shape name.";
  public static String KNOWN = "<--Known shape name.";
  public static String currName = "";
  public static String currState = ILLEGAL;


  public ShapeTrainer() {
    super("ShapeTrainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
  }

  public void paintComponent(Graphics g) {
    G.fillBackground(g);
    g.setColor(Color.BLACK);
    g.drawString(currName, 600, 30);
    g.drawString(currState, 700, 30);
  }

  public void setState() {
    currState = (currName.equals("") || currName.equals("DOT")) ? ILLEGAL : UNKNOWN;
  }

  public void keyTyped(KeyEvent ke) {
    char c = ke.getKeyChar();
    System.out.println("Typed: " + c);
    // ' ' == space, 0x0D == \r enter, 0x0A == \n newline character
    currName = (c == ' ' || c == 0x0D || c == 0x0A ) ? "" : currName + c;
    setState();
    repaint();
  }
}
