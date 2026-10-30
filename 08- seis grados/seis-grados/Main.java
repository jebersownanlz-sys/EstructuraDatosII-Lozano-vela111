import java.io.IOException;
import java.util.List;

/**
 * Reto "Seis grados de separacion" (Tries / Integracion BST).
 *
 * Construye una red social simulada de 22 usuarios y usa BFS para hallar el
 * numero minimo de conexiones entre dos usuarios, mostrando el recorrido por
 * niveles (en consola y en una imagen PNG) y ejecutando pruebas automaticas.
 *
 * Compilar: javac -encoding UTF-8 -d out *.java
 * Ejecutar: java -cp out Main
 */
public class Main {

    private static int pruebasOk = 0;
    private static int pruebasTotal = 0;

    public static void main(String[] args) throws IOException {
        RedSocial red = construirRed();

        System.out.println("=== Seis grados de separacion (BFS) ===");
        System.out.println("Usuarios: " + red.numUsuarios() + " | Conexiones: " + red.numConexiones());

        mostrarRecorrido(red, "Ana");
        mostrarCaminos(red);

        GraficoRecorrido.guardar(red, "Ana", "Ulises", "recorrido_Ana_Ulises.png");
        GraficoRecorrido.guardar(red, "Hugo", "Beto", "recorrido_Hugo_Beto.png");
        System.out.println();
        System.out.println("Imagenes generadas: recorrido_Ana_Ulises.png, recorrido_Hugo_Beto.png");

        ejecutarPruebas(red);

        if (pruebasOk != pruebasTotal) {
            System.exit(1);
        }
    }

    /** Red de 22 usuarios y 29 amistades, en grupos (colegio, universidad, trabajo...) unidos por puentes. */
    static RedSocial construirRed() {
        RedSocial r = new RedSocial();
        String[] conexiones = {
            // Colegio
            "Ana-Beto", "Ana-Carla", "Beto-Carla", "Beto-Diego",
            // Universidad
            "Carla-Elena", "Diego-Elena", "Elena-Fabian", "Elena-Gloria", "Fabian-Gloria", "Gloria-Hugo",
            // Trabajo
            "Fabian-Irene", "Hugo-Irene", "Irene-Jorge", "Irene-Karen", "Jorge-Karen", "Karen-Luis",
            // Barrio
            "Fabian-Marta", "Luis-Marta", "Marta-Nicolas", "Marta-Olga", "Nicolas-Olga", "Olga-Pablo",
            // Gimnasio
            "Irene-Rosa", "Pablo-Rosa", "Rosa-Sergio", "Rosa-Tatiana", "Sergio-Tatiana", "Rosa-Ulises",
            // Grupo aislado (sin ninguna conexion con el resto)
            "Vera-Walter"
        };
        for (String c : conexiones) {
            String[] p = c.split("-");
            r.conectar(p[0], p[1]);
        }
        return r;
    }

    // ------------------------------------------------------------------
    // Salida del recorrido
    // ------------------------------------------------------------------

    static void mostrarRecorrido(RedSocial red, String origen) {
        RedSocial.Recorrido r = red.recorridoPorNiveles(origen);
        System.out.println();
        System.out.println("--- Recorrido BFS por niveles desde " + origen + " ---");
        for (int k = 0; k < r.niveles.size(); k++) {
            System.out.printf("Nivel %d (%d): %s%n", k, r.niveles.get(k).size(), r.niveles.get(k));
        }
        System.out.println("Alcanzados: " + r.totalAlcanzados() + " de " + red.numUsuarios() + " usuarios");
    }

    static void mostrarCaminos(RedSocial red) {
        String[][] pares = {
            {"Ana", "Ulises"}, {"Ana", "Hugo"}, {"Beto", "Rosa"},
            {"Pablo", "Carla"}, {"Jorge", "Jorge"}, {"Ana", "Vera"}
        };
        System.out.println();
        System.out.println("--- Numero minimo de conexiones ---");
        for (String[] p : pares) {
            List<String> camino = red.caminoMinimo(p[0], p[1]);
            if (camino.isEmpty()) {
                System.out.printf("%s -> %s: sin conexion posible%n", p[0], p[1]);
            } else {
                System.out.printf("%s -> %s: %d conexiones | %s%n", p[0], p[1],
                        camino.size() - 1, String.join(" - ", camino));
            }
        }
        System.out.println("Diametro de la red (mayor distancia entre usuarios conectados): " + red.diametro());
    }

    // ------------------------------------------------------------------
    // Pruebas automaticas
    // ------------------------------------------------------------------

    static void verificar(String descripcion, boolean condicion) {
        pruebasTotal++;
        if (condicion) pruebasOk++;
        System.out.println((condicion ? "[OK]    " : "[FALLO] ") + descripcion);
    }

    static void ejecutarPruebas(RedSocial red) {
        System.out.println();
        System.out.println("--- Pruebas automaticas ---");

        verificar("la red tiene 22 usuarios y 29 conexiones",
                red.numUsuarios() == 22 && red.numConexiones() == 29);
        verificar("distancia(Ana, Ana) = 0", red.distancia("Ana", "Ana") == 0);
        verificar("distancia(Ana, Beto) = 1 (amigos directos)", red.distancia("Ana", "Beto") == 1);
        verificar("distancia(Ana, Diego) = 2 (amigo de un amigo)", red.distancia("Ana", "Diego") == 2);
        verificar("distancia(Ana, Ulises) = 6 (seis grados)", red.distancia("Ana", "Ulises") == 6);
        verificar("la distancia es simetrica: Ulises -> Ana = 6", red.distancia("Ulises", "Ana") == 6);
        verificar("distancia(Ana, Vera) = -1 (grupos desconectados)", red.distancia("Ana", "Vera") == -1);
        verificar("caminoMinimo(Ana, Vera) es vacio", red.caminoMinimo("Ana", "Vera").isEmpty());

        List<String> camino = red.caminoMinimo("Ana", "Ulises");
        verificar("camino Ana -> Ulises tiene 7 usuarios y empieza/termina bien",
                camino.size() == 7 && camino.get(0).equals("Ana") && camino.get(6).equals("Ulises"));
        boolean consecutivosAmigos = true;
        for (int i = 0; i + 1 < camino.size(); i++) {
            consecutivosAmigos &= red.sonAmigos(camino.get(i), camino.get(i + 1));
        }
        verificar("en el camino, cada par consecutivo son amigos directos", consecutivosAmigos);

        RedSocial.Recorrido r = red.recorridoPorNiveles("Ana");
        verificar("desde Ana hay 7 niveles (0 a 6) y el nivel 0 es [Ana]",
                r.niveles.size() == 7 && r.niveles.get(0).equals(List.of("Ana")));
        verificar("nivel 1 de Ana = [Beto, Carla]", r.niveles.get(1).equals(List.of("Beto", "Carla")));
        verificar("Ana alcanza 20 usuarios (22 menos el grupo Vera-Walter)", r.totalAlcanzados() == 20);

        int sumaNiveles = 0;
        for (List<String> nivel : r.niveles) sumaNiveles += nivel.size();
        verificar("ningun usuario se repite entre niveles", sumaNiveles == r.totalAlcanzados());

        verificar("el diametro de la red es 6 (se cumplen los seis grados)", red.diametro() == 6);
        verificar("cumpleSeisGrados(Ana, Ulises) es true y (Ana, Vera) es false",
                red.cumpleSeisGrados("Ana", "Ulises") && !red.cumpleSeisGrados("Ana", "Vera"));
        verificar("BFS coincide con Floyd-Warshall en todos los pares de usuarios", coincideConFloydWarshall(red));

        boolean lanzo = false;
        try {
            red.distancia("Ana", "Fantasma");
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar("usuario inexistente lanza IllegalArgumentException", lanzo);

        RedSocial pequena = new RedSocial();
        pequena.conectar("A", "B");
        boolean repetida = pequena.conectar("B", "A");
        boolean consigo = pequena.conectar("A", "A");
        verificar("conexion repetida o consigo mismo no se cuenta",
                !repetida && !consigo && pequena.numConexiones() == 1);

        System.out.println();
        System.out.printf("Resultado: %d/%d pruebas correctas%n", pruebasOk, pruebasTotal);
    }

    /** Verificacion independiente: Floyd-Warshall calcula todas las distancias y se compara con BFS. */
    static boolean coincideConFloydWarshall(RedSocial red) {
        List<String> us = red.usuarios();
        int n = us.size();
        final int INF = 1_000_000;
        int[][] d = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                d[i][j] = i == j ? 0 : (red.sonAmigos(us.get(i), us.get(j)) ? 1 : INF);
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][k] + d[k][j] < d[i][j]) d[i][j] = d[i][k] + d[k][j];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int esperado = d[i][j] >= INF ? -1 : d[i][j];
                if (red.distancia(us.get(i), us.get(j)) != esperado) return false;
            }
        }
        return true;
    }
}
