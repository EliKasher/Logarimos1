package com.example.logarimos1;

import java.util.ArrayList;

public class Cluster {

  /** Agrega un par ordenado al cluster.
   * @param actualPoint el valor del par ordenado
   */
  public void addElement(Pair actualPoint) {

  }

  /** Eliminar un par ordenado del cluster.
   * @param actualPoint el valor del par ordenado
   */
  public void deleteElement(Pair actualPoint){

  }

  /** Une 2 Clusters y retorna esta unión
   * @param c el cluster con el que se quiere unir
   */
  public Cluster join(Cluster c) {

  }

  /** Busca y retorna el vecino más cercano dentro de una lista
   * @param list la lista de clusters en donde buscar
   */
  public Cluster nearest(ArrayList<Cluster> list) {

  }

  /** Retorna 2 clusters que vienen de la división de uno
   */
  public ArrayList<Cluster> minMaxSplit(){

  }

  /**
   * Tamaño del nodo
   * Node Size -> max(B · sizeof(entry))
   * min Capacity -> b = 0.5 · B
   * max Capacity -> B
   *
   * 1 bloque -> 4096 Bytes (1)
   * @return
   */
  public double size() {
    return 1;
  }
}
