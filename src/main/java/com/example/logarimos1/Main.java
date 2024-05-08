package com.example.logarimos1;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;


public class Main {
  static final Writer write = new Writer();
  static final Reader reader = new Reader();
  static final ArrayList<Pair> querys = new ArrayList<Pair>();

  //Tamaño de B, para eso creamos instancia de un objeto sizeOf;
  static final Sizeof so = new Sizeof();
  static final int tamanoBSS = (int) Math.ceil(4096 / so.sizeof(new NodeSS().getClass()));
  static final int tamanoBCP = (int) Math.ceil(4096 / so.sizeof(new NodeCP().getClass()));

  // Se arman inputs 2^10 -> 2^25 para construir M-Tree
  static final HashMap<Integer, ArrayList<Pair>> inputs = new HashMap<>();

  //Se arman listas para almacenar los árboles
  static final ArrayList<NodeSS> arbolesSS = new ArrayList<>();
  static final ArrayList<NodeCP> arbolesCP = new ArrayList<>();

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoBSS);
  static final CP cp = new CP(tamanoBCP);

  /**
   * Genera los pares de puntos desde 2^10 hasta 2^limite
   * y los guarda en un hash de inputs globales, con su potencia como llave.
   * @param limite es la potencia limite - 1 que se quiere lograr.
   */
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

  /**
   * Crea un árbol SS con los inputs de 2^potencia entregada.
   * @param potencia es la llave que se guarda en generateTestingPairs.
   */
  public static void createTreeSS(int potencia) {
    //Creamos los árboles con SS para el input dado
    NodeSS a = ss.ss(inputs.get(potencia));
    arbolesSS.add(a);
  }

  /**
   * Crea un árbol CP con los inputs de 2^potencia entregada.
   * @param limite
   */
  public static void createTreeCP(int limite) {
    //Creamos los árboles con SS y CP para cada input y los agregamos a las listas correspondientes
    for(int i=0; i<limite; i++) {
      NodeCP a = cp.cp(inputs.get(i));
      arbolesCP.add(a);
    }
  }

  /**
   * Genera 200 puntos aleatorios y los escribe como x,y en un archivo de texto querys.txt
   */
  public static void random_point() {
    for (int j = 0; j < 100; j++) {
      double x = Math.random();
      double y = Math.random();
      write.write("querys.txt", Double.toString(x) + "," + Double.toString(y));
    }
  }

/**
 * Hace las 100 querys según un tipo de árbol
 * Escribe los resultados en un archivo de texto
 * @return El promedio de accesos a disco
 * */
  public static double hundredQueries(Node a, String filename) {
    int sum = 0;
    ArrayList<Integer> accesos = new ArrayList<>();
    for (int i = 0; i < querys.size(); i++) {
      Result r = a.search(querys.get(i), 0.02);
      ArrayList<Pair> points = r.getPoints();
      int memoryAccess = r.getAccessCount();
      accesos.add(memoryAccess);
      sum+=memoryAccess;
      write.write(filename, "Query " + Integer.toString(i));
      write.write(filename, "Accesos: "+Integer.toString(memoryAccess));
      write.write(filename, "+++++++++++++++++++++++++++++++++++++++++");
    }
    //Variable que almacena la media de accesos a disco
    int avg = sum/100;
    write.write(filename,"Accesos Promedio: " + avg);
    write.write(filename, "----------------------------------");
    //Variable que almacenará la desviación estándar de accesos a disco
    double res_ds = 0;

    for(int i=0; i<accesos.size(); i++) {
      res_ds =+ Math.pow(accesos.get(i)-avg, 2);
    }
    res_ds = res_ds/100;
    write.write(filename,"Desviación estándar: " + res_ds);
    write.write(filename, "----------------------------------");
    
    return res_ds;
  }

  /**
   * Ejecuta los tests para generar los árboles desde 2^10 hasta 2^25 y realizar 100 queries sobre cada uno.
   * @param args
   */
  public static void main(String[] args) {
    //ALGORITMO SS TESTEO

    //Generamos los inputs, que se guardan en el hash map inputs
    generateTestingPairs(15);

    //Leemos el listado de querys del archivo 'querys.txt' para obtener los 100 puntos de testeo, se guardarán
    //en la variable
    reader.read("querys.txt", querys);

    /**
    //PARTE 1 Zamy
    //Generamos los árboles de largo de inputs 2^10 y 2^11
    createTreeSS(10);
    createTreeSS(11);

    //PARTE 2 Benja
    //Generamos los árboles de largo de inputs 2^12 y 2^13
    createTreeSS(12);
    createTreeSS(13);

    //PARTE 3 Eve
    //Generamos los árboles de largo de inputs 2^14 y 2^15
    createTreeSS(14);
    createTreeSS(15);
    */

    //Hacemos las 100 queries para los árboles correspondientes, ubicados en el array arbolesSS
    NodeSS a1 = arbolesSS.get(0);
    NodeSS a2 = arbolesSS.get(1);

    double dsA1 = hundredQueries(a1, "resultsSS1.txt");
    double dsA2 = hundredQueries(a2, "resultsSS2.txt");

    // Desviación Estandar Sum((x-avgX)^2)/n, xAvg= promedio de la query, x = un punto de la query, n = 100
  }
}