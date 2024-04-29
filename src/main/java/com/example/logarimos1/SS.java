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
      Cluster c1 = null;
      Cluster c2 = null;
      double actualDist = Double.MAX_VALUE;;

      for (int i=0; i<c.size(); i++) {
        Cluster actualSingleton = c.get(i);
        Cluster nearest = actualSingleton.nearest(c);

        if (actualSingleton.getG().dist(nearest.getG()) < actualDist) {
          actualDist = actualSingleton.getG().dist(nearest.getG());
          // Orden debe ser |c1| >= |c2|
          if (actualSingleton.size() >= nearest.size()) {
            c1 = actualSingleton;
            c2 = nearest;
          } else {
            c1 = nearest;
            c2 = actualSingleton;
          }
        }
      }

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

  
  public InternalNode outputInterno(ExternalNode cMra) {
    //Arreglo que contiene los medoides de cada tupla en cMra
    ArrayList<Pair> cIn = new ArrayList<>();
    //Radio cobertor del nodo interno
    double R = 0;
    //Nodo interno c
    ArrayList<Pair> c = new ArrayList<>();
    //Iteramos para armar el conjunto
    for (int i=0; i<cMra.size(); i++) {
      Cluster cluster = cMra.get(i);
      Pair g = cluster.g;
      cIn.add(g);
      c.add(cluster);
    }
    //Medoide primario de cIn, hay que ver cómo resolverlo
    Pair G = cIn.gPrimario;
    //Creamos el InternalNode (G,R,A)
    InternalNode output = new InternalNode();
    output.g = G;
    output.r = R;
    //Como A es una dirección al conjunto de elementos, lo seteamos así
    output.A = c;
    return output;
  }
}
