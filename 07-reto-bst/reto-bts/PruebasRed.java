import java.util.*;

/** Pruebas sin librerías externas. Ejecutar: java PruebasRed */
public class PruebasRed {

    private static int total = 0, fallidas = 0;

    public static void main(String[] args) {
        RedSocial red = redBase();

        System.out.println("[Distancias básicas]");
        verificar("Ana -> Ana = 0", red.gradosDeSeparacion("Ana", "Ana").getDistancia() == 0);
        verificar("Ana -> Beto = 1 (amigos directos)", red.gradosDeSeparacion("Ana", "Beto").getDistancia() == 1);
        verificar("Ana -> Diego = 2", red.gradosDeSeparacion("Ana", "Diego").getDistancia() == 2);
        verificar("Ana -> Hugo = 4", red.gradosDeSeparacion("Ana", "Hugo").getDistancia() == 4);
        verificar("Ana -> Luis = 6 (seis grados)", red.gradosDeSeparacion("Ana", "Luis").getDistancia() == 6);
        verificar("Ana -> Marta = 7", red.gradosDeSeparacion("Ana", "Marta").getDistancia() == 7);

        System.out.println("[Camino]");
        ResultadoBFS r = red.gradosDeSeparacion("Ana", "Luis");
        verificar("el camino empieza en Ana y termina en Luis",
                r.getCamino().get(0).equals("Ana")
                        && r.getCamino().get(r.getCamino().size() - 1).equals("Luis"));
        verificar("el camino tiene distancia+1 usuarios", r.getCamino().size() == r.getDistancia() + 1);
        verificar("cada par consecutivo del camino son amigos", caminoValido(red, r.getCamino()));
        verificar("camino Ana->Diego = [Ana, Beto, Diego]",
                red.gradosDeSeparacion("Ana", "Diego").getCamino().equals(List.of("Ana", "Beto", "Diego")));

        System.out.println("[Simetría]");
        verificar("distancia(Ana,Luis) = distancia(Luis,Ana)",
                red.gradosDeSeparacion("Ana", "Luis").getDistancia()
                        == red.gradosDeSeparacion("Luis", "Ana").getDistancia());

        System.out.println("[Sin camino]");
        ResultadoBFS sin = red.gradosDeSeparacion("Ana", "Nora");
        verificar("Ana -> Nora (usuario aislado): distancia -1", sin.getDistancia() == -1);
        verificar("Ana -> Nora: hayCamino() es false y camino vacío", !sin.hayCamino() && sin.getCamino().isEmpty());
        verificar("Ana -> Pablo (otra isla): distancia -1", red.gradosDeSeparacion("Ana", "Pablo").getDistancia() == -1);
        verificar("Olga -> Pablo = 1", red.gradosDeSeparacion("Olga", "Pablo").getDistancia() == 1);

        System.out.println("[Recorrido por niveles desde Ana]");
        List<List<String>> n = red.recorridoPorNiveles("Ana");
        verificar("nivel 0 = [Ana]", n.get(0).equals(List.of("Ana")));
        verificar("nivel 1 = [Beto, Carla]", n.get(1).equals(List.of("Beto", "Carla")));
        verificar("nivel 2 = [Diego, Elena]", n.get(2).equals(List.of("Diego", "Elena")));
        verificar("nivel 3 = [Fabi, Gabo]", n.get(3).equals(List.of("Fabi", "Gabo")));
        verificar("nivel 4 = [Hugo, Irene]", n.get(4).equals(List.of("Hugo", "Irene")));
        verificar("nivel 5 = [Jorge, Karla]", n.get(5).equals(List.of("Jorge", "Karla")));
        verificar("nivel 6 = [Luis]", n.get(6).equals(List.of("Luis")));
        verificar("nivel 7 = [Marta]", n.get(7).equals(List.of("Marta")));
        verificar("hay 8 niveles (0..7)", n.size() == 8);

        System.out.println("[Errores]");
        verificar("usuario inexistente lanza IllegalArgumentException",
                lanza(() -> red.gradosDeSeparacion("Ana", "Zzz")));
        verificar("conectar un usuario consigo mismo lanza IllegalArgumentException",
                lanza(() -> new RedSocial().conectar("A", "A")));
        verificar("conexión duplicada no aumenta el conteo", conexionDuplicada());

        System.out.println("[Verificación cruzada con Floyd-Warshall]");
        verificar("BFS = Floyd-Warshall en TODOS los pares de la red base", comparaConFloyd(red));
        boolean todas = true;
        for (long semilla = 1; semilla <= 20; semilla++)
            todas &= comparaConFloyd(redAleatoria(30, 45, semilla));
        verificar("BFS = Floyd-Warshall en 20 redes aleatorias (30 usuarios, 45 conexiones)", todas);

        System.out.println("\nPruebas ejecutadas: " + total + " | Fallidas: " + fallidas);
        System.out.println(fallidas == 0 ? "RESULTADO: TODAS LAS PRUEBAS PASARON"
                                         : "RESULTADO: HAY PRUEBAS FALLIDAS");
        if (fallidas > 0) System.exit(1);
    }

    // ---------- Utilidades ----------

    private static RedSocial redBase() {
        RedSocial red = new RedSocial();
        String[][] a = {
            {"Ana","Beto"},{"Ana","Carla"},{"Beto","Diego"},{"Carla","Diego"},{"Carla","Elena"},
            {"Diego","Fabi"},{"Elena","Gabo"},{"Fabi","Hugo"},{"Gabo","Irene"},{"Hugo","Irene"},
            {"Hugo","Jorge"},{"Irene","Karla"},{"Jorge","Luis"},{"Karla","Luis"},{"Luis","Marta"}
        };
        for (String[] p : a) red.conectar(p[0], p[1]);
        red.agregarUsuario("Nora");
        red.conectar("Olga", "Pablo");
        return red;
    }

    private static RedSocial redAleatoria(int usuarios, int conexiones, long semilla) {
        Random rnd = new Random(semilla);
        RedSocial red = new RedSocial();
        for (int i = 0; i < usuarios; i++) red.agregarUsuario("U" + i);
        for (int k = 0; k < conexiones; k++) {
            int a = rnd.nextInt(usuarios), b = rnd.nextInt(usuarios);
            if (a != b) red.conectar("U" + a, "U" + b);
        }
        return red;
    }

    /** Calcula todas las distancias con Floyd-Warshall y las compara con BFS. */
    private static boolean comparaConFloyd(RedSocial red) {
        List<String> us = new ArrayList<>(red.usuarios());
        int n = us.size(), INF = 1_000_000;
        int[][] d = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(d[i], INF);
            d[i][i] = 0;
            for (String v : red.amigosDe(us.get(i))) d[i][us.indexOf(v)] = 1;
        }
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (d[i][k] + d[k][j] < d[i][j]) d[i][j] = d[i][k] + d[k][j];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int esperado = d[i][j] >= INF ? -1 : d[i][j];
                if (red.gradosDeSeparacion(us.get(i), us.get(j)).getDistancia() != esperado) return false;
            }
        return true;
    }

    private static boolean caminoValido(RedSocial red, List<String> camino) {
        for (int i = 0; i + 1 < camino.size(); i++)
            if (!red.estanConectados(camino.get(i), camino.get(i + 1))) return false;
        return true;
    }

    private static boolean conexionDuplicada() {
        RedSocial r = new RedSocial();
        r.conectar("A", "B");
        r.conectar("B", "A");
        r.conectar("A", "B");
        return r.cantidadConexiones() == 1;
    }

    private static boolean lanza(Runnable r) {
        try { r.run(); } catch (IllegalArgumentException e) { return true; }
        return false;
    }

    private static void verificar(String descripcion, boolean ok) {
        total++;
        if (ok) System.out.println("  OK     " + descripcion);
        else { fallidas++; System.out.println("  FALLÓ  " + descripcion); }
    }
}
