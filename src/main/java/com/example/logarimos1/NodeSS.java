package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que representa un nodo creado por el algoritmo SS.
 */
public class NodeSS {
  //VARIABLES
  /**
   * El punto representante del nodo
   */
  private Pair point = new Pair(0,0);

  /**
   * Las entradas asociadas al nodo (tuplas)
   */
  private ArrayList<TupleSS> entries;

  /**
   * Las entradas asociadas, que se guardan dentro de otro nodo.
   */
  public NodeSS() {
    entries = new ArrayList<TupleSS>();
  }

  //GETTERS y SETTERS
  /**
   * @return el punto representante
   */
  public Pair getPoint(){
    return point;
  }

  /**
   * Cambia el punto representante asociado a este nodo
   * @param newP el nuevo punto representante
   */
  public void setPoint(Pair newP){
    this.point = newP;
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


  //MÉTODOS
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
   * @return null si no se encuentra, el array de pares coincidentes si los encuentra.
   */
  public Result search(Pair q, double r) {
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
      Pair p = this.getPoint();
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
          if (entry.getA() == null) {
            res.addPoint(entry.getG());
          } else {
            Result resultChild = entry.getA().search(q, r);

            //Se accede a los puntos encontrados por el hijo y se agregan al resultado general
            for (Pair point : resultChild.getPoints()) {
              res.addPoint(point);
            }

            //Se accede a la cantidad de accesos de memoria del hijo y se suman al resultado final
            res.memoryAccess(resultChild.getAccessCount());
          }

        }
      }
    }
    return res;
  }
}
