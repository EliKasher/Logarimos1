package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que conforma el método Sexton-Swinbank
 */
public class SS {
  //Hay que definir bien estos valores
  double B = 2;
  double b = 0.5*B;
  /**
   * Función que crea clusters a partir de una colección de puntos.
   * @param cIn Colección de puntos
   * @return
   */
  public ArrayList<Cluster> cluster(ArrayList<Pair> cIn) {
    ArrayList<Cluster> cOut = new ArrayList<>();
    ArrayList<Cluster> c = new ArrayList<>();

    // Por cada par de cIn
    for (Pair actualPoint : cIn) {
      Cluster singleton = new Cluster();
      singleton.addElement(actualPoint);
      c.add(singleton);
    }

    // Hasta que quede un elemento
    while (c.size() > 1) {
      // define c1 c2
      // Pair nearestPair = Pair(c1,c2);
      Cluster c1c2 = c1.join(c2);
      c.remove(c1);  // Hay que probar si esta linea funciona adecuadamente :0

      if(c1c2.size() <= B) {
        c.remove(c2);
        c.add(c1c2);
      } else {
        cOut.add(c1);
      }
    }

    Cluster lastElement = c.get(0);
    Cluster cPrima = new Cluster();

    if(cOut.size() > 0) {
      cPrima = lastElement.nearest(cOut);
      cOut.remove(cPrima);
    }

    Cluster cLcP = lastElement.join(cPrima);
    if(cLcP.size() <= B) {
      cOut.add(cLcP);
    } else {
      ArrayList<Cluster> minMaxClusters = cLcP.minMaxSplit();
      cOut.add(minMaxClusters.get(0));
      cOut.add(minMaxClusters.get(1));
    }

    return cOut;
  }

  /**
   *
   */
  public void outputHoja() {

  }

  /**
   *
   */
  public void outputInterno() {

  }
}
