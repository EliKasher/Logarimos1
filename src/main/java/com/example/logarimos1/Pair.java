package com.example.logarimos1;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

/**
 * Esta clase representa un punto en el plano, representado por dos coordenadas de tipo double.
 */
public class Pair {
  double x;
  double y;

  public Pair(double xVal, double yVal) {
    x = xVal;
    y = yVal;
  }

  /**
   * Retorna la distancia (un double) etre 2 puntos dados.
   *
   * @param p2 el punto con el cual se debe calcular la distancia.
   */
  public double dist(Pair p2) {
    return sqrt(pow((p2.x - x), 2) + pow((p2.y - y), 2));
  }

  /**
   * @return Devuelve la coordenada x del par.
   */
  public double getX() {
    return x;
  }

  /**
   * @return Devuelve la coordenada y del par.
   */
  public double getY() {
    return y;
  }

  /**
   * @return Devuelve si un par ordenado es igual a otro
   */
  public Boolean equals(Pair p2) {
    if (this.getX() == p2.getX() && this.getY() == p2.getY()) {
      return true;
    }
    return false;
  }

  public String toString() {
    return "(" + x + "," + y + ")\n";
  }
}


