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
        Cluster nearest = cluster.nearest((ArrayList<Cluster>) c.clone());

        if (cluster.getG().dist(nearest.getG()) < actualDist) {
          actualDist = cluster.getG().dist(nearest.getG());

          if (cluster.getElements().size() >= nearest.getElements().size()) {
            c1 = cluster;
            c2 = nearest;
          } else {
            c1 = nearest;
            c2 = cluster;
          }
        }
      }

      Cluster c1c2 = c1.join(c2);
      c.remove(c1); 

      if (c1c2.getElements().size() <= B) {
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

    if(cLcP.getElements().size() <= B) {
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
    //Creamos las estructuras necesarias
    TupleSS result = new TupleSS();
    NodeSS c = new NodeSS();
    double r = 0;
    Pair primaryG = cIn.getG();

    //Asignamos al nodo c el punto representante de este cluster
    c.setPoint(primaryG);

    //Luego, por cada punto creamos una tupla del estilo: (punto, 0, null) y lo agregamos al nodo
    for (Pair point : cIn.getElements()) {
      TupleSS extNode = new TupleSS();
      extNode.setG(point);
      extNode.setA(null);
      extNode.setR(0);

      c.addEntry(extNode);

      //Actualizamos el radio si es necesario
      r = Math.max(r, primaryG.dist(extNode.getG()));
    }

    //Definimos los datos de la tupla result
    result.setR(r);
    result.setG(primaryG);
    result.setA(c);

    // result = TupleSS(g,r,a = NodeSS(c = {TupleSS(g,r = 0,a = null), ...}))
    return result;
  }

  /**
   * En base a una lista de nodos externos, conforma un conjunto de nodos internos
   * @return Un conjunto de nodos internos
   */
  public TupleSS outputInterno(ArrayList<TupleSS> cMra) {
    //Se define un cluster para generar un medoide G a partir de cMra
    Cluster cIn = new Cluster();

    //Agregamos cada elemento para ir armando el cluster
    for (TupleSS entry : cMra) {
      Pair primaryG = entry.getG();
      cIn.addElement(primaryG);
    }

    //Extraemos el medoide del cluster
    Pair G = cIn.getG();
    //Definimos R
    double R = 0;
    //Definimos el conjunto C
    NodeSS c = new NodeSS();

    //Agregamos todos los (g,r,a) en cMra
    for (TupleSS entry : cMra) {
      c.addEntry(entry);
      //Definimos g y r para setear R
      Pair g = entry.getG();
      double r = entry.getR();
      //Seteamos R
      R = Math.max(R, g.dist(G) + r);
    }

    //DEfinimos el punto representante del nodo como el asociado a la tupla
    c.setPoint(G);

    //Definimos los datos de la tupla a retornar
    TupleSS result = new TupleSS();
    result.setG(G);
    result.setR(R);
    result.setA(c);

    // result = TupleSS(g,r,a = NodeSS(c = TupleSS(g,r,a = NodeSS(c = cositas))))
    return result;
  }

  /**
   * Aplica el algoritmo SS sobre un conjunto de puntos.
   * @returns Un M-Tree construido
   */
  public NodeSS ss(ArrayList<Pair> cIn) {
    ArrayList<Cluster> cOut = cluster(cIn);

    // Caso Base con cIn <= B -> Simplemente se forma un Nodo Hoja para todo (Aún así debe pasar por la función Cluster).
    if (cIn.size() <= B) {
      NodeSS extNode = new NodeSS();

      // Por cada entry que sale de outputHoja, conformamos un Nodo con c
      // al cual se le añaden todas estas entries nuevas
      for (Cluster cluster : cOut) {
        TupleSS entry = outputHoja(cluster);
        extNode.addEntry(entry);
      }

      return extNode;

    } else {
      //Conjunto para hacer el return
      ArrayList<TupleSS> newC = new ArrayList<TupleSS>();

      ArrayList<TupleSS> c = new ArrayList<TupleSS>();

      // Se convierten los elementos de cOut (clusters) a hijos
      // Se añade TupleSS(g,r,a = NodeSS(c = TupleSS(g,r,a = null))) a C
      for (Cluster cluster : cOut) {
        TupleSS entry = outputHoja(cluster);
        c.add(entry);
      }


      if(c.size() <= B){
        newC = (ArrayList<TupleSS>) c.clone();
      }
      //Mientras |C| > B
      while (c.size() > B) {
        //Armamos el conjunto cIn2, que contiene los medoides (puntos) de los clusters de c
        ArrayList<Pair> cIn2 = new ArrayList<>();

        for (TupleSS entry: c) {
          cIn2.add(entry.getG());
        }
        //Luego creamos el conjunto de clusters cOut2, en base al cIn2
        ArrayList<Cluster> cOut2 = cluster(cIn2);
        //Conjunto de listas de nodos Cmra
        ArrayList<ArrayList<TupleSS>> cMra = new ArrayList<ArrayList<TupleSS>>();

        //Por cada cluster e en cOut2
        for (Cluster cluster : cOut2) {
          //Rescatamos los elementos del cluster
          ArrayList<Pair> clusterElements = cluster.getElements();

          //Definimos el conjunto s como una entrada de c tal que su medoide pertenece al cluster e
          ArrayList<TupleSS> s = new ArrayList<TupleSS>();

          //Iteramos sobre los elementos de c para encontrar las entradas que satisfagan la condición de s
          int n = c.size();
          ArrayList<TupleSS> cCopy = (ArrayList<TupleSS>) c.clone();
          for (int i=0; i<n; i++) {
            TupleSS entry = cCopy.get(i);
            if (clusterElements.contains(entry.getG())) {
              s.add(entry);
              c.remove(entry);
            }
          }

          //Luego añadimos la lista de nodos s a Cmra
          cMra.add(s);
        }

        //Luego iteramos sobre cMra y vamos llenando el array newC
        for (ArrayList<TupleSS> entryList: cMra) {
            TupleSS intEntry = outputInterno(entryList);
            newC.add(intEntry);
        }
      }

      TupleSS result = outputInterno(newC);
      return result.getA();
    }
  }
}
