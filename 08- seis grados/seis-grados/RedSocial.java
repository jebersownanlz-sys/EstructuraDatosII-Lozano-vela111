import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Red social simulada: un grafo NO dirigido y NO ponderado donde cada vertice
 * es un usuario y cada arista es una amistad (conexion directa).
 *
 * Se implementa con lista de adyacencia. Los amigos de cada usuario se guardan
 * en un TreeSet para recorrerlos siempre en orden alfabetico (resultados
 * deterministas y pruebas verificables).
 *
 * Complejidad del BFS: O(U + C), con U = usuarios y C = conexiones.
 */
class RedSocial {

    /** Resultado de un BFS completo desde un usuario origen. */
    static class Recorrido {
        /** Usuario desde el que se hizo el recorrido. */
        final String origen;
        /** Niveles del BFS: niveles.get(k) = usuarios a exactamente k conexiones del origen. */
        final List<List<String>> niveles = new ArrayList<>();
        /** Distancia (en conexiones) de cada usuario alcanzado. */
        final Map<String, Integer> distancia = new LinkedHashMap<>();
        /** Padre de cada usuario en el arbol BFS (el origen no tiene padre). */
        final Map<String, String> padre = new HashMap<>();

        Recorrido(String origen) {
            this.origen = origen;
        }

        /** Cantidad de usuarios alcanzados (incluye al origen). */
        int totalAlcanzados() {
            return distancia.size();
        }
    }

    /** Lista de adyacencia: usuario -> sus amigos directos. */
    private final Map<String, Set<String>> amigos = new TreeMap<>();

    private int numConexiones = 0;

    // ------------------------------------------------------------------
    // Construccion de la red
    // ------------------------------------------------------------------

    /** Agrega un usuario. Devuelve false si ya existia. */
    boolean agregarUsuario(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede ser vacio");
        }
        if (amigos.containsKey(nombre)) {
            return false;
        }
        amigos.put(nombre, new TreeSet<>());
        return true;
    }

    /**
     * Crea una amistad entre dos usuarios (en ambos sentidos). Si alguno no
     * existe, se registra. Ignora autoconexiones y amistades repetidas.
     *
     * @return true si se creo una conexion nueva
     */
    boolean conectar(String a, String b) {
        if (a == null || b == null || a.equals(b)) {
            return false;
        }
        agregarUsuario(a);
        agregarUsuario(b);
        if (amigos.get(a).contains(b)) {
            return false;
        }
        amigos.get(a).add(b);
        amigos.get(b).add(a);
        numConexiones++;
        return true;
    }

    // ------------------------------------------------------------------
    // Consultas basicas
    // ------------------------------------------------------------------

    boolean existe(String nombre) {
        return amigos.containsKey(nombre);
    }

    int numUsuarios() {
        return amigos.size();
    }

    int numConexiones() {
        return numConexiones;
    }

    /** Usuarios en orden alfabetico. */
    List<String> usuarios() {
        return new ArrayList<>(amigos.keySet());
    }

    /** Amigos directos de un usuario, en orden alfabetico. */
    List<String> amigosDe(String nombre) {
        validar(nombre);
        return new ArrayList<>(amigos.get(nombre));
    }

    boolean sonAmigos(String a, String b) {
        validar(a);
        validar(b);
        return amigos.get(a).contains(b);
    }

    private void validar(String nombre) {
        if (!amigos.containsKey(nombre)) {
            throw new IllegalArgumentException("Usuario inexistente: " + nombre);
        }
    }

    // ------------------------------------------------------------------
    // BFS
    // ------------------------------------------------------------------

    /**
     * Busqueda en anchura (BFS) completa desde un usuario.
     *
     * Se usa una cola (FIFO). Se procesa nivel por nivel: primero el origen
     * (nivel 0), luego sus amigos (nivel 1), luego los amigos de sus amigos
     * (nivel 2), y asi sucesivamente. Como BFS visita primero los vertices
     * mas cercanos, la primera vez que se llega a un usuario es por un camino
     * con el MINIMO numero de conexiones.
     */
    Recorrido recorridoPorNiveles(String origen) {
        validar(origen);
        Recorrido r = new Recorrido(origen);
        Deque<String> cola = new ArrayDeque<>();

        r.distancia.put(origen, 0);
        cola.add(origen);

        while (!cola.isEmpty()) {
            int tamanoNivel = cola.size(); // usuarios que hay en el nivel actual
            List<String> nivel = new ArrayList<>();

            for (int i = 0; i < tamanoNivel; i++) {
                String u = cola.poll();
                nivel.add(u);
                for (String v : amigos.get(u)) {
                    if (!r.distancia.containsKey(v)) { // aun no visitado
                        r.distancia.put(v, r.distancia.get(u) + 1);
                        r.padre.put(v, u);
                        cola.add(v);
                    }
                }
            }
            r.niveles.add(nivel);
        }
        return r;
    }

    /**
     * Numero minimo de conexiones para llegar de un usuario a otro.
     *
     * @return la distancia, o -1 si no hay ninguna cadena de amistades entre ellos
     */
    int distancia(String desde, String hasta) {
        validar(hasta);
        Integer d = recorridoPorNiveles(desde).distancia.get(hasta);
        return d == null ? -1 : d;
    }

    /**
     * Camino minimo entre dos usuarios, de origen a destino (ambos incluidos).
     * Se reconstruye siguiendo los padres del arbol BFS desde el destino.
     *
     * @return la lista de usuarios, o lista vacia si no hay camino
     */
    List<String> caminoMinimo(String desde, String hasta) {
        validar(hasta);
        Recorrido r = recorridoPorNiveles(desde);
        if (!r.distancia.containsKey(hasta)) {
            return new ArrayList<>();
        }
        return caminoDesde(r, hasta);
    }

    /** Reconstruye el camino origen -> destino a partir de un recorrido ya hecho. */
    static List<String> caminoDesde(Recorrido r, String destino) {
        List<String> camino = new ArrayList<>();
        for (String u = destino; u != null; u = r.padre.get(u)) {
            camino.add(u);
        }
        Collections.reverse(camino);
        return camino;
    }

    /** true si existe camino y tiene como maximo 6 conexiones ("seis grados"). */
    boolean cumpleSeisGrados(String a, String b) {
        int d = distancia(a, b);
        return d >= 0 && d <= 6;
    }

    /**
     * Mayor distancia entre dos usuarios conectados de la red (diametro).
     * Los pares sin camino no se cuentan.
     */
    int diametro() {
        int max = 0;
        for (String u : amigos.keySet()) {
            for (int d : recorridoPorNiveles(u).distancia.values()) {
                max = Math.max(max, d);
            }
        }
        return max;
    }
}
