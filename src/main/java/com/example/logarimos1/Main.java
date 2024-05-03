package com.example.logarimos1;

import java.util.ArrayList;

public class Main {
  //Tamaño de B
  static final Sizeof so = new Sizeof();
  static final int tamanoB = 4096 / so.sizeof(double.class);

  // Se arman inputs 2^10 -> 2^25 para construir M-Tree
  static final ArrayList<ArrayList<Pair>> inputs = new ArrayList<>();

  //Se arman listas para almacenar los árboles
  static final ArrayList<ArrayList<NodeSS>> arbolesSS = new ArrayList<>();
  static final ArrayList<NodeCP> arbolesCP = new ArrayList<>();

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoB);
  static final CP cp = new CP(tamanoB);
  static boolean isInputReady = false;

  public static void generateTestingPairs() {
    //Generamos los inputs
    for (int i = 10; i<26; i++) {
      double size = Math.pow(2, i);
      ArrayList<Pair> pairs = new ArrayList<>();

      for(int j=0; j<i; j++) {
        double x = Math.random();
        double y = Math.random();

        pairs.add(new Pair(x, y));
      }
      inputs.add(pairs);
    }
  }

  public static void startingProcess() {
    //Creamos los inputs
    generateTestingPairs();

    //Creamos los árboles con SS y CP para cada input y los agregamos a las listas correspondientes
    for(int i=0; i<inputs.size(); i++) {
      ArrayList<NodeSS> a1 = ss.ss(inputs.get(i));
      arbolesSS.add(a1);

      NodeCP a2 = cp.cp(inputs.get(i));
      arbolesCP.add(a2);
    }
    isInputReady = true;
  }

  public static void main(String[] args) {
    if(!isInputReady) {
      startingProcess();
    }

    // Realizar 100 búsquedas por algoritmo

  }
}