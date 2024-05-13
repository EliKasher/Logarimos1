package com.example.logarimos1;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;


public class Main {
  //Objetos para poder leer los inputs y queries, además de escribir los resultados
  static final Writer write = new Writer();
  static final Reader reader = new Reader();

  //Array para guardar las queries
  static final ArrayList<Pair> querys = new ArrayList<Pair>();

  //Tamaño de B, para eso creamos una instancia del objeto sizeOf;
  static final Sizeof so = new Sizeof();
  static final int tamanoBSS = (int) Math.ceil(4096 / so.sizeof(new NodeSS().getClass()));
  static final int tamanoBCP = (int) Math.ceil(4096 / so.sizeof(new NodeCP().getClass()));

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoBSS);
  static final CP cp = new CP(tamanoBCP);

  /**
   * Genera 2^25 pares y los guarda en un archivo. Se usó para generar los inputs.
   */
  public static void generateTestingPairs() {
    double size = Math.pow(2, 25);
    ArrayList<Pair> pairs = new ArrayList<>();
    
    //Hacemos un for sobre el size y creamos un punto para luego escribirlo archivo
    //así por cada línea tenemos un punto distinto
    for (int j = 0; j < size; j++) {
        double x = Math.random();
        double y = Math.random();
        pairs.add(new Pair(x, y));
        write.write("inputs/points.txt", Double.toString(x) + "," + Double.toString(y));
    }
  }

  /**
   * Crea un árbol SS con el input entregado.
   * @param pairs Los pares con los que se construye el arbol.
   */
  public static NodeSS createTreeSS(ArrayList<Pair> pairs) {
    //Creamos el árbol SS para el input dado
    NodeSS a = ss.ss(pairs);
    return a;
  }

  /**
   * Crea un árbol CP con los inputs entregados.
   * @param pairs Los pares con los que se construye el arbol.
   */
  public static NodeCP createTreeCP(ArrayList<Pair> pairs) {
    //Creamos el árbol CP para el input dado
    NodeCP a = cp.cp(pairs);
    return a;
  }

  /**
   * Genera 100 puntos aleatorios y los escribe como x,y en un archivo de texto querys.txt
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
 * Hace las 100 querys sobre el árbol entregado. Escribe los resultados en un
 * archivo de texto de nombre entregado y lo deja en la carpeta results del directorio.
 * @param a árbol donde buscar
 * @param filename nombre del archivo donde se escribirán los resultados
 * @return El promedio de accesos a disco
 * */
  public static double hundredQueries(Node a, String filename) {
    //Variable para guardar la suma total de accesos
    int sum = 0;

    //Lista que va a ir guardando los accesos de memoria de cada query
    ArrayList<Integer> accesos = new ArrayList<>();

    //Realizamos las búsquedas
    for (int i = 0; i < querys.size(); i++) {
      Result r = a.search(querys.get(i), 0.02);
      int memoryAccess = r.getAccessCount();
      accesos.add(memoryAccess);
      sum += memoryAccess;
      write.write(filename, "Query " + Integer.toString(i));
      write.write(filename, "Accesos: "+Integer.toString(memoryAccess));
      write.write(filename, "+++++++++++++++++++++++++++++++++++++++++");
    }

    //Se calcula y escribe el promedio de los accesos de memoria
    int avg = sum/accesos.size();
    write.write(filename,"Accesos Promedio: " + avg);
    write.write(filename, "----------------------------------");
    double res_ds = 0;

    //Se calcula y escriben la varianza y la desviación estándar
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
   * Función para armar el input del tamaño espicificado a base del archivo points.
   * @param potencia potencia del input a generar
   * @return un array list de tamaño 2^potencia para testear
   */
  public static ArrayList<Pair> readInputBySize(int potencia) {
    int readUntil = (int) Math.pow(2,potencia);
    ArrayList<Pair> pairs = new ArrayList<Pair>();

    //Con la función read del reader se leen readUntil puntos
    reader.read("inputs/points.txt", pairs, readUntil);
    return pairs;
  }

  /**
   * Ejecuta los tests para generar los árboles desde 2^10 hasta 2^25 y realizar 100 queries
   * sobre cada uno.
   */
  public static void main(String[] args) {
      // Se lee el listado de querys del archivo 'querys.txt' para obtener los 100
      // puntos de testeo, se guardarán en la variable querys
      reader.read("querys/querys.txt", querys, 100);
      System.out.println("Querys listas");

      // Se generaron los inputs presentes en el archivo points.txt usando la siguiente
      // función:
      //generateTestingPairs();
      // Esto no se debe volver a ejecutar si es que ya se tiene el archivo points.txt, pues
      // el proceso es bastante largo y sería innecesario (por esto se deja comentado)

      // La variable potencia especificará el tamaño del input a generar y luego testear en
      // el árbol. Se puede repetir esta sección las veces que sean necesarias para
      // testear los árboles necesarios

      //-----TESTEO PARA INPUT DE TAMAÑO 2^potencia-----
      int potencia = 10;

      // Se genera el input de tamaño 2^potencia
      ArrayList<Pair> input = readInputBySize(potencia);
      System.out.println("Input generado correctamente");

      //Se generan los árboles CP y SS para el input correspondiente
      System.out.println("Inicio creación de árboles");
      NodeSS treeSS = createTreeSS(input);
      NodeCP treeCP = createTreeCP(input);
      System.out.println("Árboles creados correctamente");

      //Creamos los nombres de los archivos
      String filenameSS = "results/resultsSS" + input;
      String filenameCP = "results/resultsCP" + input;

      //Realizamos las búsquedas
      System.out.println("Inicio búsquedas");
      double dsTreeSS = hundredQueries(treeSS, filenameSS);
      double dsTreeCP = hundredQueries(treeCP, filenameCP);
      System.out.println("Búsquedas listas");
  }
}
