package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import reaction.Ink;
import reaction.Ink.List;
import music.UC;
import reaction.Shape;

public class PaintInk extends Window {

  public static Ink.List inkList = new List();
  public static Shape.Prototype.List pList = new Shape.Prototype.List();

  public PaintInk() {
    super("PaintInk", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
  }

  @Override
  public void paintComponent(Graphics g) {
    G.fillBackground(g);
    inkList.show(g);
    Ink.BUFFER.show(g);
    if (inkList.size() > 1) {
      int last = inkList.size() - 1;
      int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
      g.setColor(dist > UC.NO_MATCH_DIST ? Color.RED : Color.BLACK);
      g.drawString("Dist: " + dist, 600, 60);
    }
    pList.show(g);
  }

  public void mousePressed(MouseEvent me) { Ink.BUFFER.dn(me.getX(), me.getY()); repaint(); }

  public void mouseDragged(MouseEvent me) { Ink.BUFFER.drag(me.getX(), me.getY()); repaint(); }

  public void mouseReleased(MouseEvent me) {
    Ink ink = new Ink();
    Shape.Prototype proto;
    inkList.add(ink);
    if (pList.bestDist(ink.norm) < UC.NO_MATCH_DIST) {
      proto = pList.bestMatch;
      proto.blend(ink.norm);
    } else {
      proto = new Shape.Prototype();
      pList.add(proto);
    }
    ink.norm = proto;
    repaint();
  }


}
