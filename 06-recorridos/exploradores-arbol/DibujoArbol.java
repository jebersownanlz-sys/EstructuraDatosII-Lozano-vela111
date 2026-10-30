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
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Dibuja el arbol de productos y las secuencias de los cuatro recorridos en un PNG.
 * La posicion horizontal de cada nodo es su lugar en el recorrido inorden y la
 * vertical es su nivel (profundidad).
 */
class DibujoArbol {

    private static final int COL = 78;
    private static final int FILA = 95;
    private static final int MARGEN = 50;
    private static final int TOP = 70;

    static void guardar(ArbolProductos arbol, String archivo) throws IOException {
        System.setProperty("java.awt.headless", "true");
        List<Producto> inorden = arbol.inorden();
        int niveles = arbol.altura();

        int ancho = MARGEN * 2 + Math.max(inorden.size(), 1) * COL;
        int alto = TOP + niveles * FILA + 210;
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);

        g.setColor(new Color(30, 40, 70));
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("Arbol binario de busqueda de productos (clave = codigo)", MARGEN, 34);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(Color.DARK_GRAY);
        g.drawString("Izquierda = codigos menores, derecha = codigos mayores. Cada fila es un nivel.", MARGEN, 54);

        // Posiciones
        Map<Integer, int[]> pos = new HashMap<>();
        for (int i = 0; i < inorden.size(); i++) {
            pos.put(inorden.get(i).getCodigo(), new int[] {MARGEN + i * COL + COL / 2, 0});
        }
        asignarNivel(arbol.getRaiz(), 0, pos);

        // Aristas
        g.setStroke(new BasicStroke(2f));
        g.setColor(new Color(110, 120, 150));
        aristas(g, arbol.getRaiz(), pos);

        // Nodos
        for (Producto p : inorden) {
            int[] c = pos.get(p.getCodigo());
            int x = c[0];
            int y = TOP + c[1] * FILA + 30;
            g.setColor(c[1] == 0 ? new Color(255, 214, 90) : new Color(220, 232, 255));
            g.fillOval(x - 26, y - 26, 52, 52);
            g.setColor(new Color(50, 70, 120));
            g.setStroke(new BasicStroke(2f));
            g.drawOval(x - 26, y - 26, 52, 52);

            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 15));
            centrar(g, String.valueOf(p.getCodigo()), x, y + 5);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g.setColor(new Color(60, 60, 60));
            centrar(g, p.getNombre(), x, y + 44);
        }

        // Secuencias
        int y0 = TOP + niveles * FILA + 40;
        String[][] filas = {
            {"Preorden (raiz-izq-der)", codigos(arbol.preorden())},
            {"Inorden (izq-raiz-der)", codigos(arbol.inorden())},
            {"Postorden (izq-der-raiz)", codigos(arbol.postorden())},
            {"Por niveles (cola)", codigos(arbol.porNiveles())}
        };
        for (int i = 0; i < filas.length; i++) {
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            g.setColor(new Color(30, 40, 70));
            g.drawString(filas[i][0] + ":", MARGEN, y0 + i * 34);
            g.setFont(new Font("Monospaced", Font.PLAIN, 14));
            g.setColor(Color.BLACK);
            g.drawString(filas[i][1], MARGEN + 210, y0 + i * 34);
        }

        g.dispose();
        ImageIO.write(img, "png", new File(archivo));
    }

    private static void asignarNivel(NodoArbol<Producto> n, int nivel, Map<Integer, int[]> pos) {
        if (n == null) return;
        pos.get(n.getDato().getCodigo())[1] = nivel;
        asignarNivel(n.getIzquierdo(), nivel + 1, pos);
        asignarNivel(n.getDerecho(), nivel + 1, pos);
    }

    private static void aristas(Graphics2D g, NodoArbol<Producto> n, Map<Integer, int[]> pos) {
        if (n == null) return;
        int[] a = pos.get(n.getDato().getCodigo());
        for (NodoArbol<Producto> h : new NodoArbol[] {n.getIzquierdo(), n.getDerecho()}) {
            if (h != null) {
                int[] b = pos.get(h.getDato().getCodigo());
                g.drawLine(a[0], TOP + a[1] * FILA + 30, b[0], TOP + b[1] * FILA + 30);
                aristas(g, h, pos);
            }
        }
    }

    private static void centrar(Graphics2D g, String t, int cx, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(t, cx - fm.stringWidth(t) / 2, y);
    }

    static String codigos(List<Producto> lista) {
        StringBuilder sb = new StringBuilder();
        for (Producto p : lista) {
            if (sb.length() > 0) sb.append("  ");
            sb.append(p.getCodigo());
        }
        return sb.toString();
    }
}
