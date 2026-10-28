import java.util.ArrayList;
import java.util.List;

/**
 * BST de productos indexado por código (String, orden lexicográfico).
 *
 * Convenciones:
 *  - código menor  -> subárbol izquierdo
 *  - código mayor  -> subárbol derecho
 *  - código repetido -> NO se inserta (insertar devuelve false)
 *  - altura medida en NIVELES: árbol vacío = 0, un solo nodo = 1
 */
public class CatalogoBST {

    /** Nodo interno: nadie fuera del árbol necesita verlo. */
    private static class Nodo {
        final Producto dato;
        Nodo izq, der;
        Nodo(Producto dato) { this.dato = dato; }
    }

    private Nodo raiz;
    private int tamano;

    // Se actualiza en cada búsqueda: nodos visitados en la última llamada.
    private int comparacionesUltimaBusqueda;

    // ---------------------------------------------------------------
    // INSERCIÓN (recursiva)
    // ---------------------------------------------------------------
    /** @return true si se insertó; false si el código ya existía. */
    public boolean insertar(Producto p) {
        if (p == null || p.getCodigo() == null) {
            throw new IllegalArgumentException("Producto o código nulo");
        }
        int antes = tamano;
        raiz = insertar(raiz, p);
        return tamano > antes;
    }

    private Nodo insertar(Nodo actual, Producto p) {
        if (actual == null) {            // caso base: hueco encontrado
            tamano++;
            return new Nodo(p);
        }
        int cmp = p.getCodigo().compareTo(actual.dato.getCodigo());
        if (cmp < 0)      actual.izq = insertar(actual.izq, p);
        else if (cmp > 0) actual.der = insertar(actual.der, p);
        // cmp == 0: duplicado, se ignora
        return actual;
    }

    // ---------------------------------------------------------------
    // BÚSQUEDA (recursiva)
    // ---------------------------------------------------------------
    /** @return el producto o null si no existe. */
    public Producto buscar(String codigo) {
        comparacionesUltimaBusqueda = 0;
        Nodo n = buscar(raiz, codigo);
        return n == null ? null : n.dato;
    }

    private Nodo buscar(Nodo actual, String codigo) {
        if (actual == null) return null;         // caso base: no existe
        comparacionesUltimaBusqueda++;
        int cmp = codigo.compareTo(actual.dato.getCodigo());
        if (cmp == 0) return actual;             // caso base: encontrado
        return cmp < 0 ? buscar(actual.izq, codigo)
                       : buscar(actual.der, codigo);
    }

    public int getComparacionesUltimaBusqueda() { return comparacionesUltimaBusqueda; }

    // ---------------------------------------------------------------
    // MÉTRICAS Y RECORRIDOS
    // ---------------------------------------------------------------
    public int tamano() { return tamano; }

    /** Altura en niveles (vacío = 0, un nodo = 1). */
    public int altura() { return altura(raiz); }

    private int altura(Nodo n) {
        if (n == null) return 0;
        return 1 + Math.max(altura(n.izq), altura(n.der));
    }

    /** Altura mínima teórica de un BST con n nodos: ceil(log2(n+1)). */
    public static int alturaMinima(int n) {
        int h = 0;
        while ((1L << h) - 1 < n) h++;
        return h;
    }

    /** Productos en orden ascendente de código (recorrido inorden). */
    public List<Producto> inorden() {
        List<Producto> salida = new ArrayList<>();
        inorden(raiz, salida);
        return salida;
    }

    private void inorden(Nodo n, List<Producto> salida) {
        if (n == null) return;
        inorden(n.izq, salida);
        salida.add(n.dato);
        inorden(n.der, salida);
    }

    /** Dibujo horizontal (raíz a la izquierda) para árboles pequeños. */
    public String dibujar() {
        StringBuilder sb = new StringBuilder();
        dibujar(raiz, "", sb);
        return sb.toString();
    }

    private void dibujar(Nodo n, String sangria, StringBuilder sb) {
        if (n == null) return;
        dibujar(n.der, sangria + "      ", sb);
        sb.append(sangria).append(n.dato.getCodigo()).append('\n');
        dibujar(n.izq, sangria + "      ", sb);
    }
}
