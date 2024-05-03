package com.example.logarimos1;
import java.util.ArrayList;
/**
 *
 */

public class NodeSS {
    //VARIABLES
    /** Representa al medoide del nodo */
    private Pair g = null;
    /** El radio cobertor del nodo */
    private double r = 0;
    /** Dirección a los hijos del nodo */
    private ArrayList<NodeSS>  c = null;

    //GETTERS Y SETTERS
    /** Retorna una copia de los elementos del nodo*/
    public ArrayList<NodeSS> getA() { return c;}

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
    public void setA(ArrayList<NodeSS> newC) {
      this.c = newC;
    }

    /**
     * Busca los pares dentro de un radio de búsqueda r para un punto q, en un M-Tree.
     * @return null si no se encuentra, el array de pares coincidentes si los encuentra.
     */
    public Result search(NodeSS mTree, Pair q,  double r) {
        // Comparar ingresos a disco
        // Buscar el nodo correspondiente
        // Cada vez que se ingresa a un nodo se suma 1

        // Creamos la estructura para almacenar el resultado
        Result res = new Result();
        
        // Cuando el arbol es nulo
        if (mTree == null) {
            return null;
        }

        // Si el nodo es una hoja (nodo externo)
        if (mTree.getA() == null) {
            Pair p = mTree.getG();
            double d = q.dist(p);

            if (d <= r) {
                res.addPoint(p);
            }

            return res;
        } else {
            //Si el nodo es interno, significa que vamos a acceder a disco para obtener su infromación
            //por lo que aumentamos el contador del resultado
            res.memoryAccess(1);

            for(NodeSS child : mTree.getA()) {
                double d = q.dist(child.getG());

                //Si la distancia es menor al radio de búsqueda, se ingresa para buscar recursivamente
                if (d <= r + child.getR()) {
                    for (NodeSS childsChild : mTree.getA()) {
                        Result resultChild = search(childsChild,q,r);

                        //Se accede a los puntos encontrados por el hijo y se agregan al resultado general
                        for (Pair point : resultChild.getPoints()) {
                            res.addPoint(point);
                        }

                        //Se accede a la cantidad de accesos de memoria del hijo y se suman al resultado final
                        res.memoryAccess(resultChild.getAccessCount());
                    }
                }
            }

            return res;
        }
    }
}

