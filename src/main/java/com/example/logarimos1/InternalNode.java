package com.example.logarimos1;

import java.util.ArrayList;

public class InternalNode extends Node {
    //VARIABLES
    /** Representa al medoide del nodo interno */
    private Pair g = null;
    /** El radio cobertor del nodo interno */
    private double r = 0;
    /** Dirección a la lista de elementos del nodo */
    final ArrayList<Pair> c = new ArrayList<Pair>();

    /** Retorna el punto medoide del nodo interno */
    public Pair getG() {
        return g;
    }

    /** Retorna el radio cobertor del nodo interno  */
    public double getR() {
        return r;
    }
}
