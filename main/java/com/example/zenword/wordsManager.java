// Proyecto creado por Gaizka Medina Gordo
package com.example.zenword;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

// Clase wordsManager donde inicializaremos los diferentes catálogos para llevar a cabo la realización
// del programa y donde ubicaremos los diferentes métodos que traten con las palabras (así como elegir
// una palabra de entre todas las del catálogo, elegir las palabras ocultas, verficar que una palabra
// es válida, etc.)
public class wordsManager {

    // Catálogo de palabras válidas (con y sin acento)
    // Mapping: Se utiliza un HashMap para permitir la búsqueda rápida de palabras válidas.
    private final HashMap<String, String> palabrasValidas = new HashMap<>();
    // Conjunto de palabras agrupadas por su longitud
    // Mapping: Se utiliza un HashMap con claves enteras (para la longitud) y valores que son
    // conjuntos de palabras (HashSet) para acceder rápidamente a las palabras según su longitud.
    private final HashMap <Integer, HashSet<String>> longitudPalabras = new HashMap<>();
    // Soluciones agrupadas por longitud
    // Mapping: Uso de UnsortedArrayMapping para permitir un acceso rápido al catálogo de soluciones
    private UnsortedArrayMapping<Integer, HashSet<String>> soluciones;
    // Palabras ocultas con su posición en la pantalla
    // Mapping: TreeMap para mantener las palabras ocultas ordenadas por su clave entera.
    private TreeMap<Integer, String> palabrasOcultas;
    // Soluciones encontradas
    // Conjunto: TreeSet para mantener las soluciones encontradas ordenadas y evitar duplicados.
    private TreeSet<String> solucionesEncontradas;
    // Letras disponibles en la palabra elegida
    // Mapping: Uso de UnsortedArrayMapping para almacenar las letras y sus cantidades, permitiendo
    // un acceso rápido.
    private UnsortedArrayMapping<Character, Integer> letrasDisponibles;
    // Palabras ocultas sin bonus
    // Mapping: TreeMap para mantener las palabras ordenadas y permitir una búsqueda rápida.
    private TreeMap<Integer, String> sinBonus;
    // Contador de palabras por longitud (de 3 a 7)
    private final int[] numLongitudes = new int[5];
    // Contador de soluciones por longitud (de 3 a 7)
    private int[] numSoluciones = new int[5];
    // Número de palabras ocultas
    private Integer numPalabrasOcultas = 0;
    // Palabras para obtener un bonus
    private Integer palabrasParaBonus = 0;
    // Longitud de la palabra elegida
    private int longitudPalabra;
    // Palabra elegida para la partida
    private String palabraElegida;
    // Método constructor
    public wordsManager(InputStream is){
        cargarPalabras(is);
    }

    // Método cargarPalabras que carga el catálogo de palabras válidas y el catálogo de longitudes
    private void cargarPalabras(InputStream is){

        // Inicialización de las diferentes longitudes posibles para las palabras (de 3 a 7 letras)
        for (int i = 3; i < 8; i++){
            longitudPalabras.put(i, new HashSet<>());
        }

        // Llamada a la clase wordsFileReader
        wordsFileReader fr = new wordsFileReader();

        // Llamamos a la lectura del achivo
        try {
            fr.lectura(is, palabrasValidas, longitudPalabras, numLongitudes);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // Método cargarPartida que inicializa los catálogos necesarios para jugar una nueva patida
    // También llama a los métodos seleccionPalabra y seleccionPalabrasOcultas
    public void cargarPartida(){
        // Inicialización de los catálogos
        soluciones = new UnsortedArrayMapping<>(5);
        palabrasOcultas = new TreeMap<>();
        solucionesEncontradas = new TreeSet<>();
        letrasDisponibles = new UnsortedArrayMapping<>(7);
        numSoluciones = new int[5];
        // Métodos
        seleccionPalabra();
        seleccionPalabrasOcultas();
        sinBonus =(TreeMap<Integer, String>) palabrasOcultas.clone();
    }

    // Método seleccionPalabra que genera una palabra elegida aleatoria de entre todas las palabras
    // válidas del catálogo de palabras.
    private void seleccionPalabra(){
        Random r = new Random();
        // Calcula aleatoriamente la longitud de la palabra (entre 3 y 7)
        longitudPalabra = r.nextInt(5) + 3;

        // Coge la longitud aleatoria del catálogo de longitudes mediante el iterador
        Iterator<String> it = longitudPalabras.get(longitudPalabra).iterator();

        // Genera un entero aleatorio que corresponde a la posición de la palabra en su catálogo
        // de palabras de una misma longitud
        int numPalabra = r.nextInt(numLongitudes[longitudPalabra - 3]);
        int i = 0;

        // Iteramos hasta llegar a la posición de la palabra elegida
        while (i <= numPalabra && it.hasNext()){
            palabraElegida = it.next();
            i++;
        }
    }

    // Método seleccionPalabrasOcultas que generará una lista de palabras ocultas a partir de la
    // palabra elegida (las palabras ocultas son las que pueden formarse a partir de la palabra
    // elegida)
    private void seleccionPalabrasOcultas(){
        int posicion = 3;
        int tam = numSoluciones[0];
        String palabraOculta = "";
        Iterator<String> itPalabrasOcultas;
        int x = 0;
        Random r = new Random();
        // Comparador para ordenar la lista de palabras primero por su longitud y luego por orden
        // alfabético
        Comparator<String> ordenAlfabetico = Comparator.naturalOrder();
        Comparator<String> c = Comparator.comparingInt(String::length).thenComparing(ordenAlfabetico);
        // Conjunto temporal para almacenar las palabras ocultas una vez están ordenadas y cumplen
        // con las condiciones de ser una palabra oculta. También se declara su iterador
        TreeSet<String> tmp = new TreeSet<>(c);


        // Inicializa el conjunto de soluciones
        for (int i = 3; i < 8; i++){
            soluciones.put(i, new HashSet<>());
        }

        // Inicializa el número de palabras ocultas y de palabras para bonus
        numPalabrasOcultas = 0;
        palabrasParaBonus = 0;
        // Iterador para leer las palabras de cada conjunto
        Iterator<Map.Entry<Integer, HashSet<String>>> it = longitudPalabras.entrySet().iterator();
        while (it.hasNext()){
            // Inicializa el iterador de palabras y la longitud de palabras que son solución
            Map.Entry<Integer, HashSet<String>> entrada = it.next();
            Iterator<String> itPalabras = entrada.getValue().iterator();
            HashSet<String> longitudPalabrasSoluciones = soluciones.get(entrada.getKey());

            // Verifica, una a una, si las palabras son soluciones de la palabra elegida
            while (itPalabras.hasNext()){
                String palabra = itPalabras.next();
                // Verificamos si la palabra actual puede ser formada a partir de la palabra elegida
                if (esPalabraSolucion(palabraElegida, palabra)){
                    // La añadimos al número de palabras con soluciones de su longitud
                    longitudPalabrasSoluciones.add(palabra);
                    // Aumentamos el número de soluciones
                    numSoluciones[entrada.getKey() -3]++;
                }
            }
        }
        tmp.add(palabraElegida);

        // Bucle iterando para cada longitud de la palabra hasta llegar a la longitud 4
        for( int i = (longitudPalabra - 1); i > 3; i--){
            // Verificamos que haya soluciones para esa longitud de palabra
            if (numSoluciones[i-3] > 0){
                int random =r.nextInt(numSoluciones[i-3]);
                // Asignamos valor al iterador de palabras ocultas
                itPalabrasOcultas = soluciones.get(i).iterator();
                // Bucle hasta llegar a la posición aleatoria
                while(random >=0 && itPalabrasOcultas.hasNext()){
                    palabraOculta = itPalabrasOcultas.next();
                    random--;
                }
                // Verificamos que la palabra sea válida (exista)
                if (!palabraOculta.isEmpty()){
                    tmp.add(palabraOculta);
                    posicion--;
                }
            }
        }

        // Comprobamos que hayan palabras de longitud 3 disponibles
        if (tam >= 0){
            // Inicializamos el contador
            int contadorPalabrasLong3 = 0;
            // Conjunto indicesUsados que se utilizará para asegurar que no se repitan la selección
            // de palabras
            HashSet<Integer> indicesUsados = new HashSet<>();
            while ( contadorPalabrasLong3 < tam && posicion >= 0){
                int random = r.nextInt(numSoluciones[0]);
                // Asignamos valor al iterador de palabras ocultas (en este caso de longitud 3)
                itPalabrasOcultas = soluciones.get(3).iterator();
                // Gracias a indicesUsados y su condición, evitamos que se repitan palabras debido
                // a la repetición de números aleatorios
                if (indicesUsados.add(random)){
                    while (itPalabrasOcultas.hasNext() && random >= 0){
                        palabraOculta = itPalabrasOcultas.next();
                        random--;
                    }
                    contadorPalabrasLong3++;
                    posicion--;
                    tmp.add(palabraOculta);
                }
            }
        }

        // Añadimos las palabras ocultas almacenadas en el conjunto temporal a sus
        // correspondientes catálogos (añadimos la palabra al catálogo de palabras ocultas,
        // aumentamos el número de palabras ocultas y aumentamos el número de palabras que
        // todavía presentan un bonus)
        Iterator<String> iteradorTemp = tmp.iterator();
        while (iteradorTemp.hasNext()){
            palabrasOcultas.put(x, iteradorTemp.next());
            numPalabrasOcultas++;
            palabrasParaBonus++;
            x++;
        }
        System.out.println(palabrasOcultas);
    }


    // Método esPalabraSolucion que, a partir de una primera palabra pasada por parámetro, se verifica
    // si una segunda palabra pasada por parámetro puede formarse a partir de la primera.
    // Devuelve true en caso de que pueda formarse y false en caso contrario
    private boolean esPalabraSolucion (String palabra1, String palabra2){

        letrasDisponibles = new UnsortedArrayMapping<>(palabra1.length());

        // Itera a través de cada letra de la primera palabra
        for (int i = 0; i < palabra1.length(); i++){
            // Por cada nueva letra, la agrega a letrasDisponibles
            Integer valor = letrasDisponibles.put(palabra1.charAt(i), 1);
            // Si la letra ya está agregada, aumenta su valor en 1
            if (valor != null){
                letrasDisponibles.put(palabra1.charAt(i), valor+1);
            }
        }

        // Itera a través de cada letra de la segunda palabra
        for (int i = 0; i < palabra2.length(); i++){
            // Por cada nueva letra, la intenta obtener a partir de letrasDisponibles
            Integer valor = letrasDisponibles.get(palabra2.charAt(i));
            // Si esa letra no se encuentra en la primera palabra o no hay más letras disponibles de
            // su cantidad, significa que la segunda palabra no puede formarse a partir de la primera
            if (valor == null || valor == 0){
                return false;
            }
            // Si la letra si se encuentra dentro de letras disponibles, se le resta 1 a
            // su cantidad total
            letrasDisponibles.put(palabra2.charAt(i), valor-1);
        }
        // Si después de todas las iteraciones no ha devuelto false, significa que la segunda palabra
        // puede formarse a partir de la primera
        return true;
    }

    // MÉTODOS GETTERS Y SETTERS
    public Integer getPalabrasParaBonus() {
        return palabrasParaBonus;
    }
    public TreeMap<Integer, String> getPalabrasOcultas(){
        return palabrasOcultas;
    }
    public TreeSet<String> getSolucionesEncontradas(){
        return solucionesEncontradas;
    }
    public String getPalabraElegida(){
        return palabraElegida;
    }
    public HashMap<String, String> getPalabrasValidas() {
        return palabrasValidas;
    }
    public int getNumPalabrasEncontradas(){
        int numEncontradas = 0;
        Iterator<String> it = solucionesEncontradas.iterator();
        while (it.hasNext()){
            numEncontradas++;
            it.next();
        }
        return numEncontradas;
    }
    public int getNumPosiblesSoluciones(){
        int numPosiblesSoluciones = 0;
        for(int longSolucion : numSoluciones){
            numPosiblesSoluciones += longSolucion;
        }
        return numPosiblesSoluciones;
    }
    public TreeMap<Integer, String> getSinBonus() {
        return sinBonus;
    }
    public void restarNumPalabrasOcultas(){
        numPalabrasOcultas--;
    }
    public void restarNumPalabrasParaBonus(){
        palabrasParaBonus--;
    }
    public UnsortedArrayMapping<Integer, HashSet<String>> getSoluciones() {
        return soluciones;
    }

}
