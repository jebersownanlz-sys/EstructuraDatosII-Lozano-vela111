import java.util.List;

public class Main {

    public static void main(String[] args) {
        Grafo g = construirGrafoDeModulos();

        imprimirTablaVertices(g);
        imprimirTablaAristas(g);
        imprimirRecorridoDfs(g);
        imprimirOrdenDeInstalacion(g);
        imprimirClasificacion(g);
        imprimirInstalacionParcial(g, "Pagos");
        imprimirCasoConCiclo();
    }

    /** Construye el grafo de dependencias (13 vértices, 21 aristas dirigidas). */
    static Grafo construirGrafoDeModulos() {
        Grafo g = new Grafo();

        // ---- Vértices (13 módulos) ----
        g.agregarVertice(new Vertice("Nucleo", "librerías base y utilidades"));
        g.agregarVertice(new Vertice("Configuracion", "lectura de parámetros y variables de entorno"));
        g.agregarVertice(new Vertice("Logging", "registro de eventos y errores"));
        g.agregarVertice(new Vertice("BaseDatos", "conexión y acceso a datos"));
        g.agregarVertice(new Vertice("Seguridad", "cifrado y manejo de claves"));
        g.agregarVertice(new Vertice("Usuarios", "registro y perfil de usuarios"));
        g.agregarVertice(new Vertice("Autenticacion", "inicio de sesión y tokens"));
        g.agregarVertice(new Vertice("Catalogo", "productos y categorías"));
        g.agregarVertice(new Vertice("Carrito", "carrito de compras"));
        g.agregarVertice(new Vertice("Pagos", "pasarela de pagos"));
        g.agregarVertice(new Vertice("Notificaciones", "correos y avisos al cliente"));
        g.agregarVertice(new Vertice("ApiRest", "servicios web que exponen el sistema"));
        g.agregarVertice(new Vertice("Frontend", "interfaz web para el cliente"));

        // ---- Aristas dirigidas y no ponderadas (21): "origen se instala antes que destino" ----
        dep(g, "Nucleo", "Configuracion");
        dep(g, "Nucleo", "Logging");
        dep(g, "Configuracion", "BaseDatos");
        dep(g, "Configuracion", "Seguridad");
        dep(g, "Logging", "BaseDatos");
        dep(g, "BaseDatos", "Usuarios");
        dep(g, "BaseDatos", "Catalogo");
        dep(g, "Seguridad", "Autenticacion");
        dep(g, "Usuarios", "Autenticacion");
        dep(g, "Catalogo", "Carrito");
        dep(g, "Usuarios", "Carrito");
        dep(g, "Carrito", "Pagos");
        dep(g, "Autenticacion", "Pagos");
        dep(g, "Seguridad", "Pagos");
        dep(g, "Pagos", "Notificaciones");
        dep(g, "Usuarios", "Notificaciones");
        dep(g, "Autenticacion", "ApiRest");
        dep(g, "Carrito", "ApiRest");
        dep(g, "Pagos", "ApiRest");
        dep(g, "Notificaciones", "ApiRest");
        dep(g, "ApiRest", "Frontend");

        return g;
    }

    /** Atajo para agregar una dependencia dirigida con peso 1 (no ponderada). */
    private static void dep(Grafo g, String antes, String despues) {
        g.agregarArista(new Arista(antes, despues, 1.0, true));
    }

    // Salidas por consola

    static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    static void imprimirTablaVertices(Grafo g) {
        titulo("Vértices (|V| = " + g.numVertices() + ")");
        System.out.printf("%-3s %-15s %-8s %-8s %s%n", "N°", "Módulo", "G.entr", "G.sal", "Descripción");
        int i = 1;
        for (Vertice v : g.getVertices()) {
            System.out.printf("%-3d %-15s %-8d %-8d %s%n", i++, v.nombre,
                    g.gradoEntrada(v.nombre), g.gradoSalida(v.nombre), v.descripcion);
        }
    }

    static void imprimirTablaAristas(Grafo g) {
        titulo("Aristas (|A| = " + g.numAristas() + ")");
        System.out.printf("%-3s %-15s %-15s %-10s %s%n", "N°", "Origen", "Destino", "¿Dirigida?", "¿Ponderada?");
        int i = 1;
        for (Arista a : g.getAristas()) {
            System.out.printf("%-3d %-15s %-15s %-10s %s%n", i++, a.origen, a.destino,
                    a.dirigida ? "Sí" : "No", a.peso != 1.0 ? "Sí" : "No");
        }
    }

    static void imprimirRecorridoDfs(Grafo g) {
        titulo("Recorrido DFS desde 'Nucleo'");
        System.out.println(String.join(" -> ", g.recorridoDFS("Nucleo")));

        titulo("DFS completo: tiempos de descubrimiento / finalización");
        Grafo.ResultadoDfs r = g.dfsCompleto();
        System.out.printf("%-15s %-6s %s%n", "Módulo", "Desc.", "Final.");
        for (String v : r.preorden) {
            System.out.printf("%-15s %-6d %d%n", v, r.descubrimiento.get(v), r.finalizacion.get(v));
        }
        System.out.println("Preorden  : " + String.join(", ", r.preorden));
        System.out.println("Postorden : " + String.join(", ", r.postorden));
    }

    static void imprimirOrdenDeInstalacion(Grafo g) {
        titulo("RESULTADO: orden de instalación (postorden del DFS invertido)");
        List<String> orden = g.ordenDeInstalacion();
        if (orden.isEmpty()) {
            System.out.println("No existe un orden válido: hay dependencias circulares.");
            return;
        }
        int i = 1;
        for (String m : orden) {
            System.out.println(i++ + ". " + m);
        }
        System.out.println("¿Respeta todas las dependencias? " + (g.esOrdenValido(orden) ? "Sí" : "No"));
    }

    static void imprimirClasificacion(Grafo g) {
        titulo("Clasificación del grafo");
        int n = g.numVertices();
        System.out.println("Dirección   : " + (g.esDirigido() ? "dirigido" : "no dirigido / mixto"));
        System.out.println("Ponderación : " + (g.esPonderado() ? "ponderado" : "no ponderado"));
        System.out.println("Ciclos      : " + (g.esCiclico() ? "cíclico" : "acíclico (DAG)"));
        System.out.println("Conectividad: " + (g.esConexo() ? "conexo (débilmente)" : "no conexo"));
        System.out.printf("Densidad    : D = %d / (%d·%d) = %.3f -> %s%n",
                g.numAristas(), n, n - 1, g.densidad(), g.esDenso() ? "denso" : "disperso");
        System.out.println("Módulos raíz (sin dependencias): " + g.modulosRaiz());
    }

    static void imprimirInstalacionParcial(Grafo g, String objetivo) {
        titulo("Ejercicio: ¿qué instalo solo para tener '" + objetivo + "'?");
        List<String> orden = g.ordenParaInstalar(objetivo);
        System.out.println(String.join(" -> ", orden));
    }

    /** Caso de prueba: si se agrega una dependencia circular, el DFS la detecta. */
    static void imprimirCasoConCiclo() {
        titulo("Caso con ciclo: se agrega 'Autenticacion -> Seguridad'");
        Grafo g = construirGrafoDeModulos();
        g.agregarArista(new Arista("Autenticacion", "Seguridad", 1.0, true));

        List<String> ciclo = g.buscarCiclo();
        System.out.println("Ciclo detectado: " + String.join(" -> ", ciclo));
        System.out.println("Orden de instalación: "
                + (g.ordenDeInstalacion().isEmpty() ? "NO EXISTE (hay que romper el ciclo)" : "existe"));
    }
}
