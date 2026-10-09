import java.util.ArrayList;
import java.util.List;

/**
 * Actividad: "De lo lineal a lo jerarquico".
 * Representa el menú de una aplicación como un árbol general
 * (cada nodo tiene un padre y varios hijos) y lo compara con una lista lineal.
 */
class NodoMenu {
    private final String nombre;
    private final List<NodoMenu> hijos = new ArrayList<>();

    NodoMenu(String nombre) {
        this.nombre = nombre;
    }

    /** Crea un hijo, lo conecta a este nodo (padre) y lo devuelve. */
    NodoMenu agregarHijo(String nombre) {
        NodoMenu hijo = new NodoMenu(nombre);
        hijos.add(hijo);
        return hijo;
    }

    boolean esHoja() {
        return hijos.isEmpty();
    }

    /** Recorrido en preorden: primero el padre y luego cada hijo. */
    void imprimir(String prefijo) {
        System.out.println(prefijo + "- " + nombre);
        for (NodoMenu hijo : hijos) {
            hijo.imprimir(prefijo + "  ");
        }
    }

    /** Cuenta todos los nodos del árbol (este nodo incluido). */
    int contarNodos() {
        int total = 1;
        for (NodoMenu hijo : hijos) {
            total += hijo.contarNodos();
        }
        return total;
    }

    /** Cuenta las hojas (opciones que ya no se despliegan). */
    int contarHojas() {
        if (esHoja()) {
            return 1;
        }
        int total = 0;
        for (NodoMenu hijo : hijos) {
            total += hijo.contarHojas();
        }
        return total;
    }

    /** Altura del árbol: una hoja tiene altura 0. */
    int altura() {
        int max = -1;
        for (NodoMenu hijo : hijos) {
            max = Math.max(max, hijo.altura());
        }
        return max + 1;
    }

    /** Busca una opción y devuelve la ruta desde la raíz, o null si no existe. */
    List<String> buscarRuta(String objetivo) {
        List<String> ruta = new ArrayList<>();
        ruta.add(nombre);
        if (nombre.equalsIgnoreCase(objetivo)) {
            return ruta;
        }
        for (NodoMenu hijo : hijos) {
            List<String> sub = hijo.buscarRuta(objetivo);
            if (sub != null) {
                ruta.addAll(sub);
                return ruta;
            }
        }
        return null;
    }

    /** Aplana el árbol a una lista (así se vería en una estructura lineal). */
    void aplanar(List<String> destino) {
        destino.add(nombre);
        for (NodoMenu hijo : hijos) {
            hijo.aplanar(destino);
        }
    }
}

public class MenuAplicacion {
    public static void main(String[] args) {
        NodoMenu menu = new NodoMenu("Menu principal");

        NodoMenu inicio = menu.agregarHijo("Inicio");
        inicio.agregarHijo("Resumen");
        inicio.agregarHijo("Noticias");

        NodoMenu perfil = menu.agregarHijo("Perfil");
        perfil.agregarHijo("Datos personales");
        perfil.agregarHijo("Historial");

        NodoMenu configuracion = menu.agregarHijo("Configuracion");
        configuracion.agregarHijo("Cuenta");
        configuracion.agregarHijo("Seguridad");
        configuracion.agregarHijo("Notificaciones");

        NodoMenu ayuda = menu.agregarHijo("Ayuda");
        ayuda.agregarHijo("Preguntas frecuentes");
        ayuda.agregarHijo("Soporte");

        System.out.println("=== Arbol jerarquico de un menu de aplicacion ===");
        menu.imprimir("");

        System.out.println();
        System.out.println("Total de nodos: " + menu.contarNodos());
        System.out.println("Hojas (opciones finales): " + menu.contarHojas());
        System.out.println("Altura del arbol: " + menu.altura());

        System.out.println();
        System.out.println("=== Busqueda con ruta (ventaja de la jerarquia) ===");
        List<String> ruta = menu.buscarRuta("Seguridad");
        System.out.println("Ruta a 'Seguridad': " + String.join(" > ", ruta));

        System.out.println();
        System.out.println("=== La misma informacion como lista lineal ===");
        List<String> lineal = new ArrayList<>();
        menu.aplanar(lineal);
        System.out.println(lineal);
        System.out.println("En la lista se pierde quien es padre de quien.");
    }
}
