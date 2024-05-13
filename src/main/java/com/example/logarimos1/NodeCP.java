package com.example.logarimos1;

import java.util.ArrayList;

/**
 * Clase que representa un nodo creado por el algoritmo CP.
 */
public class NodeCP extends Node {
  /** Altura del árbol */
  private int h = 0;
  /** Lista de nodos hijos */
  private ArrayList<TupleCP> entries = new ArrayList<TupleCP>();
  private Pair sample = null;

  /**
   * @return altura del nodo
   */
  public int getH() {
    return h;
  }

  /**
   * @return Devuelve el sample del nodo.
   */
  public Pair getSample() {
    return sample;
  }

  /**
   * @return Las entradas hijas del nodo.
   */
  public ArrayList<TupleCP> getEntries() {
    return this.entries;
  }

  /**
   * Cambia la altura a newH
   * @param newH la nueva altura del arbol
   */
  public void setH(int newH) {
    h = newH;
  }

  /**
   * Cambia el sample asociado al nodo.
   * @param newSample Un nuevo par asociado al nodo.
   */
  public void setSample(Pair newSample) {
    sample = newSample;
  }

  public void addChild(TupleCP newChild) {
    //si un nodo vale 1
    int hActual = this.getH() - 1;
    NodeCP nodo = newChild.getA();
    if (nodo != null) {
      int nodoH = nodo.getH();
      if (nodoH > hActual) {
        this.setH(nodoH+1);
      }
    } else {
      this.setH(1);
    }
    entries.add(newChild);
  }

  /**
   * Inserta una lista de puntos al nodo.
   * @param cIn Lista de puntos (Pares).
   */
  public void insert(ArrayList<Pair> cIn) {
    for (Pair p : cIn) {
      TupleCP entry = new TupleCP();
      entry.setSample(p);
      this.addChild(entry);
    }
    Pair g = this.medoide(cIn);
    this.setSample(g);
  }

  /**
   * Calcula un medoide representante para el Nodo y lo asigna.
   */
  public Pair medoide(ArrayList<Pair> cIn) {
    Pair g = new Pair(0,0);
    // Sólo hay 1 candidato a medoide
    if (cIn.size() == 1) {
      g = cIn.get(0);
      return g;
    } else {
      //Guardamos las distancias máximas de cada punto al considerarlo medoide
      double radio = Double.MAX_VALUE;
      //Guardamos el mejor candidato a medoide
      Pair m = new Pair(0, 0);

      //Iteramos sobre todo par de elementos en el medioide
      for (Pair p : cIn) {

        //Guardamos, para el punto p, la distancia máxima a cualquier otro punto
        double maxDist = 0;

        //Vamos a iterar nuevamente, considerando al punto p como 'centro' y calculando el radio según p
        for (Pair p2 : cIn) {
          if (p.dist(p2) > maxDist) {
            maxDist = p.dist(p2);
          }
        }

        //Si la distancia máxima es menor que el radio que se tenía anteriormente
        if (maxDist < radio) {
          radio = maxDist;
          m = p;
        }
      }
      g = m;
      return g;
    }
  }

  /**
   * @return Devuelve el tamaño de los hijos.
   */
  public double size() {
    return this.entries.size();
  }

  /**
   * Busca los pares dentro de un radio de búsqueda r para un punto q, en un M-Tree.
   * @param q El punto sobre el que se busca
   * @param r Radio de búsqueda
   * @return null si no se encuentra, el array de pares coincidentes si los encuentra.
   */
  @Override
  public Result search(Pair q, double r) {
    // Creamos la estructura para almacenar el resultado
    Result res = new Result();

    // Cuando el arbol es nulo
    if (this == null) {
      return null;
    }

    // Caso donde NodeCP(c = ArrayList<TupleCP> (sample,r = 0,a = null))
    ArrayList<TupleCP> treeEntries = this.getEntries();
    // Si el nodo es una hoja (nodo externo)
    if (treeEntries == null) {
      Pair p = this.getSample();
      double d = q.dist(p);

      if (d <= r) {
        res.addPoint(p);
      }
    } else {
      // Si el nodo es interno, significa que vamos a acceder a disco para obtener su infromación
      // por lo que aumentamos el contador del resultado
      // Caso donde NodeCP(c = ArrayList<TupleCP> (sample,r,a = NodeSS(c)))
      res.memoryAccess(1);

      for (TupleCP entry : this.getEntries()) {
        double d = q.dist(entry.getSample());

        //Si la distancia es menor al radio de búsqueda, se ingresa para buscar recursivamente
        if (d <= r + entry.getR()) {
          if (entry.getA() == null) {
            res.addPoint(entry.getSample());
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

  /**
   * Busca el sample asociado al subTree.
   * @param subTree Es el subarbol que queremos colgar de este arbol.
   * @return El arbol donde se encuentra el sample.
   */
  public void getChildBySample(NodeCP subTree) {
    Pair subTreeSample = subTree.getSample();

    for (TupleCP entry : this.getEntries()) {
      NodeCP child = entry.getA();
      if (child == null) {
        if(entry.getSample().equals(subTreeSample)) {
          entry.setA(subTree);
          int h = subTree.getH();
          this.setH(h+this.getH());
          return; // Si encontramos el sample, terminamos la ejecución del método
        }
      } else if (child != subTree) { // Solo continuamos con la recursión si el nodo actual no es igual al subárbol que estamos buscando
        child.getChildBySample(subTree);
      }
    }
  }

  /**
   * Se inicializan los radios cobertores para todos los nodos y entradas.
   */
  public void initializeR() {
    // Por cada entrada en el nodo, se setea su radio cobertor
    for (TupleCP entry : this.getEntries()) {
      entry.initializeR(this.getSample());
    }
  }
}
