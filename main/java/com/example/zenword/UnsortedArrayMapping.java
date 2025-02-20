// Proyecto creado por Gaizka Medina Gordo
package com.example.zenword;

import java.util.Iterator;

// Clase UnsortedArrayMapping que será utilizada para diversos apartados del programa
public class UnsortedArrayMapping<K, V> {
    private K[] claves;
    private V[] valores;
    private int n;

    // Prepara los arrays para almacenar el mapeo
    public UnsortedArrayMapping(int max) {
        this.claves = (K[]) new Object[max];
        this.valores = (V[]) new Object[max];
        this.n = 0;
    }

    // Consultar el valor asociado a la clave
    // O(n): Búsqueda lineal
    public V get(K key) {
        for (int i = 0; i < n; i++) {
            if (claves[i].equals(key)) {
                return valores[i];
            }
        }
        return null;
    }

    // Añadir una pareja clave-valor
    // Retorna el valor anterior asociado a la clave, si lo había, o null
    // O(n): Devuelve el valor anterior asociado a la clave (búsqueda lineal), si lo había
    public V put(K key, V value) {
        for (int i = 0; i < n; i++) {
            if (claves[i].equals(key)) {
                V oldValue = valores[i];
                valores[i] = value;
                return oldValue;
            }
        }
        if (n < claves.length) {
            claves[n] = key;
            valores[n] = value;
            n++;
        }
        return null;
    }

    // Eliminar la asociación de una clave
    // Retorna el valor asociado a la clave, si lo había, o null
    // O(n): Devuelve el valor anterior asociado a la clave (búsqueda lineal), si lo había
    public V remove(K key) {
        for (int i = 0; i < n; i++) {
            if (claves[i].equals(key)) {
                V oldValue = valores[i];
                claves[i] = claves[n - 1];
                valores[i] = valores[n - 1];
                claves[n - 1] = null;
                valores[n - 1] = null;
                n--;
                return oldValue;
            }
        }
        return null;
    }

    // Consultar si el mapeo se encuentra vacío
    // O(1)
    public boolean isEmpty() {
        return n == 0;
    }

    // Declaración y clase Iterador
    public Iterator iterator(){
        Iterator it = new IteratorUnsortedArrayMapping();
        return it;
    }

    private class IteratorUnsortedArrayMapping implements Iterator {

        private int idxIterator;
        private IteratorUnsortedArrayMapping(){
            idxIterator = 0;
        }

        @Override
        public boolean hasNext(){
            return idxIterator < n;
        }

        @Override
        public Object next(){
            Pair p= new Pair(claves[idxIterator - 1], valores[idxIterator - 1]);
            idxIterator++;
            return p;
        }
    }

    public static class Pair<K, V>{
        private final K clave;
        private final V valor;

        public Pair(K clave, V valor){
            this.clave = clave;
            this.valor = valor;
        }

        public K getClave(){
            return clave;
        }

        public V getValor(){
            return valor;
        }
    }
}
