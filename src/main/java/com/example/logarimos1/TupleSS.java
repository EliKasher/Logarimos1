package com.example.logarimos1;
import java.util.ArrayList;
/**
 *
 */

public class TupleSS {
    //VARIABLES
    /** Representa al medoide del nodo */
    private Pair g = null;
    /** El radio cobertor del nodo */
    private double r = 0;
    /** Dirección a los hijos del nodo */
    private NodeSS  c = null;

    //GETTERS Y SETTERS
    /**
     * Retorna una copia de los elementos del nodo
     */
    public NodeSS getA() { return c;}

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
    public void setA(NodeSS newC) {
      this.c = newC;
    }

}

