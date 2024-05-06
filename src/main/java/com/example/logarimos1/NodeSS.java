package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que representa un nodo creado por el algoritmo SS.
 */
public class NodeSS {
  private ArrayList<TupleSS> entries;

  /**
   * Las entradas asociadas, que se guardan dentro de otro nodo.
   */
  public NodeSS() {
    entries = new ArrayList<TupleSS>();
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
   * @param q El punto sobre el que se busca
   * @param r Radio de búsqueda
   * @return La lista de resultados Result con los pares encontrados y sus accesos a memoria.
   */
  public Result search(Pair q,  double r) {
    return this.auxSearch(q,r,null);
  }

  /**
   * Es un search con memoria.
   * Busca los pares dentro de un radio de búsqueda r para un punto q, en un M-Tree.
   * @param q El punto sobre el que se busca
   * @param r Radio de búsqueda
   * @param nodeG El medoide g del arbol previo.
   * @return null si no se encuentra, el array de pares coincidentes si los encuentra.
   */
  public Result auxSearch(Pair q, double r, Pair nodeG) {
    // Creamos la estructura para almacenar el resultado
    Result res = new Result();

    // Cuando el arbol es nulo
    if (this == null) {
      return null;
    }
    // Caso donde NodeSS(c = ArrayList<TupleSS> (g,r = 0,a = null))
    ArrayList<TupleSS> treeEntries = this.getEntries();
    // Si el nodo es una hoja (nodo externo)
    if (treeEntries == null) {
      Pair p = nodeG;
      double d = q.dist(p);

      if (d <= r) {
        res.addPoint(p);
      }
    } else {
      // Si el nodo es interno, significa que vamos a acceder a disco para obtener su infromación
      // por lo que aumentamos el contador del resultado
      // Caso donde NodeSS(c = ArrayList<TupleSS> (g,r,a = NodeSS(c)))
      res.memoryAccess(1);

      for(TupleSS entry : this.getEntries()) {
        double d = q.dist(entry.getG());

        //Si la distancia es menor al radio de búsqueda, se ingresa para buscar recursivamente
        if (d <= r + entry.getR()) {
          Pair newNodeG = entry.getG();

          Result resultChild = entry.getA().auxSearch(q, r, newNodeG);

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
