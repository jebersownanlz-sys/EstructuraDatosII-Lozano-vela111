import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Árbol binario que guarda la referencia a la raíz y ofrece
 * métodos de impresión, recorridos y clasificación.
 * Todos los métodos validan null para evitar NullPointerException.
 */
public class ArbolBinario<T> {

    private NodoArbol<T> raiz;

    public ArbolBinario() {
        this.raiz = null;
    }

    public ArbolBinario(NodoArbol<T> raiz) {
        this.raiz = raiz;
    }

    public NodoArbol<T> getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoArbol<T> raiz) {
        this.raiz = raiz;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    // ------------------------------------------------------------
    // imprimirRaizYHijos()
    // ------------------------------------------------------------

    /** Imprime la raíz del árbol y sus dos hijos. */
    public void imprimirRaizYHijos() {
        if (raiz == null) {
            System.out.println("El árbol está vacío (raiz = null).");
            return;
        }
        imprimirRaizYHijos(raiz);
    }

    /** Imprime un nodo cualquiera con sus hijos, validando null. */
    public void imprimirRaizYHijos(NodoArbol<T> nodo) {
        if (nodo == null) {
            System.out.println("No hay nodo que imprimir (referencia null).");
            return;
        }
        String izq = (nodo.getIzquierdo() == null)
                ? "null" : String.valueOf(nodo.getIzquierdo().getDato());
        String der = (nodo.getDerecho() == null)
                ? "null" : String.valueOf(nodo.getDerecho().getDato());

        System.out.printf("Nodo: %-12s | Izquierdo: %-12s | Derecho: %-12s%n",
                nodo.getDato(), izq, der);
    }

    /** Imprime todos los nodos con sus hijos (recorrido preorden). */
    public void imprimirTodosConHijos() {
        imprimirTodosConHijos(raiz);
    }

    private void imprimirTodosConHijos(NodoArbol<T> nodo) {
        if (nodo == null) return;
        imprimirRaizYHijos(nodo);
        imprimirTodosConHijos(nodo.getIzquierdo());
        imprimirTodosConHijos(nodo.getDerecho());
    }

    // ------------------------------------------------------------
    // Métricas básicas
    // ------------------------------------------------------------

    public int contarNodos() {
        return contarNodos(raiz);
    }

    private int contarNodos(NodoArbol<T> n) {
        if (n == null) return 0;
        return 1 + contarNodos(n.getIzquierdo()) + contarNodos(n.getDerecho());
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoArbol<T> n) {
        if (n == null) return 0;
        if (n.esHoja()) return 1;
        return contarHojas(n.getIzquierdo()) + contarHojas(n.getDerecho());
    }

    /** Altura en aristas. Árbol vacío = -1, un solo nodo = 0. */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoArbol<T> n) {
        if (n == null) return -1;
        return 1 + Math.max(altura(n.getIzquierdo()), altura(n.getDerecho()));
    }

    // ------------------------------------------------------------
    // Recorridos
    // ------------------------------------------------------------

    public List<T> preorden() {
        List<T> r = new ArrayList<>();
        preorden(raiz, r);
        return r;
    }

    private void preorden(NodoArbol<T> n, List<T> r) {
        if (n == null) return;
        r.add(n.getDato());
        preorden(n.getIzquierdo(), r);
        preorden(n.getDerecho(), r);
    }

    public List<T> inorden() {
        List<T> r = new ArrayList<>();
        inorden(raiz, r);
        return r;
    }

    private void inorden(NodoArbol<T> n, List<T> r) {
        if (n == null) return;
        inorden(n.getIzquierdo(), r);
        r.add(n.getDato());
        inorden(n.getDerecho(), r);
    }

    public List<T> postorden() {
        List<T> r = new ArrayList<>();
        postorden(raiz, r);
        return r;
    }

    private void postorden(NodoArbol<T> n, List<T> r) {
        if (n == null) return;
        postorden(n.getIzquierdo(), r);
        postorden(n.getDerecho(), r);
        r.add(n.getDato());
    }

    public List<T> porNiveles() {
        List<T> r = new ArrayList<>();
        if (raiz == null) return r;
        Queue<NodoArbol<T>> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            NodoArbol<T> n = cola.remove();
            r.add(n.getDato());
            if (n.getIzquierdo() != null) cola.add(n.getIzquierdo());
            if (n.getDerecho() != null) cola.add(n.getDerecho());
        }
        return r;
    }

    // ------------------------------------------------------------
    // Clasificación
    // ------------------------------------------------------------

    /** Lleno: cada nodo tiene 0 o 2 hijos. */
    public boolean esLleno() {
        return raiz != null && esLleno(raiz);
    }

    private boolean esLleno(NodoArbol<T> n) {
        if (n == null) return true;
        if (n.cantidadHijos() == 1) return false;
        return esLleno(n.getIzquierdo()) && esLleno(n.getDerecho());
    }

    /** Completo: todos los niveles llenos, el último se llena de izquierda a derecha. */
    public boolean esCompleto() {
        if (raiz == null) return false;
        Queue<NodoArbol<T>> cola = new LinkedList<>();
        cola.add(raiz);
        boolean huboHuecoPrevio = false;
        while (!cola.isEmpty()) {
            NodoArbol<T> n = cola.remove();

            if (n.getIzquierdo() != null) {
                if (huboHuecoPrevio) return false;
                cola.add(n.getIzquierdo());
            } else {
                huboHuecoPrevio = true;
            }

            if (n.getDerecho() != null) {
                if (huboHuecoPrevio) return false;
                cola.add(n.getDerecho());
            } else {
                huboHuecoPrevio = true;
            }
        }
        return true;
    }

    /** Perfecto: todos los internos tienen 2 hijos y todas las hojas están al mismo nivel. */
    public boolean esPerfecto() {
        if (raiz == null) return false;
        int h = altura();
        return contarNodos() == (1 << (h + 1)) - 1;
    }

    /** Degenerado: cada nodo tiene un solo hijo (como una lista enlazada). */
    public boolean esDegenerado() {
        return raiz != null && contarNodos() >= 2 && esDegenerado(raiz);
    }

    private boolean esDegenerado(NodoArbol<T> n) {
        if (n == null || n.esHoja()) return true;
        if (n.cantidadHijos() == 2) return false;
        return esDegenerado(n.getIzquierdo()) && esDegenerado(n.getDerecho());
    }

    /** Devuelve todas las clasificaciones que cumple el árbol. */
    public String clasificar() {
        if (raiz == null) return "Vacío";
        List<String> tipos = new ArrayList<>();
        if (esPerfecto()) tipos.add("PERFECTO");
        if (esLleno()) tipos.add("LLENO");
        if (esCompleto()) tipos.add("COMPLETO");
        if (esDegenerado()) tipos.add("DEGENERADO");
        if (tipos.isEmpty()) tipos.add("binario sin clasificación especial");
        return String.join(" + ", tipos);
    }

    // ------------------------------------------------------------
    // Dibujo en consola (el árbol "acostado": derecha arriba, izquierda abajo)
    // ------------------------------------------------------------

    public void mostrar() {
        if (raiz == null) {
            System.out.println("(árbol vacío)");
            return;
        }
        mostrar(raiz, 0);
    }

    private void mostrar(NodoArbol<T> n, int nivel) {
        if (n == null) return;
        mostrar(n.getDerecho(), nivel + 1);
        System.out.println("      ".repeat(nivel) + n.getDato());
        mostrar(n.getIzquierdo(), nivel + 1);
    }
}
