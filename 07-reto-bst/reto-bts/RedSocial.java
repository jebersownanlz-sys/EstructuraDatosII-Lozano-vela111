import java.util.*;

/**
 * Red social simulada: grafo no dirigido (lista de adyacencia).
 * Cada usuario es un vértice; una amistad es una arista.
 */
public class RedSocial {

    // TreeMap/TreeSet: el recorrido es siempre en el mismo orden (alfabético).
    private final Map<String, Set<String>> amigos = new TreeMap<>();

    // ---------------- Construcción de la red ----------------
    public void agregarUsuario(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre de usuario inválido.");
        amigos.putIfAbsent(nombre, new TreeSet<>());
    }

    public void conectar(String a, String b) {
        if (a.equals(b))
            throw new IllegalArgumentException("Un usuario no puede conectarse consigo mismo.");
        agregarUsuario(a);
        agregarUsuario(b);
        amigos.get(a).add(b);
        amigos.get(b).add(a);   // amistad en ambos sentidos
    }

    // ---------------- Consultas ----------------
    public boolean existe(String u) { return amigos.containsKey(u); }
    public Set<String> usuarios() { return Collections.unmodifiableSet(amigos.keySet()); }
    public Set<String> amigosDe(String u) {
        verificar(u);
        return Collections.unmodifiableSet(amigos.get(u));
    }
    public boolean estanConectados(String a, String b) {
        return existe(a) && amigos.get(a).contains(b);
    }
    public int cantidadUsuarios() { return amigos.size(); }
    public int cantidadConexiones() {
        int suma = 0;
        for (Set<String> s : amigos.values()) suma += s.size();
        return suma / 2;
    }

    // ---------------- BFS ----------------

    /** Grados de separación (camino mínimo) entre dos usuarios. Se detiene al encontrar el destino. */
    public ResultadoBFS gradosDeSeparacion(String origen, String destino) {
        verificar(origen);
        verificar(destino);
        return bfs(origen, destino);
    }

    /** Recorrido por niveles completo desde un usuario (alcanza a toda su componente). */
    public List<List<String>> recorridoPorNiveles(String origen) {
        verificar(origen);
        return bfs(origen, null).getNiveles();
    }

    private ResultadoBFS bfs(String origen, String destino) {
        Map<String, String> padre = new HashMap<>();
        Map<String, Integer> nivelDe = new HashMap<>();
        Queue<String> cola = new LinkedList<>();

        cola.add(origen);
        nivelDe.put(origen, 0);
        padre.put(origen, null);

        boolean encontrado = origen.equals(destino);

        while (!cola.isEmpty() && !encontrado) {
            String actual = cola.poll();
            for (String vecino : amigos.get(actual)) {
                if (!nivelDe.containsKey(vecino)) {          // aún no visitado
                    nivelDe.put(vecino, nivelDe.get(actual) + 1);
                    padre.put(vecino, actual);
                    cola.add(vecino);
                    if (vecino.equals(destino)) {            // salida temprana
                        encontrado = true;
                        break;
                    }
                }
            }
        }

        // Agrupar los usuarios descubiertos por nivel
        int maxNivel = 0;
        for (int n : nivelDe.values()) maxNivel = Math.max(maxNivel, n);
        List<List<String>> niveles = new ArrayList<>();
        for (int i = 0; i <= maxNivel; i++) niveles.add(new ArrayList<>());
        for (Map.Entry<String, Integer> e : nivelDe.entrySet())
            niveles.get(e.getValue()).add(e.getKey());
        for (List<String> n : niveles) Collections.sort(n);

        // Reconstruir el camino siguiendo los padres hacia atrás
        List<String> camino = new ArrayList<>();
        int distancia = -1;
        if (destino != null && nivelDe.containsKey(destino)) {
            for (String v = destino; v != null; v = padre.get(v)) camino.add(v);
            Collections.reverse(camino);
            distancia = camino.size() - 1;
        }
        return new ResultadoBFS(origen, destino, distancia, camino, niveles);
    }

    private void verificar(String u) {
        if (!existe(u))
            throw new IllegalArgumentException("El usuario no existe: " + u);
    }
}
