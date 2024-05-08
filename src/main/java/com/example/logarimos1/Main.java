package com.example.logarimos1;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;

public class Main {
  static final Writer write = new Writer();

  //Tamaño de B
  static final Sizeof so = new Sizeof();
  static final int tamanoB = (int) Math.ceil(4096 / so.sizeof(double.class));

  // Se arman inputs 2^10 -> 2^25 para construir M-Tree
  static final HashMap<Integer, ArrayList<Pair>> inputs = new HashMap<>();

  //Se arman listas para almacenar los árboles
  static final ArrayList<NodeSS> arbolesSS = new ArrayList<>();
  static final ArrayList<NodeCP> arbolesCP = new ArrayList<>();

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoB);
  static final CP cp = new CP(tamanoB);



  public static void generateTestingPairs(int limite) {
    if (inputs.size() == 0) {
      //Generamos los inputs
      for (int i = 10; i < limite + 1; i++) {
        double size = Math.pow(2, i);
        ArrayList<Pair> pairs = new ArrayList<>();

        for (int j = 0; j < size; j++) {
          double x = Math.random();
          double y = Math.random();

          pairs.add(new Pair(x, y));
        }
        //Los guardamos en un hash, la llave corresponde al tamaño correspondiente del input (2^i) y
        //su valor es la lista de puntos
        inputs.put(i, pairs);
      }
    }
  }

  public static void startingProcessSS(int potencia) {
    //Creamos los árboles con SS para el input dado
    NodeSS a = ss.ss(inputs.get(potencia));
    arbolesSS.add(a);
  }

  public static void startingProcessCP(int limite) {
    //Creamos los árboles con SS y CP para cada input y los agregamos a las listas correspondientes
    for(int i=0; i<limite; i++) {
      NodeCP a = cp.cp(inputs.get(i));
      arbolesCP.add(a);
    }
  }

  public static void main(String[] args) {
    // 11 a 26
    generateTestingPairs(12);

    // Creamos un árbol SS para el tamaño 2^10
    startingProcessSS(12);

    // Crear los arboles CP
    //if(!isInputReadyCP) {
      //startingProcessCP();
    //}

    // Realizar 100 búsquedas por algoritmo
    NodeSS a = arbolesSS.get(0);

    Result r = a.search(inputs.get(12).get(8), 0.02);
    ArrayList<Pair> points = r.getPoints();
    int memoryAccess = r.getAccessCount();

    write.write("resultadosSS.txt", Integer.toString(memoryAccess));
    for (Pair p: points) {
      write.write("resultadosSS.txt", p.toString());
    }
  }
}