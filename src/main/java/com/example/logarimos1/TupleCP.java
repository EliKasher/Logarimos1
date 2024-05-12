package com.example.logarimos1;

import java.util.ArrayList;

public class TupleCP {
  /** Radio Cobertor */
  private double r = 0;

  /** El sample de esta tupla */
  private Pair sample;

  /** Nodo que contiene tuplas 'hijas' */
  private NodeCP a;

  public TupleCP() {
  }

  /**
   * @return radio cobertor
   */
  public double getR() {
    return r;
  }

  /**
   * @return El sample de la tupla asociada
   */
  public Pair getSample() {
    return sample;
  }

  /**
   * @return El nodo hijo.
   */
  public NodeCP getA() {
    return a;
  }

  /**
   * Cambia el radio a newR
   * @param newR nuevo radio cobertor
   */
  public void setR(double newR) {
    r = newR;
  }

  /**
   * Cambia el sample asociado al Nodo actual.
   * @param newSample El nuevo sample asociado.
   */
  public void setSample(Pair newSample) {
    sample = newSample;
  }

  /**
   * Añade un nodo hijo a la entrada.
   * @param nodeCP Un nodo.
   */
  public void setA(NodeCP nodeCP) {
    this.a = nodeCP;
  }

  /**
   * Setea los radios cobertores de ésta tupla y las asociadas a su hijo nodo.
   * @param lastSample el sample más cercano anterior a esta tupla
   * @return la distancia entre el punto de ésta tupla y el punto lastSample
   */
  public double initializeR(Pair lastSample) {
    NodeCP child = this.getA();
    // Si la tupla no tiene un nodo hijo, significa que es una 'tupla hoja',
    // por lo que no se actualiza su radio cobertor y se retorna la distancia
    // al sample 'más cercano' (entregado como lastSample)
    if(child == null) {
      return lastSample.dist(this.getSample());
      // Estamos en un nodo interno
    } else {
      // Si esta tupla tiene hijos,
      // se debe encontrar la máxima distancia
      // entre este nodo y su hijo
      double maxDist = 0;
      for (TupleCP entry : child.getEntries()) {
        double d = entry.initializeR(this.getSample());
        // Si la distancia es mayor a la actual se guarda
        if (d > maxDist) {
          maxDist = d;
        }
      }
      // Se setea el radio cobertor como la máxima
      // distancia entre el sample actual con alguno de sus hijos sumado
      // con la distancia entre el sample de esta tupla con el lastSample
      double R = maxDist + lastSample.dist(this.getSample());
      this.setR(R);
      // Se retorna la máxima distancia para la recursión
      return R;
    }
  }

}
