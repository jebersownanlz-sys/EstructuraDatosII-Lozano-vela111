import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Dibuja el recorrido BFS por niveles y lo guarda como imagen PNG.
 *
 * Cada columna es un nivel (0 = origen, 1 = amigos directos, ...).
 * - Lineas grises: amistades de la red que no se usaron en el BFS.
 * - Lineas azules: aristas del arbol BFS (por donde se descubrio a cada usuario).
 * - Lineas rojas gruesas: camino minimo hacia el destino elegido.
 * - Ultima columna (gris): usuarios que no se pueden alcanzar desde el origen.
 */
class GraficoRecorrido {

    private static final int ANCHO_NODO = 92;
    private static final int ALTO_NODO = 30;
    private static final int ANCHO_COL = 150;
    private static final int ALTO_FILA = 52;
    private static final int MARGEN = 40;
    private static final int ENCABEZADO = 90;

    /**
     * Genera la imagen.
     *
     * @param red     red social
     * @param origen  usuario de partida del BFS
     * @param destino usuario cuyo camino minimo se resalta (puede ser null)
     * @param archivo ruta del PNG de salida
     */
    static void guardar(RedSocial red, String origen, String destino, String archivo) throws IOException {
        System.setProperty("java.awt.headless", "true");
        RedSocial.Recorrido r = red.recorridoPorNiveles(origen);

        // Usuarios que el BFS no alcanzo: van en una columna aparte
        List<String> sinAlcance = new java.util.ArrayList<>();
        for (String u : red.usuarios()) {
            if (!r.distancia.containsKey(u)) sinAlcance.add(u);
        }

        int columnas = r.niveles.size() + (sinAlcance.isEmpty() ? 0 : 1);
        int maxFilas = sinAlcance.size();
        for (List<String> nivel : r.niveles) maxFilas = Math.max(maxFilas, nivel.size());

        int ancho = MARGEN * 2 + columnas * ANCHO_COL;
        int alto = ENCABEZADO + maxFilas * ALTO_FILA + MARGEN;
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);

        // ---- Posiciones (centro de cada nodo) ----
        Map<String, int[]> pos = new HashMap<>();
        for (int k = 0; k < r.niveles.size(); k++) {
            colocarColumna(pos, r.niveles.get(k), k, maxFilas);
        }
        if (!sinAlcance.isEmpty()) {
            colocarColumna(pos, sinAlcance, r.niveles.size(), maxFilas);
        }

        // ---- Caminos y aristas especiales ----
        Set<String> aristasArbol = new HashSet<>();
        for (Map.Entry<String, String> e : r.padre.entrySet()) {
            aristasArbol.add(clave(e.getKey(), e.getValue()));
        }
        Set<String> aristasCamino = new HashSet<>();
        List<String> camino = null;
        if (destino != null && r.distancia.containsKey(destino)) {
            camino = RedSocial.caminoDesde(r, destino);
            for (int i = 0; i + 1 < camino.size(); i++) {
                aristasCamino.add(clave(camino.get(i), camino.get(i + 1)));
            }
        }

        // ---- Titulo ----
        g.setColor(new Color(30, 40, 70));
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        String titulo = "Recorrido BFS por niveles desde " + origen;
        if (destino != null) {
            titulo += "  |  camino minimo a " + destino + ": "
                    + (camino == null ? "no existe" : (camino.size() - 1) + " conexiones");
        }
        g.drawString(titulo, MARGEN, 32);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(Color.DARK_GRAY);
        g.drawString("gris = amistad no usada   azul = arbol BFS   rojo = camino minimo", MARGEN, 54);

        // ---- Encabezados de columnas ----
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        for (int k = 0; k < columnas; k++) {
            boolean esSinAlcance = k >= r.niveles.size();
            String t = esSinAlcance ? "Sin conexion" : (k == 0 ? "Nivel 0 (origen)" : "Nivel " + k);
            g.setColor(esSinAlcance ? Color.GRAY : new Color(30, 40, 70));
            FontMetrics fm = g.getFontMetrics();
            int cx = MARGEN + k * ANCHO_COL + ANCHO_COL / 2;
            g.drawString(t, cx - fm.stringWidth(t) / 2, ENCABEZADO - 18);
        }

        // ---- Aristas: primero las grises, luego azules, luego rojas ----
        for (String u : red.usuarios()) {
            for (String v : red.amigosDe(u)) {
                if (u.compareTo(v) < 0) {
                    String k = clave(u, v);
                    if (!aristasArbol.contains(k) && !aristasCamino.contains(k)) {
                        linea(g, pos, u, v, new Color(200, 200, 205), 1.2f);
                    }
                }
            }
        }
        for (String k : aristasArbol) {
            if (!aristasCamino.contains(k)) {
                String[] p = k.split("\\|");
                linea(g, pos, p[0], p[1], new Color(70, 120, 210), 2.2f);
            }
        }
        for (String k : aristasCamino) {
            String[] p = k.split("\\|");
            linea(g, pos, p[0], p[1], new Color(215, 40, 40), 4.5f);
        }

        // ---- Nodos ----
        Set<String> enCamino = camino == null ? new HashSet<>() : new HashSet<>(camino);
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        for (Map.Entry<String, int[]> e : pos.entrySet()) {
            String u = e.getKey();
            int x = e.getValue()[0] - ANCHO_NODO / 2;
            int y = e.getValue()[1] - ALTO_NODO / 2;
            boolean alcanzado = r.distancia.containsKey(u);

            Color fondo;
            if (!alcanzado) fondo = new Color(225, 225, 228);
            else if (u.equals(origen)) fondo = new Color(255, 214, 90);
            else if (enCamino.contains(u)) fondo = new Color(255, 190, 190);
            else fondo = new Color(220, 232, 255);

            g.setColor(fondo);
            g.fillRoundRect(x, y, ANCHO_NODO, ALTO_NODO, 16, 16);
            g.setColor(enCamino.contains(u) ? new Color(215, 40, 40) : new Color(50, 70, 120));
            g.setStroke(new BasicStroke(enCamino.contains(u) ? 2.5f : 1.5f));
            g.drawRoundRect(x, y, ANCHO_NODO, ALTO_NODO, 16, 16);

            g.setColor(alcanzado ? Color.BLACK : Color.GRAY);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(u, e.getValue()[0] - fm.stringWidth(u) / 2, e.getValue()[1] + 5);
        }

        g.dispose();
        ImageIO.write(img, "png", new File(archivo));
    }

    private static void colocarColumna(Map<String, int[]> pos, List<String> usuarios, int col, int maxFilas) {
        int cx = MARGEN + col * ANCHO_COL + ANCHO_COL / 2;
        double offset = (maxFilas - usuarios.size()) / 2.0; // centra la columna verticalmente
        for (int i = 0; i < usuarios.size(); i++) {
            int cy = ENCABEZADO + (int) ((i + offset) * ALTO_FILA) + ALTO_FILA / 2;
            pos.put(usuarios.get(i), new int[] {cx, cy});
        }
    }

    private static String clave(String a, String b) {
        return a.compareTo(b) < 0 ? a + "|" + b : b + "|" + a;
    }

    private static void linea(Graphics2D g, Map<String, int[]> pos, String a, String b, Color c, float grosor) {
        int[] pa = pos.get(a);
        int[] pb = pos.get(b);
        g.setColor(c);
        g.setStroke(new BasicStroke(grosor));
        g.drawLine(pa[0], pa[1], pb[0], pb[1]);
    }
}
