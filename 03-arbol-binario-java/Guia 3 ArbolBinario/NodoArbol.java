/**
 * Nodo genérico de un árbol binario.
 * Cada nodo guarda un dato y dos referencias: hijo izquierdo e hijo derecho.
 * Una referencia en null significa "no hay nodo ahí".
 *
 * @param <T> tipo del dato almacenado.
 */
public class NodoArbol<T> {

    private T dato;
    private NodoArbol<T> izquierdo;
    private NodoArbol<T> derecho;

    public NodoArbol(T dato) {
        this.dato = dato;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoArbol<T> getIzquierdo() {
        return izquierdo;
    }

    public NodoArbol<T> getDerecho() {
        return derecho;
    }

    public void setIzquierdo(NodoArbol<T> n) {
        izquierdo = n;
    }

    public void setDerecho(NodoArbol<T> n) {
        derecho = n;
    }

    /** Un nodo hoja es un nodo real cuyas dos referencias son null. */
    public boolean esHoja() {
        return izquierdo == null && derecho == null;
    }

    /** Cantidad de hijos reales (0, 1 o 2). */
    public int cantidadHijos() {
        int total = 0;
        if (izquierdo != null) total++;
        if (derecho != null) total++;
        return total;
    }

    @Override
    public String toString() {
        return String.valueOf(dato);
    }
}
