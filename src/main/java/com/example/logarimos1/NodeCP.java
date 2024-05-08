package com.example.logarimos1;

import javafx.scene.Node;

import java.util.ArrayList;

/**
 * Clase que representa un nodo creado por el algoritmo CP.
 */
public class NodeCP extends Node {
  /** Altura del árbol */
  private int h = Integer.MAX_VALUE;
  /** Lista de nodos hijos */
  private ArrayList<TupleCP> entries = new ArrayList<TupleCP>();

  /**
   * @return altura del nodo
   */
  public int getH() {
    return h;
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

  public void addChild(TupleCP newChild) {
    entries.add(newChild);
  }

  /**
   * Inserta una lista de puntos al nodo.
   * @param cIn Lista de puntos (Pares).
   */
  public void insert(ArrayList<Pair> cIn) {

  }

  /**
   * @return Devuelve el tamaño de los hijos.
   */
  public double size() {
    return this.entries.size();
  }

  /**
   * Busca un nodo específico dentro de un M-Tree.
   * @return -1 si se encuentra, el número de ingresos a disco si lo encuentra.
   */
  public NodeCP search(NodeCP mTree, Pair query) {
    // Comparar ingresos a disco
    // Buscar el nodo correspondiente
    // Cada vez que se ingresa a un nodo se suma 1

    return null;
  }
}
