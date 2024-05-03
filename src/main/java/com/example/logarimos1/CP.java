package com.example.logarimos1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

/**
 * Clase que conforma el método Ciaccia-Patella.
 */
public class CP {
  //Hay que definir bien estos valores
  double B = 2;
  double b = 0.5*B;

  public CP(double tamanoB) {
    this.B = tamanoB;
    this.b = 0.5*tamanoB;
  }

  /**
   * Función que busca el par más cercano al punto p de una lista de puntos.
   * @param p punto al que se le busca el par más cercano
   * @param sample lista de puntos candidatos
   * @return El índice del par más cercano encontrado.
   */
  public Integer nearestSample(Pair p, ArrayList<Pair> sample) {
    int n = sample.size();
    double minDist = Double.MAX_VALUE;
    int resultIndex = 0;

    for(int i = 0; i<n;i++) {
      Pair sample_i = sample.get(i);

      if(p.dist(sample_i)<minDist) {
        minDist = p.dist(sample_i);
        resultIndex = i;
      }
    }

    return resultIndex;
  }

  /**
   * Método que asigna un punto a un sample.
   * @param p el punto a reasignar
   * @param listSamples la lista de samples disponibles
   * @param hash el hash que guarda la información de cada sample y el conjunto de puntos asociado
   */
  public void asignSample(Pair p, ArrayList<Pair> listSamples, HashMap<Integer, ArrayList<Pair>> hash) {
    //Se consigue el sample más cercano al punto p
    int index = nearestSample(p, listSamples);

    //Se obtiene la lista de puntos de ese sample y se actualiza después de agregar el punto p
    ArrayList<Pair> points = hash.get(index);

    if (points == null) {
      points = new ArrayList<>();
    }

    points.add(p);
    hash.put(index, points);
  }

  /**
   * Utiliza el algoritmo Ciaccia-Patella para construir un M-Tree.
   * @param cIn Es un conjunto de puntos (Pares).
   * @return El M-Tree construido
   */
  public NodeCP cp(ArrayList<Pair> cIn) {
    //Se revisa si los puntos de cIn se pueden contener en un bloque o no, en caso de hacerlo retornamos
    if (cIn.size() <= B) {
      NodeCP a = new NodeCP();
      a.insert(cIn);
      return a;
    } else {
      //Definimos una variable random
      Random random = new Random();
      random.nextInt();

      int n = cIn.size();

      //Conjunto que contendrá a los samples de puntos
      ArrayList<Pair> f = new ArrayList<>();

      // F contiene los samples
      // samples contiene el key: indice y value: sus puntos asociados
      HashMap<Integer,ArrayList<Pair>> samples = new HashMap<>();

      //Hacemos do-while en caso de tener que repetir la selección de samples.
      do {
        //k corresponde a la cantidad de puntos aleatorios a escoger para conformar la lista de samples
        int k = (int) Math.min(B, n/B);
        ArrayList<Integer> outIndex = new ArrayList<>();

        //Se hace la selección de los índices de puntos que serán asigandos como samples
        //en un ciclo hasta haber escogido los k valores necesarios
        while (outIndex.size() < k) {
          int j = random.nextInt(k);

          //Se revisa que los puntos a insertar no sean repetidos
          if(!outIndex.contains(j)) {
            outIndex.add(j);
            f.add(cIn.get(j));
          }
        }

        //Asignamos un sample a cada punto de cIn
        for (int i=0; i<n; i++) {
          asignSample(cIn.get(i),f,samples);
        }

        //Revisamos si hay algun sample de tamaño menor a b
        for (int i=0; i<f.size(); i++) {
          ArrayList<Pair> points = samples.get(i);

          //En caso de cumplir la condicion, el sample se vuelve un punto corriente y se reasigna junto con sus hijos.
          if (points.size() < b) {
            samples.remove(i);
            Pair removedPoint = f.remove(i);

            //Este sample pasa a ser un punto 'normal' por lo que lo agregamos a la lista de puntos por reasignar
            //después de eliminarlo de la lista de samples
            points.add(removedPoint);

            // Cada punto del sample eliminado es reasignado al siguiente más cercano
            for (Pair point : points) {
              asignSample(point,f,samples);
            }
          }
        }
        //En caso de que el tamaño de F sea 1, repetimos el ciclo
      } while (f.size() == 1);

      //Creamos una lista para ir almacenando los árboles entregados por cp
      ArrayList<NodeCP> trees = new ArrayList<>();

      for(int i = 0; i < f.size(); i++) {
        NodeCP a = cp(samples.get(i));

        if (a.size() < b) {
          Pair pfj = f.remove(i);  // HAY QUE VER QUE SE HACE CON ESTO

          ArrayList<NodeCP> childs = a.getChilds();
          for(int j=0; j<childs.size(); j++) {
            NodeCP child = childs.get(j); // subarboles
            f.add(child.getSample());
            trees.add(child); // se añaden los subarboles como arboles a T (trees)
          }
        } else {
          trees.add(a);
        }
      }
      // Balanceamiento

      //Buscamos la altura mínima entre los subárboles
      int h = Integer.MAX_VALUE;
      for(int i = 0; i < trees.size(); i++) {
        int tempH = trees.get(i).getH();
        if(tempH < h){
          h = tempH;
        }
      }

      //Se define el conjunto T vacío
      ArrayList<NodeCP> t = new ArrayList<>();

      for(int i=0; i<trees.size(); i++) {
        NodeCP ti = trees.get(i);
        if(ti.getH() == h) {
          t.add(ti);
        } else {
          Pair p = ti.getSample(); // HAY QUE VER QUE SE HACE CON ESTO
          int index = f.indexOf(p);
          f.remove(index);
        }
      }





    }
  }
}

