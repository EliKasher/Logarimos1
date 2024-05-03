package com.example.logarimos1;

import java.util.ArrayList;

public class NodeCP {
  /** Radio Cobertor */
  private double r = 0;
  /** Altura del árbol */
  private int h = Integer.MAX_VALUE;
  private Pair sample = null;

  public NodeCP() {
  }

  /**
   * @return radio cobertor
   */
  public double getR() {
    return r;
  }

  /**
   * @return altura del árbol
   */
  public int getH() {
    return h;
  }

  /**
   * @return El sample del Nodo asociado.
   */
  public Pair getSample() {
    return sample;
  }

  /**
   * Cambia el radio a newR
   * @param newR nuevo radio cobertor
   */
  public void setR(double newR) {
    r = newR;
  }

  /**
   * Cambia la altura a newH
   * @param newH la nueva altura del arbol
   */
  public void setH(int newH) {
    h = newH;
  }

  /**
   * Cambia el sample asociado al Nodo actual.
   * @param newSample El nuevo sample asociado.
   */
  public void setSample(Pair newSample) {
    sample = newSample;
  }

  /**
   * Inserta una lista de puntos al nodo.
   * @param cIn Lista de puntos (Pares).
   */
  public void insert(ArrayList<Pair> cIn) {

  }

  /**
   * Busca un nodo específico dentro de un M-Tree.
   * @return -1 si se encuentra, el número de ingresos a disco si lo encuentra.
   */
  public int search(NodeCP mTree, Pair query) {
    // Comparar ingresos a disco
    // Buscar el nodo correspondiente
    // Cada vez que se ingresa a un nodo se suma 1

    return null;
  }

  public double size() {

  }

  public ArrayList<NodeCP> getChilds() {

  }
}
