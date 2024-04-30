package com.example.logarimos1;

import java.util.ArrayList;
import java.lang.Math;

public class Cluster {
  //VARIABLES
  /** Variable privada para mantener los elementos del cluster */
  final ArrayList<Pair> elements = new ArrayList<Pair>();

  /** El punto medoide del cluster */
  private Pair g = null;

  /** El radio cobertor del cluster */
  private double r = 0;

  //GETTERS Y SETTERS
  /** Retorna una copia de los elementos del cluster */
  public ArrayList<Pair> getElements() {
    ArrayList<Pair> a = (ArrayList<Pair>) elements.clone();
    return a;
  }

  /** Retorna el punto medoide del cluster */
  public Pair getG() {
    return g;
  }

  /** Retorna el radio cobertor del cluster  */
  public double getR() {
    return r;
  }

  /** Setter del medoide */
  private void setG(Pair newG){
    this.g = newG;
  }

  /** Setter del radio cobertor */
  private void setR(double newR){
    this.r = newR;
  }


  //MÉTODOS
  /** Agrega un par ordenado al cluster y actualiza el radio cobertor si es necesario
   * @param actualPoint el valor del par ordenado
   */
  public void addElement(Pair actualPoint) {
    //Si el punto agregado es el primer punto, corresponde al medoide
    if(elements.isEmpty()) {
      setG(actualPoint);
    } else {
      //Si el punto agregado está más lejos del actual radio cobertor, se debe modificar
      double d = g.dist(actualPoint);
      if(d>r) {
        this.setR(d);
      }
    }
    elements.add(actualPoint);
  }

  /** Eliminar un par ordenado del cluster.
   * @param actualPoint el valor del par ordenado
   */
  public void deleteElement(Pair actualPoint) {
    //PENDIENTE: actualizar radio cobertor
    elements.remove(actualPoint);  // HAY QUE TESTEAR ESTA FUNCION
  }

  /** Une este Cluster con otro y retorna esta unión
   * @param c el cluster con el que se quiere unir
   */
  public Cluster join(Cluster c) {
    //Definimos las listas con los elementos de ambos cluster
    ArrayList<Pair> c1 = this.getElements();
    ArrayList<Pair> c2 = c.getElements();
    //Creamos el cluster que se va a retornar
    Cluster c3 = new Cluster();

    //Agregamos todos los elementos de ambos clusters
    for(int i=0; i<c1.size(); i++){
      c3.addElement(c1.get(i));
    }
    for(int i=0; i<c2.size(); i++){
      c3.addElement(c2.get(i));
    }

    //Actualizamos el medoide
    c3.medoide();

    return c3;
  }

  /** Busca y retorna el cluster vecino más cercano dentro de una lista
   * @param list la lista de clusters en donde buscar
   */
  public Cluster nearest(ArrayList<Cluster> list) {
    double minDist = Double.MAX_VALUE;
    Cluster nearest = null;
    Pair actualG = this.getG();

    //Quitamos al Cluster desde el que se llama la función de la lista
    list.remove(this);

    for (Cluster actualCluster: list) {
      if (actualG.dist(actualCluster.getG()) < minDist) {
        minDist = actualG.dist(actualCluster.getG());
        nearest = actualCluster;
      }
    }

    return nearest;
  }

  /** Retorna 2 clusters que vienen de la división de uno
   */
  public ArrayList<Cluster> minMaxSplit() {
    //Variables para ir guardando los clusters elegidos
    Cluster c1 = new Cluster();
    Cluster c2 = new Cluster();

    //Variable para ir guardando el radio cobertor máximo entre c1 y c2, lo inicializamos
    //muy grande para poder ir comparándolos y elegir el menor
    double finalR = Double.MAX_VALUE;

    //Iteramos sobre los puntos del cluster y vamos evaluando
    ArrayList<Pair> e = this.getElements();
    for(Pair pt1: e) {
      ArrayList<Pair> e2 = e;
      //Eliminamos este punto para no repetir
      e2.remove(pt1);
      for(Pair pt2: e2) {
        //Eliminamos este punto para no repetir
        e2.remove(pt2);

        //Agregamos estos puntos a clusters como medoides
        Cluster temporalC1 = new Cluster();
        Cluster temporalC2 = new Cluster();
        temporalC1.addElement(pt1);
        temporalC1.addElement(pt2);

        //Agregamos los demás puntos a estos clusters alternadamente,
        //para índices par lo agregamos al cluster 2, sino, al cluster 1
        for (int i=0; i<e2.size(); i++) {
          if (i%2==0) {
            temporalC2.addElement(e2.get(i));
          } else {
            temporalC2.addElement(e2.get(i));
          }
        }

        //Conseguimos el máximo entre sus radios cobertores
        double currentR = Math.max(temporalC1.getR(), temporalC2.getR());

        //Evaluamos si es una mejor elección de clusters de la que teníamos
        if (currentR<finalR) {
          c1 = temporalC1;
          c2 = temporalC2;

          finalR = currentR;
        }
      }
    }
    //Con el ciclo anterior conseguimos las 2 mejores elecciones de cluster,
    //los retornamos en una lista de 2 objetos
    ArrayList<Cluster> a = new ArrayList<Cluster>();
    a.add(c1);
    a.add(c2);

    return a;
  }

  /**
   * El medoide de un cluster es un punto g en el cluster tq
   * No existe otro punto p en el cluster que, al ser nominado
   * como medoide, genere un r < al entregado por g.
   * Se definen el medoide del cluster junto a su radio cobertor.
   */
  public void medoide() {
    // Sólo hay 1 candidato a medoide
    if(elements.size()==1) {
      g = elements.get(0);
    } else {
      //Guardamos las distancias máximas de cada punto al considerarlo medoide
      double radio = Double.MAX_VALUE;
      //Guardamos el mejor candidato a medoide
      Pair m = new Pair(0,0);

      //Iteramos sobre todo par de elementos en el medioide
      for (Pair p: elements){

        //Guardamos, para el punto p, la distancia máxima a cualquier otro punto
        double maxDist = 0;

        //Vamos a iterar nuevamente, considerando al punto p como 'centro' y calculando el radio según p
        for (Pair p2: elements){
          if(p.dist(p2)>maxDist){
            maxDist = p.dist(p2);
          }
        }

        //Si la distancia máxima es menor que el radio que se tenía anteriormente
        if(maxDist<radio){
          radio = maxDist;
          m = p;
        }
      }
      r = radio;
      g = m;
    }
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
