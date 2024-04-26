package com.example.logarimos1;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

public class Pair {
  double x;
  double y;

  public Pair(double xVal, double yVal){
    x = xVal;
    y = yVal;
  }

  /** Retorna la distancia (un double) de este punto a otro */
  public double dist(Pair p2){
    return sqrt(pow((p2.x - x), 2)+pow((p2.y - y), 2));
  }
}
