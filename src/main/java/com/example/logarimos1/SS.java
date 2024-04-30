package com.example.logarimos1;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Clase que conforma el método Sexton-Swinbank
 */
public class SS {
  //Hay que definir bien estos valores
  double B = 2;
  double b = 0.5*B;

  public SS(double tamanoB) {
    this.B = tamanoB;
    this.b = 0.5*tamanoB;
  }

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
   * Conforma un nodo hoja en base a un conjunto de clusters
   * @return Retorna el nodo hoja conformado
   */
  public Node outputHoja(ArrayList<Cluster> cIn) {
    double r = 0;
    Pair g = null;
    ArrayList<Node> c = null;

    for (Cluster cluster : cIn) {
      if (g == null) {
        g = cluster.getG();
      }

      Node extNode = new Node();
      extNode.setG(cluster.getG());
      extNode.setA(null);
      extNode.setR(0);
      c.add(extNode);

      r = Math.max(r, g.dist(cluster.getG()));
    }

    Node result = new Node();
    result.setG(g);
    result.setR(r);
    result.setA(c);
    return result;
  }

  /**
   * En base a una lista de nodos externos, conforma un conjunto de nodos internos
   * @return Un conjunto de nodos internos
   */
  public Node outputInterno(ArrayList<Node> cMra) {
    //Se define un cluster para generar un medoide G a partir de cMra
    Cluster cIn = new Cluster();
    //Agregamos cada elemento para ir armando el cluster
    for (Node node : cMra) {
      Pair g = node.getG();
      cIn.addElement(g);
    }
    //Extraemos el medoide del cluster
    Pair G = cIn.getG();
    //Definimos R
    double R = 0;
    //Definimos el conjunto C
    ArrayList<Node> c = new ArrayList<Node>();
    //Agregamos todos los (g,r,a) en cMra
    for (Node node : cMra) {
      c.add(node);
      //Definimos g y r para setear R
      Pair g = node.getG();
      double r = node.getR();
      //Seteamos R
      R = Math.max(R, g.dist(G) + r);
    }

    Node result = new Node();
    result.setG(G);
    result.setR(R);
    result.setA(c);
    return result;
  }

  /**
   * Aplica el algoritmo SS sobre un conjunto de puntos.
   * @returns un M-Tree construido
   */
  public ArrayList<Node> ss(ArrayList<Pair> cIn) {
    ArrayList<Cluster> cOut = cluster(cIn);

    // Caso Base con cIn <= B -> Simplemente se forma un Nodo Hoja para todo (Aún así debe pasar por la función Cluster).
    if (cIn.size() <=B) {
      Node result = outputHoja(cOut);
      return result.getA();

    } else {
      //Conjunto para hacer el return
      ArrayList<Node> newC = new ArrayList<Node>();

      ArrayList<Node> c = new ArrayList<Node>();

      //Se agregan los elementos de cOut (clusters) pasados por OutputHoja al conjunto C
      for (Cluster cluster : cOut) {
        ArrayList<Cluster> h = new ArrayList<Cluster>();
        h.add(cluster);
        c.add(outputHoja(h));
      }
      //Mientras |C| > B
      while(c.size()>B){
        //Armamos el conjunto cIn2, que contiene los medoides (puntos) de los clusters de c
        ArrayList<Pair> cIn2 = new ArrayList<>();

        for (Node nodo: c) {
          cIn2.add(nodo.getG());
        }
        //Luego creamos el conjunto de clusters cOut2, en base al cIn2
        ArrayList<Cluster> cOut2 = cluster(cIn2);

        //Conjunto de listas de nodos Cmra
        ArrayList<ArrayList<Node>> cMra = new ArrayList<ArrayList<Node>>();

        //Por cada cluster e en cOut2
        for (Cluster cluster : cOut2) {
          Cluster e = cluster;

          //Rescatamos los elementos del cluster e
          ArrayList<Pair> eElements = e.getElements();

          //Definimos el conjunto s como un nodo de c tal que su medoide pertenece al cluster e
          ArrayList<Node> s = new ArrayList<Node>();

          //Iteramos sobre los elementos de c para encontrar los nodos que satisfagan la condición de s
          for (Node nodo : c) {
            if (eElements.contains(nodo.getG())) {
              s.add(nodo);
            }
          }
          //Luego añadimos la lista de nodos s a Cmra
          cMra.add(s);
        }

        //Luego iteramos sobre cMra y vamos llenando el array newC
        for (ArrayList<Node> nodeList: cMra) {
          newC.add(outputInterno(nodeList));
        }
      }
      Node result = outputInterno(newC);
      return result.getA();
    }
  }
}
