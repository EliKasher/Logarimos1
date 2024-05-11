package com.example.logarimos1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
  public void write(String filename, String content) {
    try (FileWriter fw = new FileWriter(filename, true); // El 'true' es para añadir datos sin sobrescribir
         BufferedWriter bw = new BufferedWriter(fw)) {

      // Escribe el contenido en el archivo
      bw.write(content);
      bw.newLine(); // Añade una nueva línea

    } catch (IOException e) {
      System.err.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
    }
  }
}
