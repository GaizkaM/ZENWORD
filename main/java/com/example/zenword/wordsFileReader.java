// Proyecto creado por Gaizka Medina Gordo
package com.example.zenword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// Clase wordsFileReader que se encarga de la lectura del archivo y del almacenamiento de las
// palabras válidas en su catálogo
public class wordsFileReader {

    // Método lectura que se encargará de leer el archivo y de almacenar las palabras válidas en su
    // catálogo en sus dos formatos (con acentos y sin acentos)
    public void lectura (InputStream is,
                         HashMap<String, String> palabrasValidas,
                         HashMap<Integer, HashSet<String>> longitudPalabras,
                         int[] numLongitudes) throws IOException{

        // Lectura del archivo
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = r.readLine()) != null){
                // División de cada línea del archivo en la palabra con y sin acento
                String palabra = line.substring(0, line.indexOf(';'));
                String palabraSinAcento = line.substring(line.indexOf(';') + 1);
                // Guardamos la longitud de la palabra
                int longPalabra = palabra.length();

                // Si cumple con la condición de longitud, la almacenamos en el catálogo de palabras
                // válidas y actualizamos la cantidad de palabras que hay de esa longitud.
                if ( longPalabra >= 3 && longPalabra <= 7){
                    palabrasValidas.put(palabraSinAcento,palabra);
                    if (longitudPalabras.get(longPalabra).add(palabraSinAcento)){
                        numLongitudes[longPalabra - 3]++;
                    }
                }
            }

        }

    }
}
