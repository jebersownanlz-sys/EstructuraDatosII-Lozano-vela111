import java.util.ArrayList;
import java.util.List;

/**
 * Árbol Binario de Búsqueda (BST) de enteros (códigos de producto).
 * Convención: menores a la izquierda, mayores o iguales a la derecha.
 * Incluye inserción y búsqueda recursivas (Guía 4) y eliminación (Guía 5).
 */
public class ArbolBST {
    private NodoArbol<Integer> raiz;
    private int comparaciones; // contador de comparaciones de la última búsqueda

    // ---------- Guía 4: inserción ----------
    public void insertar(int valor) { raiz = insertarRec(raiz, valor); }

    private NodoArbol<Integer> insertarRec(NodoArbol<Integer> nodo, int valor) {
        if (nodo == null) return new NodoArbol<>(valor);
        if (valor < nodo.getDato()) nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), valor));
        else nodo.setDerecho(insertarRec(nodo.getDerecho(), valor));
        return nodo;
    }

    // ---------- Guía 4: búsqueda ----------
    public boolean buscar(int valor) {
        comparaciones = 0;
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(NodoArbol<Integer> nodo, int valor) {
        if (nodo == null) return false;
        comparaciones++;                       // se compara el valor con el dato del nodo visitado
        if (valor == nodo.getDato()) return true;
        return valor < nodo.getDato() ? buscarRec(nodo.getIzquierdo(), valor)
                                      : buscarRec(nodo.getDerecho(), valor);
    }

    /** Camino recorrido al buscar (termina en "null" si no se encuentra). */
    public String camino(int valor) {
        StringBuilder sb = new StringBuilder();
        NodoArbol<Integer> n = raiz;
        while (n != null) {
            sb.append(n.getDato()).append(" -> ");
            if (valor == n.getDato()) return sb.substring(0, sb.length() - 4);
            n = valor < n.getDato() ? n.getIzquierdo() : n.getDerecho();
        }
        return sb.append("null").toString();
    }

    public int getComparaciones() { return comparaciones; }

    // ---------- Métricas ----------
    /** Altura en nodos (árbol vacío = 0, un solo nodo = 1). */
    public int altura() { return alturaRec(raiz); }
    private int alturaRec(NodoArbol<Integer> n) {
        return n == null ? 0 : 1 + Math.max(alturaRec(n.getIzquierdo()), alturaRec(n.getDerecho()));
    }

    /** Promedio de comparaciones de una búsqueda exitosa sobre todos los elementos. */
    public double promedioComparaciones() {
        List<Integer> v = inorden();
        long total = 0;
        for (int x : v) { buscar(x); total += comparaciones; }
        return v.isEmpty() ? 0 : (double) total / v.size();
    }

    // ---------- Recorridos ----------
    public List<Integer> inorden() { List<Integer> l = new ArrayList<>(); inordenRec(raiz, l); return l; }
    private void inordenRec(NodoArbol<Integer> n, List<Integer> l) {
        if (n == null) return;
        inordenRec(n.getIzquierdo(), l); l.add(n.getDato()); inordenRec(n.getDerecho(), l);
    }

    public String preorden() { StringBuilder sb = new StringBuilder(); preRec(raiz, sb); return sb.toString().trim(); }
    private void preRec(NodoArbol<Integer> n, StringBuilder sb) {
        if (n == null) return;
        sb.append(n.getDato()).append(' '); preRec(n.getIzquierdo(), sb); preRec(n.getDerecho(), sb);
    }

    /** Verifica la propiedad BST (inorden estrictamente no decreciente). */
    public boolean esBST() {
        List<Integer> l = inorden();
        for (int i = 1; i < l.size(); i++) if (l.get(i) < l.get(i - 1)) return false;
        return true;
    }

    // ---------- Guía 5: eliminación ----------
    public void eliminar(int valor) { raiz = eliminarRec(raiz, valor); }

    private NodoArbol<Integer> eliminarRec(NodoArbol<Integer> nodo, int valor) {
        if (nodo == null) return null;                    // valor no existe
        if (valor < nodo.getDato()) {
            nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), valor));
        } else if (valor > nodo.getDato()) {
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), valor));
        } else {
            // Caso 1 (hoja) y Caso 2 (un hijo): el padre se enlaza con el único hijo (o null)
            if (nodo.getIzquierdo() == null) return nodo.getDerecho();
            if (nodo.getDerecho() == null) return nodo.getIzquierdo();
            // Caso 3 (dos hijos): se copia el sucesor inorden y se elimina su duplicado
            int sucesor = minValor(nodo.getDerecho());
            nodo.setDato(sucesor);
            nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor));
        }
        return nodo;
    }

    /** Mínimo de un subárbol: el nodo más a la izquierda. */
    private int minValor(NodoArbol<Integer> nodo) {
        while (nodo.getIzquierdo() != null) nodo = nodo.getIzquierdo();
        return nodo.getDato();
    }

    // ---------- Salida gráfica en consola ----------
    @Override public String toString() { StringBuilder sb = new StringBuilder(); dibujar(raiz, "", true, sb); return sb.toString(); }
    private void dibujar(NodoArbol<Integer> n, String pref, boolean ultimo, StringBuilder sb) {
        if (n == null) return;
        sb.append(pref).append(ultimo ? "└── " : "├── ").append(n.getDato()).append('\n');
        List<NodoArbol<Integer>> h = new ArrayList<>();
        if (n.getIzquierdo() != null) h.add(n.getIzquierdo());
        if (n.getDerecho() != null) h.add(n.getDerecho());
        for (int i = 0; i < h.size(); i++) dibujar(h.get(i), pref + (ultimo ? "    " : "│   "), i == h.size() - 1, sb);
    }
}
