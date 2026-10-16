import java.util.ArrayList;
import java.util.List;

/**
 * Métodos de apoyo para calcular propiedades de un árbol general.
 *
 * Convenciones usadas:
 * - La raíz tiene profundidad 0.
 * - Una hoja tiene altura 0.
 * - La altura se mide en número de aristas.
 * - El nivel se toma igual a profundidad.
 */
public class MetricasArbol {

    /**
     * Calcula la altura de un nodo.
     * Una hoja tiene altura 0.
     */
    public static int altura(NodoGeneral<?> nodo) {
        if (nodo.getHijos().isEmpty()) {
            return 0;
        }

        int max = 0;

        for (NodoGeneral<?> hijo : nodo.getHijos()) {
            max = Math.max(max, altura(hijo));
        }

        return 1 + max;
    }

    /**
     * Calcula la profundidad de un nodo objetivo desde la raíz.
     * Devuelve -1 si el objetivo no pertenece al árbol.
     */
    public static int profundidad(
            NodoGeneral<?> raiz,
            NodoGeneral<?> objetivo) {

        return profundidadRec(raiz, objetivo, 0);
    }

    private static int profundidadRec(
            NodoGeneral<?> actual,
            NodoGeneral<?> objetivo,
            int profundidadActual) {

        if (actual == objetivo) {
            return profundidadActual;
        }

        for (NodoGeneral<?> hijo : actual.getHijos()) {

            int resultado = profundidadRec(
                    hijo,
                    objetivo,
                    profundidadActual + 1
            );

            if (resultado != -1) {
                return resultado;
            }
        }

        return -1;
    }

    /**
     * El grado de un nodo es el número de hijos directos.
     */
    public static int gradoNodo(NodoGeneral<?> nodo) {
        return nodo.getHijos().size();
    }

    /**
     * El grado del árbol es el mayor grado de cualquiera de sus nodos.
     */
    public static int gradoArbol(NodoGeneral<?> raiz) {

        int max = gradoNodo(raiz);

        for (NodoGeneral<?> hijo : raiz.getHijos()) {
            max = Math.max(max, gradoArbol(hijo));
        }

        return max;
    }

    /**
     * Cuenta todos los nodos del árbol.
     */
    public static int contarNodos(NodoGeneral<?> raiz) {

        int total = 1;

        for (NodoGeneral<?> hijo : raiz.getHijos()) {
            total += contarNodos(hijo);
        }

        return total;
    }

    /**
     * Cuenta las hojas del árbol.
     */
    public static int contarHojas(NodoGeneral<?> raiz) {

        if (raiz.esHoja()) {
            return 1;
        }

        int total = 0;

        for (NodoGeneral<?> hijo : raiz.getHijos()) {
            total += contarHojas(hijo);
        }

        return total;
    }

    /**
     * Construye el camino desde la raíz hasta un nodo objetivo.
     */
    public static List<NodoGeneral<?>> camino(
            NodoGeneral<?> raiz,
            NodoGeneral<?> objetivo) {

        List<NodoGeneral<?>> ruta = new ArrayList<>();

        if (buscarCamino(raiz, objetivo, ruta)) {
            return ruta;
        }

        return new ArrayList<>();
    }

    private static boolean buscarCamino(
            NodoGeneral<?> actual,
            NodoGeneral<?> objetivo,
            List<NodoGeneral<?>> ruta) {

        ruta.add(actual);

        if (actual == objetivo) {
            return true;
        }

        for (NodoGeneral<?> hijo : actual.getHijos()) {

            if (buscarCamino(hijo, objetivo, ruta)) {
                return true;
            }
        }

        ruta.remove(ruta.size() - 1);
        return false;
    }

    /**
     * Longitud del camino en número de aristas.
     */
    public static int longitudCamino(
            NodoGeneral<?> raiz,
            NodoGeneral<?> objetivo) {

        List<NodoGeneral<?>> ruta = camino(raiz, objetivo);

        if (ruta.isEmpty()) {
            return -1;
        }

        return ruta.size() - 1;
    }

    /**
     * Muestra el árbol con sangría según el nivel.
     */
    public static void mostrarArbol(
            NodoGeneral<?> nodo,
            int nivel) {

        String sangria = "   ".repeat(nivel);

        System.out.println(
                sangria + "└── " + nodo.getDato()
        );

        for (NodoGeneral<?> hijo : nodo.getHijos()) {
            mostrarArbol(hijo, nivel + 1);
        }
    }

    /**
     * Muestra las métricas de todos los nodos.
     */
    public static void mostrarTablaMetricas(
            NodoGeneral<?> raiz) {

        System.out.printf(
                "%-28s %-8s %-8s %-12s %-8s%n",
                "Nodo",
                "Grado",
                "Nivel",
                "Profundidad",
                "Altura"
        );

        System.out.println(
                "--------------------------------------------------------------------"
        );

        mostrarFila(raiz, raiz);
    }

    private static void mostrarFila(
            NodoGeneral<?> raiz,
            NodoGeneral<?> actual) {

        int profundidad = profundidad(raiz, actual);

        System.out.printf(
                "%-28s %-8d %-8d %-12d %-8d%n",
                actual.getDato(),
                gradoNodo(actual),
                profundidad,
                profundidad,
                altura(actual)
        );

        for (NodoGeneral<?> hijo : actual.getHijos()) {
            mostrarFila(raiz, hijo);
        }
    }

    /**
     * Convierte una ruta en texto legible.
     */
    public static String rutaComoTexto(
            List<NodoGeneral<?>> ruta) {

        if (ruta.isEmpty()) {
            return "(sin ruta)";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ruta.size(); i++) {

            sb.append(ruta.get(i).getDato());

            if (i < ruta.size() - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }
}
