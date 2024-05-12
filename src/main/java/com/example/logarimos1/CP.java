package com.example.logarimos1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    for (int i = 0; i<n;i++) {
      Pair sample_i = sample.get(i);
      double actualDist = p.dist(sample_i);

      if (actualDist < minDist) {
        minDist = actualDist;
        resultIndex = i;
      }
    }
    return resultIndex;
  }

  /**
   * Método que asigna un punto a un sample.
   * @param p el punto a reasignar
   * @param f la lista de samples disponibles
   * @param hash el hash que guarda la información de cada sample y el conjunto de puntos asociado
   */
  public HashMap<Integer, ArrayList<Pair>> asignSample(Pair p, ArrayList<Pair> f, HashMap<Integer, ArrayList<Pair>> hash) {
    //Se consigue el sample más cercano al punto p
    int index = nearestSample(p, f);

    //Se obtiene la lista de puntos de ese sample y se actualiza después de agregar el punto p
    ArrayList<Pair> points = new ArrayList<>(hash.get(index));
    points.add(p);
    hash.put(index, points);
    return hash;
  }

/**
 * Tomando un árbol y una altura h, este método agrega los
 * subarboles que posean altura h a una lista de nodos
 * y luego la retorna.
 * @param h altura
 * @param t árbol que se quiere analizar
 * @return El conjunto de subárboles con altura h
 * */
  public ArrayList<NodeCP> getHtree(int h, NodeCP t) {
    ArrayList<NodeCP> result = new ArrayList<>();

    if(t.getH() == h) {
      //Agregamos el ábol de altura h a la lista a retornar
      result.add(t);
    } else {
      //Si no tiene altura h, se busca recursivamente en sus hijos
      ArrayList<TupleCP> childsTuples = t.getEntries();
      for (TupleCP child : childsTuples) {
        result.addAll(getHtree(h, child.getA()));
      }
    }
    return result;
  }

  /**
   * Cada vez que se remueven elementos del hash, hay que actualizar las llaves desde
   * la llave index, restándole 1 al valor a las llaves.
   * @param hash Un hashmap con llaves a los conjuntos de pares de un sample.
   * @param index El index removido desde donde se removió un elemento.
   * @return
   */
  public HashMap<Integer, ArrayList<Pair>> updateHash(HashMap<Integer, ArrayList<Pair>> hash, int index) {
    //desde el index en adelante en el hash se deben actualizar los índices
    int hashSize = hash.size();

    if (hashSize == index+1) {
      hash.remove(index);
    } else {
      for (int i = index; i < hash.size()-1; i++) {
        ArrayList<Pair> points = hash.get(i+1);
        if (points != null) {
          hash.put(i, points);
        }
      }
      hash.remove(hash.size()-1);
    }
    return hash;
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
      HashMap<Integer, ArrayList<Pair>> samples = new HashMap<Integer, ArrayList<Pair>>();

      //Hacemos do-while en caso de tener que repetir la selección de samples.
      do {
        f = new ArrayList<Pair>();
        samples = new HashMap<Integer, ArrayList<Pair>>();

        //k corresponde a la cantidad de puntos aleatorios a escoger para conformar la lista de samples
        int k = (int) Math.min(B, Math.ceil(n / B));
        ArrayList<Integer> outIndex = new ArrayList<>();

        //Se hace la selección de los índices de puntos que serán asigandos como samples
        //en un ciclo hasta haber escogido los k valores necesarios
        while (outIndex.size() < k) {
          int j = random.nextInt(n);
          Pair pt = cIn.get(j);

          //Se revisa que los puntos a insertar no sean repetidos
          if (!outIndex.contains(pt) && !f.contains(pt)) {
            outIndex.add(j);
            f.add(pt);

            int index = f.indexOf(pt);
            ArrayList<Pair> values = new ArrayList<>();
            // Se añade sólo 1 punto por indice elegido
            values.add(pt);
            samples.put(index, values);
          }
        }

        //Asignamos un sample a cada punto de cIn
        for (int i = 0; i < n; i++) {
          // hay k elementos en f y k en samples
          samples = asignSample(cIn.get(i), f, samples);
        }

        Iterator<Pair> iterator = f.iterator();

        //Revisamos si hay algun sample de tamaño menor a b
        while (iterator.hasNext()) {
          Pair currentPoint = iterator.next();
          int i = f.indexOf(currentPoint);
          ArrayList<Pair> points = new ArrayList<>(samples.get(i));

          //En caso de cumplir la condicion, el sample se vuelve un punto corriente y se reasigna junto con sus hijos.
          if (points.size() < b) {
            iterator.remove();

            if (!(i == samples.size()-1)) {
              samples = updateHash(samples, i);
            } else {
              samples.remove(i);
            }

            //Este sample pasa a ser un punto 'normal' por lo que lo agregamos a la lista de puntos por reasignar
            //después de eliminarlo de la lista de samples
            points.add(currentPoint);

            // Cada punto del sample eliminado es reasignado al siguiente más cercano
            for (Pair point : points) {
              samples = asignSample(point, f, samples);
            }
          }
        }
        //En caso de que el tamaño de F sea 1, repetimos el ciclo
      } while (f.size() == 1);

      //Creamos una lista para ir almacenando los árboles entregados por cp
      ArrayList<NodeCP> trees = new ArrayList<NodeCP>();

      Iterator<Pair> iterator = f.iterator();

      while (iterator.hasNext()) {
        Pair currentPoint = iterator.next();
        int i = f.indexOf(currentPoint);

        NodeCP a = cp(samples.get(i));
        a.setSample(f.get(i));

        if (a.size() < b) {
          //Si el tamaño del nodo es menor a b, se trabaja con sus subárboles como nuevos árboles
          iterator.remove();

          if (!(i == samples.size()-1)) {
            samples = updateHash(samples, i);
          } else {
            samples.remove(i);
          }

          ArrayList<TupleCP> childs = a.getEntries();
          for (int j = 0; j < childs.size(); j++) {

            TupleCP child = childs.get(j); // Pasa de ser tupla a nodo

            trees.add(child.getA()); // se añaden los subarboles como arboles a T (trees)
          }
        } else {
          //Si no, se agrega el árbol directamente
          trees.add(a); // se añaden los subarboles como arboles a T (trees)
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
      ArrayList<NodeCP> treesCopy = new ArrayList<>(trees);
      int treesSize = trees.size();

      Iterator<NodeCP> it = trees.iterator();

      // Los subarboles en trees contienen solo una entry
      // Se eligen los que sirven y se añaden a t
      while (it.hasNext()) {
        NodeCP ti = it.next();

        if (ti.getH() == h) {
          t.add(ti);
        } else {
          int i = trees.indexOf(ti);

          it.remove();

          //Se saca el sample del conjunto
          f.remove(ti.getSample());

          if (!(i == samples.size() - 1)) {
            samples = updateHash(samples, i);
          } else {
            samples.remove(i);
          }
          // Se buscan los subarboles de altura h
          ArrayList<NodeCP> hTrees = getHtree(h, ti);
          // Se agregan sus respectivos samples a F
          for (NodeCP tree : hTrees) {
            f.add(tree.getSample());
            int index2 = f.indexOf(tree.getSample());
            samples.put(index2, new ArrayList<Pair>());
            t.add(tree);
          }
        }
      }
      //Hacemos cp sobre el conjunto de samples, obteniendo tSup
      tSup = cp(f);

      if (tSup.getEntries().size() == 0) {
        for (NodeCP tprima : t) {
          TupleCP tPrimaTuple = new TupleCP();
          tPrimaTuple.setSample(tprima.getSample());
          tPrimaTuple.setA(tprima);
          tSup.addChild(tPrimaTuple);
        }
      } else {
        //Para cada subárbol de altura h buscamos la hoja que corresponda a su sample
        //en el árbol tSup y lo agregamos como hijo, esto viene dado por la función getChildBySample
        for (NodeCP tprima : t) {
          tSup.getChildBySample(tprima);
        }
      }

      //Seteamos los radios cobertores
      tSup.initializeR();
      return tSup;
    }
  }
}


