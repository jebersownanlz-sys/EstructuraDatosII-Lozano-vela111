import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Grafo {

    /** Umbral de densidad: D &gt;= 0.5 se considera grafo denso. */
    static final double UMBRAL_DENSIDAD = 0.5;

    /** Estado de un vértice durante el DFS (técnica de los tres colores). */
    private enum Color {
        /** Aún no descubierto. */
        BLANCO,
        /** Descubierto, pero todavía se está explorando (está en el camino actual). */
        GRIS,
        /** Terminado: ya se exploraron todos sus descendientes. */
        NEGRO
    }

    /** Resultado completo de una ejecución de DFS sobre todo el grafo. */
    static class ResultadoDfs {
        /** Vértices en el orden en que fueron descubiertos (preorden). */
        final List<String> preorden = new ArrayList<>();
        /** Vértices en el orden en que terminaron de explorarse (postorden). */
        final List<String> postorden = new ArrayList<>();
        /** Instante de descubrimiento de cada vértice. */
        final Map<String, Integer> descubrimiento = new LinkedHashMap<>();
        /** Instante de finalización de cada vértice. */
        final Map<String, Integer> finalizacion = new LinkedHashMap<>();
        /** Ciclo encontrado (primer vértice repetido al final), o vacío si no hay. */
        List<String> ciclo = new ArrayList<>();

        /** @return {@code true} si el DFS encontró un ciclo. */
        boolean hayCiclo() {
            return !ciclo.isEmpty();
        }
    }

    /** Conjunto V, indexado por nombre (conserva el orden de inserción). */
    private final Map<String, Vertice> vertices = new LinkedHashMap<>();

    /** Conjunto A, en orden de inserción. */
    private final List<Arista> aristas = new ArrayList<>();

    /** Lista de adyacencia: módulo -> módulos que dependen de él (sus sucesores). */
    private final Map<String, List<String>> adyacencia = new LinkedHashMap<>();
 
    // Construcción del grafo
    
    /**
     * Agrega un vértice al grafo.
     
     * @return {@code true} si se agregó; {@code false} si ya existía uno con ese nombre
     */
    boolean agregarVertice(Vertice v) {
        if (vertices.containsKey(v.nombre)) {
            return false;
        }
        vertices.put(v.nombre, v);
        adyacencia.put(v.nombre, new ArrayList<>());
        return true;
    }

    /**
     * Agrega una dependencia: {@code a.origen} debe instalarse antes que {@code a.destino}.
     *
     * @throws IllegalArgumentException si algún extremo no es vértice del grafo
     *                                  o si la arista está repetida
     */
    void agregarArista(Arista a) {
        if (!vertices.containsKey(a.origen) || !vertices.containsKey(a.destino)) {
            throw new IllegalArgumentException("Vértice inexistente en la arista: " + a);
        }
        if (adyacencia.get(a.origen).contains(a.destino)) {
            throw new IllegalArgumentException("Arista repetida: " + a);
        }
        aristas.add(a);
        adyacencia.get(a.origen).add(a.destino);
    }

    // Consultas básicas
    
    Collection<Vertice> getVertices() {
        return Collections.unmodifiableCollection(vertices.values());
    }

    List<Arista> getAristas() {
        return Collections.unmodifiableList(aristas);
    }

    int numVertices() {
        return vertices.size();
    }

    int numAristas() {
        return aristas.size();
    }

    /** Grado de salida: cuántos módulos dependen directamente de este. */
    int gradoSalida(String nombre) {
        return adyacencia.get(nombre).size();
    }

    /** Grado de entrada: de cuántos módulos depende directamente este. */
    int gradoEntrada(String nombre) {
        int c = 0;
        for (Arista a : aristas) {
            if (a.destino.equals(nombre)) c++;
        }
        return c;
    }

    /** Módulos de los que depende directamente {@code nombre} (predecesores). */
    List<String> dependenciasDirectas(String nombre) {
        List<String> r = new ArrayList<>();
        for (Arista a : aristas) {
            if (a.destino.equals(nombre)) r.add(a.origen);
        }
        return r;
    }

    // DFS: recorrido
    
    /**
     * Recorrido en profundidad (DFS) desde un vértice, sin cubrir el resto del grafo.
     *
     * @param inicio módulo de partida
     * @return vértices alcanzables, en el orden en que el DFS los descubre
     */
    List<String> recorridoDFS(String inicio) {
        if (!vertices.containsKey(inicio)) {
            throw new IllegalArgumentException("Vértice inexistente: " + inicio);
        }
        List<String> orden = new ArrayList<>();
        dfsSimple(inicio, new LinkedHashSet<>(), orden);
        return orden;
    }

    private void dfsSimple(String u, Set<String> visitados, List<String> orden) {
        visitados.add(u);
        orden.add(u);
        for (String v : adyacencia.get(u)) {
            if (!visitados.contains(v)) {
                dfsSimple(v, visitados, orden);
            }
        }
    }

    ResultadoDfs dfsCompleto() {
        ResultadoDfs r = new ResultadoDfs();
        Map<String, Color> color = new HashMap<>();
        for (String v : vertices.keySet()) {
            color.put(v, Color.BLANCO);
        }
        int[] reloj = {0};
        List<String> camino = new ArrayList<>();

        for (String v : vertices.keySet()) {
            if (color.get(v) == Color.BLANCO && !r.hayCiclo()) {
                visitar(v, color, reloj, camino, r);
            }
        }
        return r;
    }

    private void visitar(String u, Map<String, Color> color, int[] reloj,
                         List<String> camino, ResultadoDfs r) {
        color.put(u, Color.GRIS);
        r.descubrimiento.put(u, ++reloj[0]);
        r.preorden.add(u);
        camino.add(u);

        for (String v : adyacencia.get(u)) {
            if (r.hayCiclo()) return;

            if (color.get(v) == Color.GRIS) {
                // Arco de retroceso: v ya está en el camino actual -> ciclo
                List<String> ciclo = new ArrayList<>(camino.subList(camino.indexOf(v), camino.size()));
                ciclo.add(v);
                r.ciclo = ciclo;
                return;
            }
            if (color.get(v) == Color.BLANCO) {
                visitar(v, color, reloj, camino, r);
            }
        }

        if (r.hayCiclo()) return;
        color.put(u, Color.NEGRO);
        r.finalizacion.put(u, ++reloj[0]);
        r.postorden.add(u);
        camino.remove(camino.size() - 1); // retroceso (backtracking)
    }

    // Orden de instalación (ordenamiento topológico con DFS)
    
    List<String> ordenDeInstalacion() {
        ResultadoDfs r = dfsCompleto();
        if (r.hayCiclo()) {
            return Collections.emptyList();
        }
        List<String> orden = new ArrayList<>(r.postorden);
        Collections.reverse(orden);
        return orden;
    }

    List<String> ordenParaInstalar(String objetivo) {
        if (!vertices.containsKey(objetivo)) {
            throw new IllegalArgumentException("Vértice inexistente: " + objetivo);
        }
        Set<String> necesarios = new LinkedHashSet<>();
        recolectarDependencias(objetivo, necesarios);

        List<String> resultado = new ArrayList<>();
        for (String m : ordenDeInstalacion()) {
            if (necesarios.contains(m)) resultado.add(m);
        }
        return resultado;
    }

    /** DFS "hacia atrás": sigue las aristas en sentido contrario (de dependiente a dependencia). */
    private void recolectarDependencias(String u, Set<String> visitados) {
        visitados.add(u);
        for (String d : dependenciasDirectas(u)) {
            if (!visitados.contains(d)) {
                recolectarDependencias(d, visitados);
            }
        }
    }

    
    boolean esOrdenValido(List<String> orden) {
        if (orden.size() != vertices.size() || !orden.containsAll(vertices.keySet())) {
            return false;
        }
        Map<String, Integer> posicion = new HashMap<>();
        for (int i = 0; i < orden.size(); i++) {
            posicion.put(orden.get(i), i);
        }
        for (Arista a : aristas) {
            if (posicion.get(a.origen) >= posicion.get(a.destino)) return false;
        }
        return true;
    }

    // Clasificación formal del grafo

    /** @return el primer ciclo de dependencias encontrado, o lista vacía si es acíclico. */
    List<String> buscarCiclo() {
        return dfsCompleto().ciclo;
    }

    /** @return {@code true} si el grafo contiene al menos un ciclo. */
    boolean esCiclico() {
        return dfsCompleto().hayCiclo();
    }

    /** Densidad de un grafo dirigido: D = |A| / (n · (n − 1)). */
    double densidad() {
        int n = vertices.size();
        if (n < 2) return 0.0;
        return (double) aristas.size() / ((double) n * (n - 1));
    }

    /** @return {@code true} si la densidad alcanza el umbral {@link #UMBRAL_DENSIDAD}. */
    boolean esDenso() {
        return densidad() >= UMBRAL_DENSIDAD;
    }

    /** @return {@code true} si todas las aristas son dirigidas. */
    boolean esDirigido() {
        for (Arista a : aristas) {
            if (!a.dirigida) return false;
        }
        return !aristas.isEmpty();
    }

    /** @return {@code true} si alguna arista tiene un peso distinto de 1. */
    boolean esPonderado() {
        for (Arista a : aristas) {
            if (a.peso != 1.0) return true;
        }
        return false;
    }

    boolean esConexo() {
        if (vertices.isEmpty()) return true;
        String inicio = vertices.keySet().iterator().next();
        Set<String> visitados = new LinkedHashSet<>();
        dfsSinDireccion(inicio, visitados);
        return visitados.size() == vertices.size();
    }

    private void dfsSinDireccion(String u, Set<String> visitados) {
        visitados.add(u);
        for (String v : adyacencia.get(u)) {
            if (!visitados.contains(v)) dfsSinDireccion(v, visitados);
        }
        for (String v : dependenciasDirectas(u)) {
            if (!visitados.contains(v)) dfsSinDireccion(v, visitados);
        }
    }

    /** Módulos sin dependencias (grado de entrada 0): por ahí se puede empezar a instalar. */
    List<String> modulosRaiz() {
        List<String> r = new ArrayList<>();
        for (String v : vertices.keySet()) {
            if (gradoEntrada(v) == 0) r.add(v);
        }
        return r;
    }
}
