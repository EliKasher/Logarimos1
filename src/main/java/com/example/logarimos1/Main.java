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

  //Tamaño de B, para eso creamos instancia de un objeto sizeOf;
  static final Sizeof so = new Sizeof();
  static final int tamanoBSS = (int) Math.ceil(4096 / so.sizeof(new NodeSS().getClass()));
  static final int tamanoBCP = (int) Math.ceil(4096 / so.sizeof(new NodeCP().getClass()));

  //Se crean objetos para llamar a los constructores de los árboles
  static final SS ss = new SS(tamanoBSS);
  static final CP cp = new CP(tamanoBCP);

  /**
   * Genera 2^25 pares y los guarda en un archivo. Se usará
   * para conseguir los distintos inputs.
   */
  public static void generateTestingPairs() {
    double size = Math.pow(2, 25);
    ArrayList<Pair> pairs = new ArrayList<>();
    
    //Hacemos un for sobre el size y creamos un punto para luego escribirlo archivo
    //así por cada línea tenemos unn punto distinto
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
   * FUnción para ramar el input
   * @param potencia
   * @return un array list de tamaño 2^potencia para testear
   */
  public static ArrayList<Pair> readInputBySize(int potencia){
    int readUntil = (int) Math.pow(2,potencia);
    ArrayList<Pair> pairs = new ArrayList<Pair>();
    reader.read("inputs/points.txt", pairs, readUntil);
    return pairs;
  }

  /**
   * Ejecuta los tests para generar los árboles desde 2^10 hasta 2^25 y realizar 100 queries sobre cada uno.
   * @param args
   */
  public static void main(String[] args) {
    //Generamos los inputs solo una vez para testear con los mismos en ambos árboles,
    //los guardamos en los archivos dentro de la carpeta inputs
    //generateTestingPairs();
    //System.out.println("Pares de testeo listos");

    //Leemos el listado de querys del archivo 'querys.txt' para obtener los 100 puntos de testeo, se guardarán
    //en la variable querys
    reader.read("querys/querys.txt", querys, 100);
    System.out.println("Querys listas");
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

    //Generamos el árbol CP
    ArrayList<Pair> input10 = readInputBySize(10);
    NodeCP a1 = createTreeCP(input10);

    System.out.println("Inicio Querys");
    double dsA1 = hundredQueries(a1, "results/resultsCP1.txt");
    System.out.println("Query Arbol 1 lista");
  }
}