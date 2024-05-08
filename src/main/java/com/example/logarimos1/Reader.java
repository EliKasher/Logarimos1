package com.example.logarimos1;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {
    public void read(String filename, ArrayList<Pair> list) {
        File fichero = new File(filename);
        Scanner s = null;
        
        try {
            // Leemos el contenido del fichero
            s = new Scanner(fichero);

            // Leemos linea a linea el fichero
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                String[] nums = linea.split(",");    // Imprimimos la linea
                Pair point = new Pair(Double.parseDouble(nums[0]), Double.parseDouble(nums[1]));
                list.add(point);
            }
        } catch (Exception e) {
            System.out.println("Mensaje: " + e.getMessage());
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null)
                    s.close();
            } catch (Exception e2) {
                System.out.println("Mensaje 2: " + e2.getMessage());
            }
        }
    }
}

