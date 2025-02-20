// Proyecto creado por Gaizka Medina Gordo
package com.example.zenword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    // Matriz de dos dimensiones que representa las diferentes letras de las palabras ocultas a
    // resolver. La primera dimensión representa las palabras (cada fila es una palabra) y la
    // segunda representa las letras (que a su vez se reptesentan como TextView)
    private int[][] matrizLetras;
    // Array de String con los colores disponibles para una partida
    private final String[] colores = {"AZUL", "VERDE", "ROSA"};
    // Puntos del bonus
    private int puntosBonus = 0;
    // Cantidad de puntos para poder utilizar el bonus
    private final int puntosNecesariosBonus = 5;
    // Drawable fono letra que se instanciará en función del color elegido
    private Drawable fondoLetra;
    // Ancho de la pantalla donde se llevará a cabo la partida
    private int anchoDisplay;
    // Array de botones con los 7 botones de letras ubicados dentro del círculo
    private final int[] idBotones = {R.id.botonLet0, R.id.botonLet1, R.id.botonLet2, R.id.botonLet3,
            R.id.botonLet4, R.id.botonLet5, R.id.botonLet6};
    // Array de guías donde se ubicarán las diferentes palabras ocultas
    private final int[] idGuias = {R.id.guidelinePalabra1, R.id.guidelinePalabra2, R.id.guidelinePalabra3,
            R.id.guidelinePalabra4, R.id.guidelinePalabra5};
    // Mapping para los diferentes colores de la partida. Como clave presenta el nombre del color y
    // como valor tiene el Array de objetos Drawable
    private final UnsortedArrayMapping<String, Drawable[]> coloresDrawable = new UnsortedArrayMapping<>(3);
    // Administrador de palabras proveniente de la clase wordsManager que será llamado para cualquier
    // uso de los catálogos que necesitemos en la creación del juego
    private wordsManager administradorPalabras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inicialización colores de los objetos de la interfaz
        inicializarColoresInterfaz();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicialización ancho de la pantalla
        DisplayMetrics metricas = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metricas);
        anchoDisplay = metricas.widthPixels;
        administradorPalabras = new wordsManager(getResources().openRawResource(R.raw.paraules));
        // Iniciamos una nueva partida
        nuevaPartida(null);
    }

    // Método setColorInterfaz que establece el color del circulo y de los fondos de las letras de
    // las palabras ocultas en función del color pasado por parámetro
    private void setColorInterfaz(String color) {
        // Inicializa el circulo de nuestra interfaz y le añade el color pasado por parámetro
        ImageView circulo = findViewById(R.id.circulo);
        circulo.setImageDrawable(coloresDrawable.get(color)[0]);
        // Añade el color al fondo de las letras
        fondoLetra = coloresDrawable.get(color)[1];
    }

    // Método inicializarInterfaz que inicializa los diferentes colores que podrán tener los
    // objetos Drawables de nuestro juego
    private void inicializarColoresInterfaz() {
        coloresDrawable.put("AZUL", new Drawable[]{
                    AppCompatResources.getDrawable(this, R.drawable.circulo_azul),
                    AppCompatResources.getDrawable(this, R.drawable.cuadrado_azul)
        });
        coloresDrawable.put("VERDE", new Drawable[]{
                AppCompatResources.getDrawable(this, R.drawable.circulo_verde),
                AppCompatResources.getDrawable(this, R.drawable.cuadrado_verde)
        });
        coloresDrawable.put("ROSA", new Drawable[]{
                AppCompatResources.getDrawable(this, R.drawable.circulo_rosa),
                AppCompatResources.getDrawable(this, R.drawable.cuadrado_rosa)
        });
    }

    // Método setLetrasCirculo que se encargará de inicializar los botonesLetras dentro del circulo
    // Este método se utiliza cada vez que se comience una nueva partida
    private void setLetrasCirculo() {
        // Pone los botones visibles
        for(int i : idBotones){
            Button boton = findViewById(i);
            boton.setVisibility(View.VISIBLE);
        }
        int i = 0;
        String palabraElegida = administradorPalabras.getPalabraElegida().toUpperCase();
        // Por cada letra de la palabra elegida, se actualiza el texto del botón y se le activa el
        // método setLetra de su función OnClick.
        for(i = 0; i < administradorPalabras.getPalabraElegida().length(); i++){
            Button botonLetra = findViewById(idBotones[i]);
            botonLetra.setOnClickListener(e -> setLetra(botonLetra));
            // Declaramos una letra de la palabra elegida y la asignamos al texto del boton
            //char [] letraPalabraElegida = new char[]{administradorPalabras.getPalabraElegida().charAt(i)};
            char letraPalabraElegida = Character.toUpperCase(palabraElegida.charAt(i));
            System.out.println(letraPalabraElegida);
            //botonLetra.setText(letraPalabraElegida,0,1);
            botonLetra.setText(String.valueOf(letraPalabraElegida));
        }
        // Los botones no utilizados (debido a que la palabra elegida no siempre tiene 7 letras) dejan
        // de ser visibiles
        for(; i < 7;i++){
            Button botonNoUsado = findViewById(idBotones[i]);
            botonNoUsado.setVisibility(View.GONE);
        }
    }

    // Método borrarPalabra para la función OnClick del botón clear. Borra la palabra contenida en
    // el TextView palabraEntrada y resetea el estado de los botones
    public void borrarPalabra(View view) {
        // Resetear los botones
        for(int i=0; i < administradorPalabras.getPalabraElegida().length(); i++){
            Button btn = findViewById(idBotones[i]);
            btn.setEnabled(true);
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        // Borra la palabra
        TextView palabraEntrada = findViewById(R.id.palabraEntrada);
        palabraEntrada.setText("");

    }

    // Método setLletra para la función OnClick de los diferentes botones de letras que se encuentran
    //dentro del circulo.
    public void setLetra(View view) {
        // Recupera el botón que ha desencadenado la llamada
        Button btn = (Button) view;
        // Obtiene el texto del botón (la letra)
        String letra = btn.getText().toString();
        // Añade la letra a la entrada actual (palabra)
        TextView entradaActual = findViewById(R.id.palabraEntrada);
        String palabra = entradaActual.getText().toString();
        palabra += letra;
        // Añade la entrada actualizada al TextView
        entradaActual.setText(palabra.toUpperCase());

        // Desactiva el botón y cambia su color para indicar que ya no se puede pulsar
        btn.setEnabled(false);
        btn.setTextColor(ContextCompat.getColor(this,R.color.light_gray));
    }

    // Método shuffleLetras para la función OnClick del botón random. Reordena las letras visibles
    // del circulo.
    public void shuffleLetras(View view) {
        // Borra la palabra de la entrada mediante el mismo método que utilizamos para el botón clear
        borrarPalabra(null);
        char[] palabra = administradorPalabras.getPalabraElegida().toCharArray();
        // Fisher-Yates shuffle para mezclar las letras de la palabra elegida
        Random r = new Random();
        for (int i = 0; i < palabra.length; i++) {
            int j = r.nextInt(palabra.length);
            // Intercambiar los botones en las posiciones i y j
            char temp = palabra[i];
            palabra[i] = palabra[j];
            palabra[j] = temp;
        }
        // Pone la palabra con sus letras mezcladas en los diferentes botones del circulo
        for(int i = 0; i < palabra.length; i++){
            Button botonLetra = findViewById(idBotones[i]);
            botonLetra.setText(palabra,i,1);
        }

    }

    //Método crearFilaTextViews que genera una fila de TextViews para una palabra oculta
    public void crearFilaTextViews (int guia, int letras, int fila) {
        ConstraintLayout main = findViewById(R.id.main);
        TextView[] filaTextViews = new TextView[letras];
        // Bucle donde creamos cada uno de los TextView por cada letra que tiene la palabra
        for(int i=0; i<letras; i++){

            int id = View.generateViewId();
            TextView textview = new TextView(this);

            matrizLetras[fila][i] = id;
            // Atributos necesarios para el TextView
            textview.setId(id);
            textview.setText("");
            textview.setBackground(fondoLetra);
            textview.setTextSize(26);
            textview.setTextColor(Color.WHITE);
            textview.setTypeface(null,Typeface.BOLD);
            textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            filaTextViews[i] = textview;
            // Lo añadimos al Layout
            main.addView(textview);
        }

        // Calculamos la separacion entre los botones
        // Divide la pantalla en 7 (máximo número de letras) y le resta un 0.05 de la pantalla
        // para la separación entre letras
        int ancho = (anchoDisplay / 7) - (int) (0.05 * anchoDisplay);
        // Bucle donde añadimos las restricciones necesarias a cada TextView
        ConstraintSet constraintSet = new ConstraintSet();
        for (int i=0; i<letras; i++){

            // Restricción inferior. Se conectará la parte inferior del TextView con la parte
            //superior de la guía
            constraintSet.connect(
                    filaTextViews[i].getId(),
                    ConstraintSet.BOTTOM,
                    guia,
                    ConstraintSet.TOP,
                    5);

            // Restricción para el inicio de la primera letra
            if (i ==0) {

                // Si es la primera letra, se conectará el inicio de esta al borde del padre
                constraintSet.connect(
                        filaTextViews[i].getId(),
                        ConstraintSet.START,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.END,
                        10);
            } else {

                // Si no es la primera letra, se conectará el inicio de esta al final de la letra
                // anterior (i-1)
                constraintSet.connect(
                        filaTextViews[i].getId(),
                        ConstraintSet.START,
                        filaTextViews[i-1].getId(),
                        ConstraintSet.END,
                        5);
            }

            // Restricción para el final de la última letra
            if (i == letras -1) {

                // Si es la última letra, se conectará el final de esta al borde del padre
                constraintSet.connect(
                        filaTextViews[i].getId(),
                        ConstraintSet.END,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.START,
                        10);
            } else {

                // Si no es la última letra, se conectará el final de esta al inicio de la siguiente
                // letra (i+1)
                constraintSet.connect(
                        filaTextViews[i].getId(),
                        ConstraintSet.END,
                        filaTextViews[i+1].getId(),
                        ConstraintSet.START,
                        5);
            }
            // Se añaden los TextView al Layout
            constraintSet.constrainWidth(filaTextViews[i].getId(), ancho);
            constraintSet.constrainHeight(filaTextViews[i].getId(), ancho);
            constraintSet.applyTo(main);
        }


    }

    // Método borrarTextViews que borra los TextViews de las letras de las palabras ocultas para
    // inicializar una nueva partida.
    private void borrarTextViews() {
        ConstraintLayout main = findViewById(R.id.main);
        for (int[] i : matrizLetras){
            for (int j : i){
                main.removeView(findViewById(j));
            }
        }
    }

    // Método muestraPalabra que muestra la palabra contenida en el String s de la línea correspondiente
    // al entero posición.
    private void muestraPalabra(String s, int posicion) {
        // Bucle para recorrer todas las letras de la palabra
        for (int i=0; i < matrizLetras[posicion].length; i++){
            // Por cada letra de la palabra, mostramos su texto con setText (en mayúscula)
            TextView t = findViewById(matrizLetras[posicion][i]);
            System.out.println(matrizLetras[posicion][i]);
            //char letraActual = Character.toUpperCase(s.charAt(i));
            //t.setText(letraActual);
            t.setText(String.format("%s", s.charAt(i)).toUpperCase());
        }
    }

    // Método muestraPrimeraLetra que muestra solamente la primera letra de la palabra contenida
    // en el String s de la línea correspondiente al entero posición.
    private void muestraPrimeraLetra(String s, int posicion) {
        // Únicamente mostramos el texto de la primera letra (columna 0 de la fila indicada por
        // parámetro) en minúscula
        TextView t = findViewById(matrizLetras[posicion][0]);
        //char primeraLetra = Character.toLowerCase(s.charAt(0));
        //t.setText(primeraLetra);
        t.setText(String.format("%s", s.charAt(0)).toLowerCase());
    }

    // Método muestraMensaje que muestra el mensaje contenido en el String s mediante un Toast. La
    // variable booleana indica si el mensaje es largo o corto.
    private void muestraMensaje(String s, boolean largo) {
        // Parámetros del mensaje
        Context contexto = getApplicationContext();
        CharSequence texto = s;
        int duracion;
        if (largo){
            duracion = Toast.LENGTH_LONG;
        } else {
            duracion = Toast.LENGTH_SHORT;
        }

        // Crea el objeto Toast y lo muestra
        Toast toast = Toast.makeText(contexto, texto, duracion);
        toast.show();
    }

    // Método habilitarViews que, a partir del identificador de un elemento de la pantalla (padre),
    // debe habilitar todos sus componentes.
    // Se utilizará cuando se vuelva a comenzar una partida.
    private void habilitarViews(int padre) {
        ViewGroup elementoPadre = findViewById(padre);

        for (int i = 0; i < elementoPadre.getChildCount(); i++){
            View hijo = elementoPadre.getChildAt(i);
            hijo.setEnabled(true);
        }
    }

    // Método deshabilitarViews que, a partir del identificador de un elemento de la pantalla (padre),
    // debe deshabilitar todos sus componentes (excepto los botones Bonus y Reiniciar).
    // Se utilizará cuando se acabe el juego.
    private void deshabilitarViews(int padre) {
        ViewGroup elementoPadre = findViewById(padre);

        for (int i = 0; i < elementoPadre.getChildCount(); i++){
            View hijo = elementoPadre.getChildAt(i);
            // Comprobamos que el elemento hijo no es ni el botón de bonus ni el botón de reiniciar
            if ((hijo.getId() != R.id.botonBonus) && (hijo.getId() != R.id.botonReiniciar)){
                hijo.setEnabled(false);
            }
        }
    }

    // Método enviar para la función OnClick del botón send.
    // Borra la palabra introducida y vuelve a activar las letras
    // Si la palabra pertenece a las palabras ocultas, muestra la palabra en su línea, la añade a
    // las palabras ocultas, lo comunica al usuario y la elimina de las palabras por descubrir
    // Si es una solución posible, la añade a las palabras encontradas hasta este momento y lo
    // comunica al usuario.
    // Si ya había estado introducida, lo notifica y marca esa palabra en rojo.
    // Si no es válida, lo notifica al usuario.
    // Por último, si se han descubierto todas las palabras ocultas, se termina el juego,
    // notificándoselo al jugador.
    public void enviar(View v) {
        // Declaramos el iterador de palabraas ocultas que utilizaremos dentro del método
        Iterator<Map.Entry<Integer, String>> itPalabrasOcultas =
                administradorPalabras.getPalabrasOcultas().entrySet().iterator();
        // Lee la palabra almacenada en el TextView palabraEntrada
        TextView textView = findViewById(R.id.palabraEntrada);
        String palabra = textView.getText().toString().toLowerCase();

        boolean palabraEncontrada = false;
        // Borra la palabra de la entrada mediante el mismo método que utilizamos para el botón clear
        borrarPalabra(null);
        if (palabra.isEmpty()){
            return;
        } else if (palabra.length() >= 3){
            // Revisa si se encuentra en el catálogo de palabras ocultas
            while (itPalabrasOcultas.hasNext()){
                Map.Entry<Integer, String> entrada = itPalabrasOcultas.next();
                // Comprueba si la palabra pertenece al conunto de palabras ocultas
                // Si pertenece, actualizamos los catálogos de soluciones encontradas y salimos del
                // bucle
                if (entrada.getValue().equals(palabra)){
                    // Muestra la palabra y el mensaje
                    muestraPalabra(administradorPalabras.getPalabrasValidas().get(palabra), entrada.getKey());
                    muestraMensaje("Encertada!",false);
                    // Añade la palabra al catálogo de palabras encontradas
                    administradorPalabras.getSolucionesEncontradas().add(palabra);
                    // Actualiza la lista de palabras que son solución
                    actualizarListaPalabrasSoluciones(null);
                    // Borra la palabra del iterador
                    itPalabrasOcultas.remove();
                    // Elimina la palabra oculta del conjunto
                    administradorPalabras.getSinBonus().remove(entrada.getKey());
                    // Actualiza el número de palabras ocultas restantes y número de palabras para bonus
                    administradorPalabras.restarNumPalabrasOcultas();
                    administradorPalabras.restarNumPalabrasParaBonus();
                    palabraEncontrada = true;
                    break;
                }
            }
            // Si no pertenece al conjunto de palabras ocultas, verificamos si es una posible solución
            if (!palabraEncontrada){
                HashSet<String> solucion = administradorPalabras.getSoluciones().get(palabra.length());
                // Comprueba si la palabra pertenece al catálogo de soluciones
                if (solucion.contains(palabra)){
                    // Comprueba si la palbra no se había puesto antes
                    if (!administradorPalabras.getSolucionesEncontradas().contains(palabra)) {
                        // Añade la palabra al catálogo
                        administradorPalabras.getSolucionesEncontradas().add(palabra);
                        // Actualiza la lista de palabras que son solución
                        actualizarListaPalabrasSoluciones(null);
                        // Muestra el mensaje correspondiente
                        muestraMensaje("Paraula vàlida! Tens un bonus", false);
                        // Suma un punto del bonus
                        puntosBonus++;
                        // Actualiza el valor del botón bonus
                        Button puntosBotonBonus = findViewById(R.id.botonBonus);
                        puntosBotonBonus.setText(String.valueOf(puntosBonus));
                    // La palabra se había puesto antes
                    } else {
                        // Muestra el mensaje correspondiente
                        muestraMensaje("Aquesta ja la tens",false);
                        // Actualiza la lista con la palabra pasada por parámetro (la mostrará en rojo)
                        actualizarListaPalabrasSoluciones(administradorPalabras.getPalabrasValidas().get(palabra));
                    }
                    palabraEncontrada = true;
                }
            }
        }
        // No es una posible solución
        if (!palabraEncontrada){
            muestraMensaje("Paraula no vàlida",false);
            return;
        }

        // Si no quedan más palabras ocultas, la partida finaliza
        if (administradorPalabras.getPalabrasOcultas().isEmpty()){
            muestraMensaje("Enhorabona! has guanyat.",true);
            deshabilitarViews(R.id.main);
        }
    }

    // Método actualizarListaPalabrasSoluciones que, dada una palabra pasada por parámetro, la añade
    // al TextView soluciones. También trata el caso en el que la palabra ya se encuentre en la lista
    // y haya que marcarla de color rojo
    public void actualizarListaPalabrasSoluciones(String palabra) {
        TextView soluciones = findViewById(R.id.soluciones);
        StringBuilder listaPalabrasSoluciones = new StringBuilder();
        Iterator<String> itSoluciones = administradorPalabras.getSolucionesEncontradas().iterator();
        // Recorrido sobre las palabras solución
        while (itSoluciones.hasNext()){
            String palabraActual = administradorPalabras.getPalabrasValidas().get(itSoluciones.next());
            // Si la palabra ya se encontraba en la lista, habrá que ponerla en rojo
            if (Objects.equals(palabra, palabraActual)){
                palabraActual = ("<font color='red'>"+palabraActual+"</font>");
            }
            // Si la lista está vacía, simplemente se añade la palabra.
            // Si la lista ya contenía alguna palabra, hay que añadir una coma
            if (listaPalabrasSoluciones.length() == 0){
                listaPalabrasSoluciones.append(palabraActual);
            } else {
                listaPalabrasSoluciones.append(", ");
                listaPalabrasSoluciones.append(palabraActual);
            }
        }
        // Actualizamos el texto del TextView
        soluciones.setText(Html.fromHtml("Has encertat "+
                            administradorPalabras.getNumPalabrasEncontradas()+
                            " de "+
                            administradorPalabras.getNumPosiblesSoluciones()+
                            " posibles: "+
                            listaPalabrasSoluciones));
    }

    // Método bonus para la función OnClick del botón bonus. Mostrará una ventana emergente con la
    // lista de palabras que se han encontrado.
    public void bonus(View v) {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        // Inicializa el título del AlertDialog
        adBuilder.setTitle("Encertades ("+
                   administradorPalabras.getNumPalabrasEncontradas()+
                    " de "+
                    administradorPalabras.getNumPosiblesSoluciones()+
                    "):");
        // StringBuilder e iterador para recorrer el catálogo de soluciones encontradas y poder
        // añadirlo al mensaje del AlertDialog
        StringBuilder palabrasEncontradas = new StringBuilder();
        Iterator<String> itPalabrasEncontradas = administradorPalabras.getSolucionesEncontradas().iterator();
        // Recorrido catálogo soluciones encontradas
        while (itPalabrasEncontradas.hasNext()){
            String palabraActual = administradorPalabras.getPalabrasValidas().get(itPalabrasEncontradas.next());
            // Comprueba si es la primera palabra o no
            if(palabrasEncontradas.length() == 0){
                palabrasEncontradas.append(palabraActual);
            } else {
                palabrasEncontradas.append(", ");
                palabrasEncontradas.append(palabraActual);
            }
        }
        adBuilder.setMessage(palabrasEncontradas);

        // Botón OK
        adBuilder.setPositiveButton("OK",null);
        AlertDialog ad = adBuilder.create();
        ad.show();
    }

    // Método ayuda para la función OnClick del botón ayuda. Verifica que se tengan los puntos
    // suficientes del bonus para la ayuda y muestra la primera letra de una de las palabras
    // ocultas si se da el caso
    public void ayuda(View v) {
        int palabrasParaBonus = administradorPalabras.getPalabrasParaBonus();
        // Verifica que se tengan los puntos o que todavía se pudean utilizar ayudas
        if (puntosBonus < puntosNecesariosBonus){
            muestraMensaje("No tens punts suficients per solicitar l'ajuda!",false);
            return;
        } else if (palabrasParaBonus <= 0){
            muestraMensaje("Ja has emprat totes les ajudes!",false);
            return;
        }
        // Resetea los puntos para otro bonus y actualizamos el valor en el botón
        puntosBonus -= puntosNecesariosBonus;
        Button botonBonus = findViewById(R.id.botonBonus);
        botonBonus.setText(String.valueOf(puntosBonus));

        // De las palabras ocultas, escoge una al azar
        Random r = new Random();
        int palabraOcultaRandom = r.nextInt(palabrasParaBonus);
        Iterator<Map.Entry<Integer, String>> itPalabrasParaBonus =
                administradorPalabras.getSinBonus().entrySet().iterator();
        // Bucle que continua mientras haya palabras Ocultas
        while(itPalabrasParaBonus.hasNext() && (palabraOcultaRandom >= 0)){
            Map.Entry<Integer, String> entrada = itPalabrasParaBonus.next();
            // Si se llega a 0, significa que nos encontramos en la palabra aleatoria que queríamos
            if (palabraOcultaRandom == 0){
                // Mostramos su primera letra, eliminamos la palabra del conjunto y decrementamos el
                // contador de palabras para bonus
                muestraPrimeraLetra(entrada.getValue(), entrada.getKey());
                administradorPalabras.getSinBonus().remove(entrada.getKey());
                administradorPalabras.restarNumPalabrasParaBonus();
                break;
            }
            // Si todavía no es la palabra que queremos, restamos el valor
            palabraOcultaRandom--;
        }
    }

    // Método nuevaPartida que implementará todos los parámetros para el inicio de una nueva
    // partida, además de ser la función OnClick del botón reiniciar.
    public void nuevaPartida (View v) {
        // Habilita todos los componentes
        habilitarViews(R.id.main);
        // Carga una nueva partida mediante el administrador de palabras
        administradorPalabras.cargarPartida();
        // Si la matriz de letras no está vacía, borra su contenido
        if (matrizLetras != null){
            borrarTextViews();
        }
        // Asigna un color aleatorio de entre los disponibles para la interfaz
        Random r = new Random();
        String colorRandom = colores[r.nextInt(3)];
        setColorInterfaz(colorRandom);
        // Inicializa a 0 los puntos para un bonus
        puntosBonus = 0;
        Button botonBonus = findViewById(R.id.botonBonus);
        botonBonus.setText(String.valueOf(puntosBonus));
        // Inicializa las letras del circulo y las mezclamos
        setLetrasCirculo();
        shuffleLetras(null);
        // Inicializa el número de palabras Ocultas y lo asigna como las filas de la matriz de Letras
        int numPalabrasOcultas = administradorPalabras.getPalabrasOcultas().entrySet().size();
        matrizLetras = new int[numPalabrasOcultas][];
        // Para cada palabra oculta, inicializa su número de letras
        for(int i = 0; i<matrizLetras.length; i++){
            int longPalabraOculta = administradorPalabras.getPalabrasOcultas().get(i).length();
            matrizLetras[i] = new int[longPalabraOculta];
        }
        // Crea el TextView para cada una de las palabras ocultas mediante un iterador y la llamada
        // a la función crearFilaTextView
        Iterator<Map.Entry<Integer, String>> itPalabrasOcultas =
                administradorPalabras.getPalabrasOcultas().entrySet().iterator();
        while (itPalabrasOcultas.hasNext()){
            Map.Entry<Integer, String> par = itPalabrasOcultas.next();
            crearFilaTextViews(idGuias[par.getKey()], par.getValue().length(), par.getKey());
            System.out.println("Crea 1 fila");
        }
        // Actualizamos la lista de palabras soluciones
        actualizarListaPalabrasSoluciones(null);
    }
}