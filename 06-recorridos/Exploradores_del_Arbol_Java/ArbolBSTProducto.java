import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolBSTProducto {

    private NodoProducto raiz;

    // ---------------- INSERCIÓN (BST) ----------------
    public void insertar(Producto p) {
        raiz = insertarRec(raiz, p);
    }

    private NodoProducto insertarRec(NodoProducto nodo, Producto p) {
        if (nodo == null) {
            return new NodoProducto(p);
        }
        if (p.getCodigo() < nodo.getProducto().getCodigo()) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), p));
        } else if (p.getCodigo() > nodo.getProducto().getCodigo()) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), p));
        }
        // Si el código ya existe, no se inserta (las claves son únicas).
        return nodo;
    }

    // ---------------- PREORDEN: Raíz - Izquierda - Derecha ----------------
    public void preorden() {
        System.out.print("Preorden:  ");
        preordenRec(raiz);
        System.out.println();
    }

    private void preordenRec(NodoProducto nodo) {
        if (nodo == null) return;
        System.out.print(nodo.getProducto() + " - ");
        preordenRec(nodo.getIzquierdo());
        preordenRec(nodo.getDerecho());
    }

    // ---------------- INORDEN: Izquierda - Raíz - Derecha ----------------
    public void inorden() {
        System.out.print("Inorden:   ");
        inordenRec(raiz);
        System.out.println();
    }

    private void inordenRec(NodoProducto nodo) {
        if (nodo == null) return;
        inordenRec(nodo.getIzquierdo());
        System.out.print(nodo.getProducto() + " - ");
        inordenRec(nodo.getDerecho());
    }

    // ---------------- POSTORDEN: Izquierda - Derecha - Raíz ----------------
    public void postorden() {
        System.out.print("Postorden: ");
        postordenRec(raiz);
        System.out.println();
    }

    private void postordenRec(NodoProducto nodo) {
        if (nodo == null) return;
        postordenRec(nodo.getIzquierdo());
        postordenRec(nodo.getDerecho());
        System.out.print(nodo.getProducto() + " - ");
    }

    // ---------------- POR NIVELES (BFS) con una cola ----------------
    public void porNiveles() {
        System.out.print("Por niveles: ");
        if (raiz == null) {
            System.out.println();
            return;
        }
        Queue<NodoProducto> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            NodoProducto actual = cola.poll();
            System.out.print(actual.getProducto() + " - ");
            if (actual.getIzquierdo() != null) cola.add(actual.getIzquierdo());
            if (actual.getDerecho() != null) cola.add(actual.getDerecho());
        }
        System.out.println();
    }

    // ---------------- COMPROBACIÓN: Inorden = claves ordenadas ----------------
    public List<Integer> codigosInorden() {
        List<Integer> lista = new ArrayList<>();
        codigosInordenRec(raiz, lista);
        return lista;
    }

    private void codigosInordenRec(NodoProducto nodo, List<Integer> lista) {
        if (nodo == null) return;
        codigosInordenRec(nodo.getIzquierdo(), lista);
        lista.add(nodo.getProducto().getCodigo());
        codigosInordenRec(nodo.getDerecho(), lista);
    }

    public boolean inordenEstaOrdenado() {
        List<Integer> codigos = codigosInorden();
        for (int i = 1; i < codigos.size(); i++) {
            if (codigos.get(i - 1) >= codigos.get(i)) {
                return false;
            }
        }
        return true;
    }
}
