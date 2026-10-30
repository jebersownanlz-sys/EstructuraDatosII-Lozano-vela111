/**
 * Nodo de un arbol binario: un dato y dos hijos (izquierdo y derecho).
 * Se respeta la interfaz del codigo base de la guia (getDato, getIzquierdo, getDerecho).
 *
 * @param <T> tipo del dato almacenado
 */
class NodoArbol<T> {

    private T dato;
    private NodoArbol<T> izquierdo;
    private NodoArbol<T> derecho;

    NodoArbol(T dato) {
        this.dato = dato;
    }

    T getDato() {
        return dato;
    }

    NodoArbol<T> getIzquierdo() {
        return izquierdo;
    }

    NodoArbol<T> getDerecho() {
        return derecho;
    }

    void setDato(T dato) {
        this.dato = dato;
    }

    void setIzquierdo(NodoArbol<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    void setDerecho(NodoArbol<T> derecho) {
        this.derecho = derecho;
    }
}
