package com.example.logarimos1;
import java.io.*;
import java.util.ArrayList;

public class Main {
  static final Writer write = new Writer();

  //Tamaño de B
  static final Sizeof so = new Sizeof();
  static final int tamanoB = 4096 / so.sizeof(double.class);

  // Se arman inputs 2^10 -> 2^25 para construir M-Tree
  static final ArrayList<ArrayList<Pair>> inputs = new ArrayList<>();

  //Se arman listas para almacenar los árboles
  static final ArrayList<NodeSS> arbolesSS = new ArrayList<>();
  static final ArrayList<NodeCP> arbolesCP = new ArrayList<>();

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoB);
  static final CP cp = new CP(tamanoB);
  static boolean isInputReadySS = false;
  static boolean isInputReadyCP = false;


  public static void generateTestingPairs(int endingFor) {
    if (inputs.size() == 0) {
      //Generamos los inputs
      for (int i = 10; i < endingFor; i++) {
        double size = Math.pow(2, i);
        ArrayList<Pair> pairs = new ArrayList<>();

        for (int j = 0; j < i; j++) {
          double x = Math.random();
          double y = Math.random();

          pairs.add(new Pair(x, y));
        }
        inputs.add(pairs);
      }
    }
  }

  public static void startingProcessSS(int limite) {
    //Creamos los árboles con SS para cada input y los agregamos a las listas correspondientes
    for(int i=0; i<limite; i++) {
      NodeSS a = ss.ss(inputs.get(i));
      arbolesSS.add(a);
    }
    isInputReadySS = true;
  }

  public static void startingProcessCP(int limite) {
    //Creamos los árboles con SS y CP para cada input y los agregamos a las listas correspondientes
    for(int i=0; i<limite; i++) {
      NodeCP a = cp.cp(inputs.get(i));
      arbolesCP.add(a);
    }
    isInputReadyCP = true;
  }

  public static void main(String[] args) {
    // 11 a 26
    generateTestingPairs(11);

    // Crear los arboles SS
    if(!isInputReadySS) {
      startingProcessSS(1);
    }

    // Crear los arboles CP
    //if(!isInputReadyCP) {
      //startingProcessCP();
    //}

    // Realizar 100 búsquedas por algoritmo
    NodeSS a = arbolesSS.get(0);
    Result r = a.search(new Pair(0.1, 0.1), 0.02);
    ArrayList<Pair> points = r.getPoints();
    int memoryAccess = r.getAccessCount();

    write.write("resultadosSS.txt", Integer.toString(memoryAccess));
    for (Pair p: points) {
      write.write("resultadosSS.txt", p.toString());
    }
  }
}