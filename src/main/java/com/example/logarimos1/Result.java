package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que representa el resultado de buscar en un árbol. Contiene los puntos
 * de la solución además de la cantidad de accesos a memoria.
 */
public class Result {

  /**
   * Puntos de la solución
   */
  public ArrayList<Pair> points = new ArrayList<>();

  /**
   * Accesos a memoria
   */
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

  /**
   * Método para obtener la lista de puntos solución
   */
  public ArrayList<Pair> getPoints() {
    return points;
  }

  /**
   * Método para obtener el número de accesos a memoria
   */
  public int getAccessCount() {
    return accessCount;
  }
}
