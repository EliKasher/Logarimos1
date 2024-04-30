package com.example.logarimos1;
import java.util.ArrayList;
/**
 *
 */
public class Node {
    //VARIABLES
    /** Representa al medoide del nodo */
    private Pair g = null;
    /** El radio cobertor del nodo */
    private double r = 0;
    /** Dirección a los hijos del nodo */
    private ArrayList<Node>  c = null;

    //GETTERS Y SETTERS
    /** Retorna una copia de los elementos del nodo*/
    public ArrayList<Node> getA() { return c;}

    /** Retorna el punto medoide del cluster */
    public Pair getG() {
      return g;
    }

    /** Retorna el radio cobertor del cluster  */
    public double getR() {
      return r;
    }

    /** Setter del medoide */
    public void setG(Pair newG) {
      this.g = newG;
    }

    /** Setter del radio cobertor */
    public void setR(double newR) {
      this.r = newR;
    }
    public void setA(ArrayList<Node> newC) {
      this.c = newC;
    }

    /**
     * Busca un nodo específico dentro de un M-Tree.
     * @return -1 si se encuentra, el número de ingresos a disco si lo encuentra.
     */
    public int search(Node mTree, Pair query) {
        // Comparar ingresos a disco
        // Buscar el nodo correspondiente
        // Cada vez que se ingresa a un nodo se suma 1

        return null;
    }
}

