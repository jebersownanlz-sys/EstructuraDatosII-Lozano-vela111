import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Arbol binario de busqueda (BST) de productos, ordenado por codigo.
 *
 * Regla del BST: para todo nodo, los codigos del subarbol izquierdo son MENORES
 * y los del subarbol derecho son MAYORES.
 *
 * Recorridos (n = cantidad de nodos):
 *   preorden   raiz - izquierda - derecha      O(n)
 *   inorden    izquierda - raiz - derecha      O(n)   -> en un BST sale ORDENADO
 *   postorden  izquierda - derecha - raiz      O(n)
 *   porNiveles cola (Queue), por profundidad   O(n)
 * Los tres recorridos en profundidad son recursivos y usan memoria O(h) (h = altura);
 * el recorrido por niveles usa una cola de hasta O(ancho del arbol).
 */
class ArbolProductos {

    private NodoArbol<Producto> raiz;
    private int tamano = 0;

    // ------------------------------------------------------------------
    // Construccion y consultas
    // ------------------------------------------------------------------

    /**
     * Inserta un producto respetando la regla del BST.
     *
     * @return true si se inserto; false si ya existia un producto con ese codigo
     */
    boolean insertar(Producto p) {
        if (p == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        NodoArbol<Producto> nuevo = new NodoArbol<>(p);
        if (raiz == null) {
            raiz = nuevo;
            tamano++;
            return true;
        }
        NodoArbol<Producto> actual = raiz;
        while (true) {
            int cmp = Integer.compare(p.getCodigo(), actual.getDato().getCodigo());
            if (cmp == 0) {
                return false; // codigo repetido
            }
            if (cmp < 0) {
                if (actual.getIzquierdo() == null) {
                    actual.setIzquierdo(nuevo);
                    break;
                }
                actual = actual.getIzquierdo();
            } else {
                if (actual.getDerecho() == null) {
                    actual.setDerecho(nuevo);
                    break;
                }
                actual = actual.getDerecho();
            }
        }
        tamano++;
        return true;
    }

    /** Busca un producto por codigo; devuelve null si no existe. */
    Producto buscar(int codigo) {
        NodoArbol<Producto> actual = raiz;
        while (actual != null) {
            int cmp = Integer.compare(codigo, actual.getDato().getCodigo());
            if (cmp == 0) return actual.getDato();
            actual = cmp < 0 ? actual.getIzquierdo() : actual.getDerecho();
        }
        return null;
    }

    int tamano() {
        return tamano;
    }

    boolean estaVacio() {
        return raiz == null;
    }

    NodoArbol<Producto> getRaiz() {
        return raiz;
    }

    /** Altura medida en niveles: arbol vacio = 0, solo la raiz = 1. */
    int altura() {
        return altura(raiz);
    }

    private int altura(NodoArbol<Producto> n) {
        if (n == null) return 0;
        return 1 + Math.max(altura(n.getIzquierdo()), altura(n.getDerecho()));
    }

    // ------------------------------------------------------------------
    // Recorridos en profundidad (DFS), recursivos
    // ------------------------------------------------------------------

    /** Preorden: raiz, izquierda, derecha. */
    List<Producto> preorden() {
        List<Producto> salida = new ArrayList<>();
        preorden(raiz, salida);
        return salida;
    }

    private void preorden(NodoArbol<Producto> n, List<Producto> salida) {
        if (n == null) return;
        salida.add(n.getDato());              // 1) visito la raiz
        preorden(n.getIzquierdo(), salida);   // 2) subarbol izquierdo
        preorden(n.getDerecho(), salida);     // 3) subarbol derecho
    }

    /** Inorden: izquierda, raiz, derecha. En un BST devuelve los codigos ordenados. */
    List<Producto> inorden() {
        List<Producto> salida = new ArrayList<>();
        inorden(raiz, salida);
        return salida;
    }

    private void inorden(NodoArbol<Producto> n, List<Producto> salida) {
        if (n == null) return;
        inorden(n.getIzquierdo(), salida);    // 1) todo lo menor
        salida.add(n.getDato());              // 2) la raiz
        inorden(n.getDerecho(), salida);      // 3) todo lo mayor
    }

    /** Postorden: izquierda, derecha, raiz. */
    List<Producto> postorden() {
        List<Producto> salida = new ArrayList<>();
        postorden(raiz, salida);
        return salida;
    }

    private void postorden(NodoArbol<Producto> n, List<Producto> salida) {
        if (n == null) return;
        postorden(n.getIzquierdo(), salida);  // 1) subarbol izquierdo
        postorden(n.getDerecho(), salida);    // 2) subarbol derecho
        salida.add(n.getDato());              // 3) la raiz al final
    }

    // ------------------------------------------------------------------
    // Recorrido por niveles (BFS) con Queue
    // ------------------------------------------------------------------

    /** Por niveles: de arriba hacia abajo y de izquierda a derecha. */
    List<Producto> porNiveles() {
        List<Producto> salida = new ArrayList<>();
        for (List<Producto> nivel : porNivelesAgrupados()) {
            salida.addAll(nivel);
        }
        return salida;
    }

    /**
     * Por niveles, agrupado: el elemento k de la lista son los productos que estan
     * a profundidad k (0 = raiz). Antes de procesar cada nivel se guarda cuantos
     * nodos hay en la cola; esos son exactamente los de ese nivel.
     */
    List<List<Producto>> porNivelesAgrupados() {
        List<List<Producto>> niveles = new ArrayList<>();
        if (raiz == null) return niveles;

        Queue<NodoArbol<Producto>> cola = new LinkedList<>();
        cola.add(raiz);

        while (!cola.isEmpty()) {
            int enEsteNivel = cola.size();
            List<Producto> nivel = new ArrayList<>();
            for (int i = 0; i < enEsteNivel; i++) {
                NodoArbol<Producto> actual = cola.poll();
                nivel.add(actual.getDato());
                if (actual.getIzquierdo() != null) cola.add(actual.getIzquierdo());
                if (actual.getDerecho() != null) cola.add(actual.getDerecho());
            }
            niveles.add(nivel);
        }
        return niveles;
    }

    // ------------------------------------------------------------------
    // Eliminar el arbol completo (Postorden)
    // ------------------------------------------------------------------

    /**
     * Elimina todo el arbol liberando cada nodo DESPUES de sus hijos (postorden).
     * Asi nunca se pierde la referencia a un subarbol que aun no se ha liberado:
     * si se soltara primero el padre (preorden), los hijos quedarian inalcanzables.
     *
     * @return los productos en el orden en que se liberaron
     */
    List<Producto> vaciar() {
        List<Producto> orden = new ArrayList<>();
        liberar(raiz, orden);
        raiz = null;
        tamano = 0;
        return orden;
    }

    private void liberar(NodoArbol<Producto> n, List<Producto> orden) {
        if (n == null) return;
        liberar(n.getIzquierdo(), orden);
        liberar(n.getDerecho(), orden);
        orden.add(n.getDato());   // aqui "se procesa" el nodo: sus hijos ya fueron liberados
        n.setIzquierdo(null);
        n.setDerecho(null);
        n.setDato(null);
    }
}
