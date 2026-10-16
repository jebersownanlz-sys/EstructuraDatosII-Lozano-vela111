import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nodo general para representar un árbol n-ario.
 * Cada nodo almacena un dato y una lista de hijos.
 *
 * @param <T> tipo de dato almacenado.
 */
public class NodoGeneral<T> {

    private final T dato;
    private final List<NodoGeneral<T>> hijos;

    public NodoGeneral(T dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
    }

    public T getDato() {
        return dato;
    }

    public void agregarHijo(NodoGeneral<T> hijo) {
        if (hijo == null) {
            throw new IllegalArgumentException("El hijo no puede ser null.");
        }
        hijos.add(hijo);
    }

    public List<NodoGeneral<T>> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }

    public int grado() {
        return hijos.size();
    }

    @Override
    public String toString() {
        return String.valueOf(dato);
    }
}
