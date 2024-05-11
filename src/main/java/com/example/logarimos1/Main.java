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
   * Genera los pares de puntos desde 2^10 hasta 2^25
   * y los guarda en un archivo de texto con su respectivo nombre
   *
   */
  public static void generateTestingPairs() {
    if (inputs.size() == 0) {
      //Generamos los inputs
      for (int i = 10; i < 26; i++) {
        double size = Math.pow(2, i);
        ArrayList<Pair> pairs = new ArrayList<>();

        for (int j = 0; j < size; j++) {
          double x = Math.random();
          double y = Math.random();
          pairs.add(new Pair(x, y));
          write.write("inputs/points"+Integer.toString(i)+".txt", Double.toString(x) + "," + Double.toString(y));
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
  public static void createTreeCP(int index) {
    //Creamos los árboles CP para cada input y los agregamos a las listas correspondientes
    NodeCP a = cp.cp(inputs.get(index));
    arbolesCP.add(a);
  }

  /**
   * Genera 200 puntos aleatorios y los escribe como x,y en un archivo de texto querys.txt
   */
  public static void random_point(int potencia) {
    double size = Math.pow(2, potencia);
    for (int j = 0; j < size; j++) {
      double x = Math.random();
      double y = Math.random();
      write.write("points/points"+Integer.toString(potencia)+".txt", Double.toString(x) + "," + Double.toString(y));
    }
  }

/**
 * Hace las 100 querys según un tipo de árbol
 * Escribe los resultados en un archivo de texto
 * @return El promedio de accesos a disco
 * */
  public static double hundredQueries(Node a, String filename) {
    //Variable para guardar la suma total de accesos
    int sum = 0;
    //Lista que contiene todos los accesos
    ArrayList<Integer> accesos = new ArrayList<>();
    for (int i = 0; i < querys.size(); i++) {
      Result r = a.search(querys.get(i), 0.02);
      int memoryAccess = r.getAccessCount();
      accesos.add(memoryAccess);
      sum += memoryAccess;
      write.write(filename, "Query " + Integer.toString(i));
      write.write(filename, "Accesos: "+Integer.toString(memoryAccess));
      write.write(filename, "+++++++++++++++++++++++++++++++++++++++++");
    }
    //Variable que almacena la media de accesos a disco
    int avg = sum/accesos.size();
    write.write(filename,"Accesos Promedio: " + avg);
    write.write(filename, "----------------------------------");
    double res_ds = 0;

    for (int i = 0; i < accesos.size(); i++) {
      res_ds += Math.pow(accesos.get(i) - avg, 2);
    }
    double varianza = res_ds / accesos.size();
    double desviacionEstandar = Math.sqrt(varianza);
    write.write(filename,"Desviación estándar: " + Double.toString(desviacionEstandar));
    write.write(filename, "----------------------------------");
    write.write(filename,"Varianza: " + Double.toString(varianza));
    write.write(filename, "----------------------------------");
    
    return res_ds;
  }

  /**
   * Ejecuta los tests para generar los árboles desde 2^10 hasta 2^25 y realizar 100 queries sobre cada uno.
   * @param args
   */
  public static void main(String[] args) {
    //Generamos los inputs solo una vez para testear con los mismos en ambos árboles,
    //los guardamos en los archivos dentro de la carpeta inputs

    //generateTestingPairs();
    System.out.println("Pares de testeo listos");
    //Leemos el listado de querys del archivo 'querys.txt' para obtener los 100 puntos de testeo, se guardarán
    //en la variable querys
    reader.read("querys/querys.txt", querys);
    System.out.println("Querys listas");
    for (int i = 10; i < 11; i++) {
      ArrayList<Pair> pairs = new ArrayList<Pair>();
      reader.read("inputs/points"+Integer.toString(i)+".txt", pairs);
      inputs.put(i, pairs);
    }
    System.out.println(inputs.get(10).get(0));

    /**
    //PARTE 1 Zamy
   //Generamos los árboles de largo de inputs 2^10 y 2^11
   createTreeSS(10);
   createTreeSS(11);

   //PARTE 2 Benja
   //Generamos los árboles de largo de inputs 2^12 y 2^13
   System.out.println("Inicio");
   createTreeSS(12);
   System.out.println("Primer arbol listo");
   createTreeSS(13);
   System.out.println("Segundo arbol listo");

   //PARTE 3 Eve
   //Generamos los árboles de largo de inputs 2^14 y 2^15
   createTreeSS(14);
   createTreeSS(15);
     
   //Hacemos las 100 queries para los árboles correspondientes, ubicados en el array arbolesSS
     NodeSS a1 = arbolesSS.get(0);
     NodeSS a2 = arbolesSS.get(1);
  */
    // Generamos arboles CP
    createTreeCP(10);
    NodeCP a1  = arbolesCP.get(0);

    System.out.println("Inicio Querys");
    double dsA1 = hundredQueries(a1, "results/resultsCP1.txt");
    System.out.println("Query Arbol 1 lista");
    //dsA2 = hundredQueries(a2, "results/resultsSS2.txt");
    System.out.println("Query Arbol 2 lista");
    // Desviación Estandar Sum((x-avgX)^2)/n, xAvg= promedio de la query, x = un punto de la query, n = 100

  }
}