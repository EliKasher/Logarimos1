package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que conforma el método Sexton-Swinbank
 */
public class SS {
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
      double actualDist = Double.MAX_VALUE;

      for (Cluster cluster : c) {
        Cluster nearest = cluster.nearest(c);

        if (cluster.getG().dist(nearest.getG()) < actualDist) {
          actualDist = cluster.getG().dist(nearest.getG());

          if (cluster.size() >= nearest.size()) {
            c1 = cluster;
            c2 = nearest;
          } else {
            c1 = nearest;
            c2 = cluster;
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
    // Si salimos del while, se sabe que sólo queda 1 elemento
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
   * Conforma un nodo hoja en base a los puntos de un cluster
   *
   * @return Retorna el nodo hoja conformado
   */
  public TupleSS outputHoja(Cluster cIn) {
    TupleSS result = new TupleSS();
    NodeSS c = new NodeSS();
    double r = 0;
    Pair primaryG = cIn.getG();


    for (Pair point : cIn.getElements()) {
      TupleSS extNode = new TupleSS();
      extNode.setG(point);
      extNode.setA(null);
      extNode.setR(0);

      c.addEntry(extNode);

      r = Math.max(r, primaryG.dist(extNode.getG()));
    }

    result.setR(r);
    result.setG(primaryG);
    result.setA(c);

    // result = TupleSS(g,r,a = NodeSS(c = TupleSS(g,r,a = null)))
    return result;
  }

  /**
   * En base a una lista de nodos externos, conforma un conjunto de nodos internos
   * @return Un conjunto de nodos internos
   */
  public ArrayList<TupleSS> outputInterno(ArrayList<NodeSS> cMra) {
    //Se define un cluster para generar un medoide G a partir de cMra
    Cluster cIn = new Cluster();
    //Agregamos cada elemento para ir armando el cluster
    for (NodeSS node : cMra) {
      Pair g = node.getG();
      cIn.addElement(g);
    }
    //Extraemos el medoide del cluster
    Pair G = cIn.getG();
    //Definimos R
    double R = 0;
    //Definimos el conjunto C
    ArrayList<NodeSS> c = new ArrayList<NodeSS>();
    //Agregamos todos los (g,r,a) en cMra
    for (NodeSS node : cMra) {
      c.add(node);
      //Definimos g y r para setear R
      Pair g = node.getG();
      double r = node.getR();
      //Seteamos R
      R = Math.max(R, g.dist(G) + r);
    }

    NodeSS result = new NodeSS();
    result.setG(G);
    result.setR(R);
    result.setA(c);
    return result;
  }

  /**
   * Aplica el algoritmo SS sobre un conjunto de puntos.
   * @returns un M-Tree construido
   */
  public ArrayList<TupleSS> ss(ArrayList<Pair> cIn) {
    ArrayList<Cluster> cOut = cluster(cIn);

    // Caso Base con cIn <= B -> Simplemente se forma un Nodo Hoja para todo (Aún así debe pasar por la función Cluster).
    if (cIn.size() <=B) {
      NodeSS extNode = new NodeSS();

      for (Cluster cluster : cOut) {
        TupleSS entry = outputHoja(cluster);
        extNode.addEntry(entry);
      }

      return extNode.getEntries();
    } else {
      //Conjunto para hacer el return
      ArrayList<NodeSS> newC = new ArrayList<NodeSS>();

      ArrayList<NodeSS> c = new ArrayList<NodeSS>();

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

        for (NodeSS nodo: c) {
          cIn2.add(nodo.getG());
        }
        //Luego creamos el conjunto de clusters cOut2, en base al cIn2
        ArrayList<Cluster> cOut2 = cluster(cIn2);

        //Conjunto de listas de nodos Cmra
        ArrayList<ArrayList<NodeSS>> cMra = new ArrayList<ArrayList<NodeSS>>();

        //Por cada cluster e en cOut2
        for (Cluster cluster : cOut2) {
          Cluster e = cluster;

          //Rescatamos los elementos del cluster e
          ArrayList<Pair> eElements = e.getElements();

          //Definimos el conjunto s como un nodo de c tal que su medoide pertenece al cluster e
          ArrayList<NodeSS> s = new ArrayList<NodeSS>();

          //Iteramos sobre los elementos de c para encontrar los nodos que satisfagan la condición de s
          for (NodeSS nodo : c) {
            if (eElements.contains(nodo.getG())) {
              s.add(nodo);
            }
          }
          //Luego añadimos la lista de nodos s a Cmra
          cMra.add(s);
        }

        //Luego iteramos sobre cMra y vamos llenando el array newC
        for (ArrayList<NodeSS> nodeList: cMra) {
          newC.add(outputInterno(nodeList));
        }
      }
      NodeSS result = outputInterno(newC);
      return result;
    }
  }
}
