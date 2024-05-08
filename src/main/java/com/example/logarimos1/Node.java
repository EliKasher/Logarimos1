package com.example.logarimos1;

/**
 * Clase para representar un Nodo.
 */
public abstract class Node {

    /**
     * Clase abstracta para la búsqueda sobre un nodo.
     * @param pair Recibe un punto sobre el que buscar.
     * @param r Recibe un radio de búsqueda.
     * @return Un objeto Result, que contiene una lista de puntos encontrados y un numero de accesos a memoria.
     */
    public abstract Result search(Pair pair, double r);
}
