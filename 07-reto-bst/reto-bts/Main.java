import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        RedSocial red = crearRed();

        System.out.println("=== SEIS GRADOS DE SEPARACIÓN ===");
        System.out.println("Usuarios: " + red.cantidadUsuarios()
                + " | Conexiones: " + red.cantidadConexiones());

        System.out.println("\n--- Lista de amigos ---");
        for (String u : red.usuarios())
            System.out.println(u + " -> " + red.amigosDe(u));

        // Caso principal: el par más alejado
        mostrar(red, "Ana", "Luis", "recorrido_ana_luis.png");

        // Otros casos
        mostrar(red, "Ana", "Hugo", "recorrido_ana_hugo.png");
        mostrar(red, "Beto", "Elena", "recorrido_beto_elena.png");
        mostrar(red, "Ana", "Nora", "recorrido_ana_nora.png");   // sin camino

        System.out.println("\n--- Recorrido completo por niveles desde Ana ---");
        List<List<String>> niveles = red.recorridoPorNiveles("Ana");
        for (int i = 0; i < niveles.size(); i++)
            System.out.println("Nivel " + i + ": " + niveles.get(i));
    }

    private static void mostrar(RedSocial red, String a, String b, String png)
            throws IOException {

        ResultadoBFS r = red.gradosDeSeparacion(a, b);

        System.out.println("\n--- " + a + " -> " + b + " ---");
        if (r.hayCamino()) {
            System.out.println("Grados de separación: " + r.getDistancia());
            System.out.println("Camino: " + String.join(" -> ", r.getCamino()));
        } else {
            System.out.println("No existe camino: " + b + " no está conectado con " + a + ".");
        }
        System.out.println("Recorrido por niveles:");
        GraficoRed.imprimirNiveles(red, r);
        GraficoRed.dibujar(red, r, png);
        System.out.println("Gráfico guardado: " + png);
    }

    private static RedSocial crearRed() {
        RedSocial red = new RedSocial();
        String[][] amistades = {
            {"Ana", "Beto"},  {"Ana", "Carla"},
            {"Beto", "Diego"}, {"Carla", "Diego"}, {"Carla", "Elena"},
            {"Diego", "Fabi"}, {"Elena", "Gabo"},
            {"Fabi", "Hugo"},  {"Gabo", "Irene"}, {"Hugo", "Irene"},
            {"Hugo", "Jorge"}, {"Irene", "Karla"},
            {"Jorge", "Luis"}, {"Karla", "Luis"},
            {"Luis", "Marta"}
        };
        for (String[] par : amistades) red.conectar(par[0], par[1]);
        red.agregarUsuario("Nora");            // usuario sin amigos
        red.conectar("Olga", "Pablo");         // isla desconectada
        return red;
    }
}
