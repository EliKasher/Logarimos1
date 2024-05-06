package com.example.logarimos1;

import java.util.ArrayList;

public class TupleCP {
  /** Radio Cobertor */
  private double r = 0;

  /** El sample de esta tupla */
  private Pair sample = null;

  /** Nodo que contiene tuplas 'hijas' */
  private NodeCP a = null;

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
   * Devuelve el sample del árbol buscado si se encuentra en el mismo.
   * @param searchSample El sample buscado
   * @return El arbol donde se encuentra el sample.
   */
  public TupleCP getChildBySample(Pair searchSample) {
    //Si el sample buscado corresponde al de este árbol lo retornamos
    if(this.sample.equals(searchSample)){
      return this;
    } else {
      ArrayList<TupleCP> sampleChilds = this.getA().getEntries();
      //Si no, se revisa si no hay más hijos
      if (sampleChilds.isEmpty()) {
        return null;
      } else {
        //Si hay hijos, se revisa recursivamente en ellos
        TupleCP result = new TupleCP();
        for (TupleCP child: sampleChilds) {
          TupleCP temp = child.getChildBySample(searchSample);
          if(temp != null) {
            result = temp;
          }
        }
        return result;
      }
    }
  }
}
