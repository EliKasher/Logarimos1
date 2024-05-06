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
    Pair sample_i = points.get(index);

    if (points == null) {
      points = new ArrayList<>();
      points.add(sample_i);
    }

    points.add(p);
    hash.put(index, points);
  }

/**
 * En base a una lista de árboles, una altura h definida y un arreglo de samples
 * este método agrega los samples de los subárboles que posean altura h, además
 * agrega estos árboles a una lista y luego la retorna.
 * @param h altura
 * @param t árbol que se quiere analizar
 * @param samples conjunto de samples ya existente
 * @return El conjunto de árboles con altura h
 * */
  //public ArrayList<NodeCP> getHtree(int h, NodeCP t, ArrayList<Pair> samples) {
    //ArrayList<NodeCP> result = new ArrayList<>();

    //if(t.getH() == h) {
      //Agregamos el ábol de altura h a la lista a retornar
      //result.add(t);

      //Agregamos el punto correspondiente a la lista de samples
      //samples.add();
    //} else {
      //Si no tiene altura h, se busca recursivamente en sus hijos
      //ArrayList<TupleCP> childsTuples = t.getEntries();
      //for (TupleCP child : childsTuples) (int i = 0; i < childsTuples.size(); i++) {
        //result.addAll(getHtreeAux(h, childsTuples, samples, newF.get(i)));
      //}
    //}
    //return result;
  //}

  /**
   * En base a una lista de árboles, una altura h definida y un arreglo de samples
   * este método agrega los samples de los subárboles que posean altura h, además
   * agrega estos árboles a una lista y luego la retorna.
   * @param h altura
   * @param t árbol que se quiere analizar
   * @param samples conjunto de samples ya existente
   * @return El conjunto de árboles con altura h
   * */
  public ArrayList<NodeCP> getHtree(int h, NodeCP t, ArrayList<Pair> samples) {
    return getHtreeAux(h, t, samples, null);
  }

  public ArrayList<NodeCP> getHtreeAux(int h, NodeCP t, ArrayList<Pair> samples, Pair f) {
    ArrayList<NodeCP> result = new ArrayList<>();

    if(t.getH() == h) {
      //Agregamos el ábol de altura h a la lista a retornar
      result.add(t);

      //Agregamos el punto correspondiente a la lista de samples
      samples.add(f);
    } else {
      //Si no tiene altura h, se busca recursivamente en sus hijos
      ArrayList<TupleCP> childsTuples = t.getEntries();
      for (int i = 0; i < childsTuples.size(); i++) {
        result.addAll(getHtreeAux(h, childsTuples.get(i).getA(), samples, samples.get(i)));
      }
    }
    return result;
  }

  /**
   * Utiliza el algoritmo Ciaccia-Patella para construir un M-Tree.
   * @param cIn Es un conjunto de puntos (Pares).
   * @return El M-Tree construido
   */
  public NodeCP cp(ArrayList<Pair> cIn) {
    //Se revisa si los puntos de cIn se pueden contener en un bloque o no, en caso de hacerlo retornamos
    if (cIn.size() <= B) {
      //Crea un nodoCP e inserta los puntos como tuplas
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
      HashMap<Integer, ArrayList<Pair>> samples = new HashMap<>();

      //Hacemos do-while en caso de tener que repetir la selección de samples.
      do {
        //k corresponde a la cantidad de puntos aleatorios a escoger para conformar la lista de samples
        int k = (int) Math.min(B, n / B);
        ArrayList<Integer> outIndex = new ArrayList<>();

        //Se hace la selección de los índices de puntos que serán asigandos como samples
        //en un ciclo hasta haber escogido los k valores necesarios
        while (outIndex.size() < k) {
          int j = random.nextInt(k);

          //Se revisa que los puntos a insertar no sean repetidos
          if (!outIndex.contains(j)) {
            outIndex.add(j);
            f.add(cIn.get(j));
          }
        }

        //Asignamos un sample a cada punto de cIn
        for (int i = 0; i < n; i++) {
          asignSample(cIn.get(i), f, samples);
        }

        //Revisamos si hay algun sample de tamaño menor a b
        for (int i = 0; i < f.size(); i++) {
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
              asignSample(point, f, samples);
            }
          }
        }
        //En caso de que el tamaño de F sea 1, repetimos el ciclo
      } while (f.size() == 1);

      //Creamos una lista para ir almacenando los árboles entregados por cp
      ArrayList<NodeCP> trees = new ArrayList<NodeCP>();

      //Creamos una lista de los samples nueva para que tengan el mismo índice que su
      //correspondiente nodo en la lista trees
      ArrayList<Pair> newF = new ArrayList<>();

      for (int i = 0; i < f.size(); i++) {
        NodeCP a = cp(samples.get(i));

        if (a.size() < b) {
          Pair pfj = f.remove(i);

          ArrayList<TupleCP> childs = a.getEntries();
          for (int j = 0; j < childs.size(); j++) {
            TupleCP child = childs.get(j); // Pasa de ser tupla a nodo

            // Agregamos sus samples a newF
            newF.add(child.getSample());

            trees.add(child.getA()); // se añaden los subarboles como arboles a T (trees)
          }
        } else {
          // Se queda con su sample original f[i]
          newF.add(f.get(i));
          trees.add(a);
        }
      }

      // Balanceamiento
      // Buscamos la altura mínima entre los subárboles
      int h = Integer.MAX_VALUE;
      for (int i = 0; i < trees.size(); i++) {
        int tempH = trees.get(i).getH();
        if (tempH < h) {
          h = tempH;
        }
      }

      // Se define el conjunto T vacío
      ArrayList<NodeCP> t = new ArrayList<>();
      NodeCP tSup = new NodeCP();

      // Los subarboles en trees contienen solo una entry
      // Se eligen los que sirven y se añaden a t
      for (int i = 0; i < trees.size(); i++) {
        NodeCP ti = trees.get(i);
        if (ti.getH() == h) {
          t.add(ti);
        } else {
          //Se saca el sample del conjunto
          Pair p = newF.get(i);
          newF.remove(i);
          trees.remove(i);

          // Se buscan los subarboles de altura h y se agregan sus respectivos samples a F
          ArrayList<NodeCP> hTrees = getHtree(h, ti, newF);
          for (NodeCP tree : hTrees) {
            t.add(tree);
          }

          //Hacemos cp sobre el conjunto de samples, obteniendo tSup
          tSup = cp(f);

          //Para cada subárbol de altura h buscamos la hoja que corresponda a su sample
          //en el árbol tSup y lo agregamos como hijo
          //for (NodeCP htree : hTrees) {
            //NodeCP obtainedChild = tSup.getChildBySample(htree.getSample());
            //obtainedChild.addChild(htree);
          //}

          //Seteamos los radios cobertores
          //tSup.set();
        }
      }

      return tSup;
    }
  }
}

