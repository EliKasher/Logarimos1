package com.example.logarimos1;

import java.util.ArrayList;

public class Result {
  public ArrayList<Pair> points = new ArrayList<>();
  public int accessCount = 0;

  /**
   * Método para añadir un punto al resultado
   * @param p el punto a agregar
   */
  public void addPoint(Pair p) {
    points.add(p);
  }

  /**
   * Se suma 1 acceso a memoria.
   */
  public void memoryAccess(int k) {
    accessCount += k;
  }

  public ArrayList<Pair> getPoints() {
    return points;
  }

  public int getAccessCount() {
    return accessCount;
  }
}
