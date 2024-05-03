package com.example.logarimos1;

import java.util.ArrayList;

public class NodeSS {
  private ArrayList<TupleSS> entries;

  public NodeSS() {
    entries = new ArrayList<>();
  }

  /**
   * @return La lista de entradas que contiene el nodo como tuplas (g,r,a).
   */
  public ArrayList<TupleSS> getEntries() {
    return entries;
  }

  /**
   * Cambia las entradas del nodo.
   * @param entries Una lista de entradas de Tuplas (g,r,a)
   */
  public void setEntries(ArrayList<TupleSS> entries) {
    this.entries = entries;
  }

  /**
   * Añade las entradas al nodo.
   * @param entry Una lista de entradas de Tuplas (g,r,a)
   */
  public void addEntry(TupleSS entry) {
    entries.add(entry);
  }

  /**
   * Busca los pares dentro de un radio de búsqueda r para un punto q, en un M-Tree.
   * @return null si no se encuentra, el array de pares coincidentes si los encuentra.
   */
  public Result search(NodeSS mTree, Pair q,  double r) {
    // Comparar ingresos a disco
    // Buscar el nodo correspondiente
    // Cada vez que se ingresa a un nodo se suma 1

    // Creamos la estructura para almacenar el resultado
    Result res = new Result();

    // Cuando el arbol es nulo
    if (mTree == null) {
      return null;
    }

    // Si el nodo es una hoja (nodo externo)
    if (mTree.getA() == null) {
      Pair p = mTree.getG();
      double d = q.dist(p);

      if (d <= r) {
        res.addPoint(p);
      }

      return res;
    } else {
      //Si el nodo es interno, significa que vamos a acceder a disco para obtener su infromación
      //por lo que aumentamos el contador del resultado
      res.memoryAccess(1);

      for(NodeSS child : mTree.getA()) {
        double d = q.dist(child.getG());

        //Si la distancia es menor al radio de búsqueda, se ingresa para buscar recursivamente
        if (d <= r + child.getR()) {
          for (NodeSS childsChild : mTree.getA()) {
            Result resultChild = search(childsChild,q,r);

            //Se accede a los puntos encontrados por el hijo y se agregan al resultado general
            for (Pair point : resultChild.getPoints()) {
              res.addPoint(point);
            }

            //Se accede a la cantidad de accesos de memoria del hijo y se suman al resultado final
            res.memoryAccess(resultChild.getAccessCount());
          }
        }
      }

      return res;
    }
  }
}
