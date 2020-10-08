package reaction;

import graphicsLib.G;
import java.awt.Graphics;
import java.util.ArrayList;
import music.I.Area;
import music.I.Show;
import music.UC;

public class Ink implements Show {

  public static Buffer BUFFER = new Buffer(); // Singleton design pattern
  public static final int K = UC.NORM_SAMPLE_SIZE;
  public static G.VS TEMP = new G.VS(100, 100, 100, 100);

  public Norm norm;
  public G.VS vs;

  public Ink() {
    norm = new Norm();
    vs = BUFFER.bbox.getNewVS();
  }

  @Override
  public void show(Graphics g) {
    g.setColor(UC.INK_COLOR);
    norm.drawAt(g, vs);
  }

  //----------------------------------Norm------------------------------------//
  public static class Norm extends G.PL {
    public static final int K = UC.NORM_SAMPLE_SIZE, MAX = UC.NORM_COORD_MAX;
    public static final G.VS normBox = new G.VS(0, 0, MAX, MAX);

    public Norm() {
      super(K);
      for (int i = 0; i < K; i++) { points[i].set(BUFFER.points[i*(BUFFER.n - 1) / (K -1)]); }
      G.V.T.set(BUFFER.bbox, normBox);
      this.transform();
    }

    public void drawAt(Graphics g, G.VS vs) {
      G.V.T.set(normBox, vs);
      for (int i = 1; i < K; i++) {
        g.drawLine(points[i - 1].tx(), points[i - 1].ty(), points[i].tx(), points[i].ty());
      }
    }

    public int dist(Norm norm) {
      int result = 0;
      for (int i = 0; i < K; i++) {
        int dx = points[i].x - norm.points[i].x;
        int dy = points[i].y - norm.points[i].y;
        result += dx*dx + dy*dy;
      }
      return result;
    }

    public void blend(Norm norm, int nBlend) {
      for (int i = 0; i < K; i++) {
        points[i].blend(norm.points[i], nBlend);
      }
    }
  }

  //----------------------------------List------------------------------------//
  public static class List extends ArrayList<Ink> implements Show {

    @Override
    public void show(Graphics g) { for (Ink ink: this) { ink.show(g); } }

  }

  //---------------------------------Buffer-------------------------------------//
  public static class Buffer extends G.PL implements Show, Area {

    public int n; // Number of points in the buffer
    public G.BBox bbox = new G.BBox();
    public static final int MAX = UC.INK_BUFFER_MAX;

    private Buffer() { super(MAX); }

    public void add(int x, int y) { if (n < MAX) { points[n++].set(x, y); bbox.add(x, y); } }

    public void clear() { n = 0; }

    @Override
    public boolean hit(int x, int y) { return true; }

    @Override
    public void dn(int x, int y) { clear(); bbox.set(x, y); add(x, y); }

    @Override
    public void drag(int x, int y) { add(x, y); }

    @Override
    public void up(int x, int y) {}

    @Override
    public void show(Graphics g) { drawN(g, n); bbox.draw(g); }
  }
}
