package com.example.logarimos1;

/**
 * Clase que representa una tupla, una entrada del estilo (p, cr, a) que estará contenida en un nodo
 */
public class TupleSS {
    //VARIABLES
    /** Representa al punto representante de la tupla */
    private Pair g = null;

    /** El radio cobertor de la tupla */
    private double r = 0;

    /** Dirección al hijo de esta tupla */
    private NodeSS c = null;

    //GETTERS Y SETTERS
    /**
     * Retorna el nodo hijo
     */
    public NodeSS getA() { return c;}

    /** Retorna el punto asociado a la tupla */
    public Pair getG() {
      return g;
    }

    /** Retorna el radio cobertor de la tupla  */
    public double getR() {
      return r;
    }

    /** Setter del punto representante */
    public void setG(Pair newG) {
      this.g = newG;
    }

    /** Setter del radio cobertor */
    public void setR(double newR) {
      this.r = newR;
    }

    /** Setter del nodo hijo **/
    public void setA(NodeSS newC) {
      this.c = newC;
    }

}

