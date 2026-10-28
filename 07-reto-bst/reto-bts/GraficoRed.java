import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/** Dibuja el recorrido BFS por niveles y resalta el camino más corto (PNG). */
public class GraficoRed {

    /** Imprime los niveles en consola como diagrama de texto. */
    public static void imprimirNiveles(RedSocial red, ResultadoBFS r) {
        Set<String> enCamino = new HashSet<>(r.getCamino());
        for (int i = 0; i < r.getNiveles().size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (String u : r.getNiveles().get(i))
                sb.append(enCamino.contains(u) ? "[" + u + "*] " : "(" + u + ") ");
            System.out.printf("Nivel %d: %s%n", i, sb.toString().trim());
        }
        if (r.hayCamino()) {
            System.out.println("  * = usuario del camino mínimo");
        }
    }

    /** Genera un PNG: columnas = niveles BFS, aristas grises = amistades, rojo = camino mínimo. */
    public static void dibujar(RedSocial red, ResultadoBFS r, String archivo) throws IOException {
        System.setProperty("java.awt.headless", "true");

        List<List<String>> niveles = r.getNiveles();
        int maxFilas = 1;
        for (List<String> n : niveles) maxFilas = Math.max(maxFilas, n.size());

        int anchoCol = 140, altoFila = 70, margen = 60, radio = 24;
        int ancho = margen * 2 + anchoCol * (niveles.size() - 1) + 2 * radio;
        int alto = margen * 2 + 40 + altoFila * (maxFilas - 1) + 2 * radio;

        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, ancho, alto);

        // Posición de cada usuario: x según el nivel, y centrado dentro del nivel
        Map<String, Point> pos = new HashMap<>();
        for (int i = 0; i < niveles.size(); i++) {
            List<String> n = niveles.get(i);
            int x = margen + radio + i * anchoCol;
            int altoNivel = altoFila * (n.size() - 1);
            int y0 = margen + 40 + radio + (altoFila * (maxFilas - 1) - altoNivel) / 2;
            for (int j = 0; j < n.size(); j++) pos.put(n.get(j), new Point(x, y0 + j * altoFila));
        }

        g.setColor(new Color(30, 30, 30));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g.drawString("BFS desde " + r.getOrigen()
                + (r.hayCamino() ? " hasta " + r.getDestino() + "  |  distancia = " + r.getDistancia()
                                 : "  |  sin camino a " + r.getDestino()), 20, 28);

        // Encabezado de cada nivel
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        g.setColor(new Color(110, 110, 110));
        for (int i = 0; i < niveles.size(); i++)
            g.drawString("Nivel " + i, margen + i * anchoCol + 4, 52);

        // Aristas (amistades entre usuarios dibujados)
        g.setStroke(new BasicStroke(1.5f));
        g.setColor(new Color(200, 200, 200));
        for (String a : pos.keySet())
            for (String b : red.amigosDe(a))
                if (pos.containsKey(b) && a.compareTo(b) < 0)
                    g.drawLine(pos.get(a).x, pos.get(a).y, pos.get(b).x, pos.get(b).y);

        // Camino mínimo en rojo
        List<String> camino = r.getCamino();
        g.setStroke(new BasicStroke(4f));
        g.setColor(new Color(214, 40, 40));
        for (int i = 0; i + 1 < camino.size(); i++) {
            Point p = pos.get(camino.get(i)), q = pos.get(camino.get(i + 1));
            g.drawLine(p.x, p.y, q.x, q.y);
        }

        // Nodos
        Set<String> enCamino = new HashSet<>(camino);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        for (Map.Entry<String, Point> e : pos.entrySet()) {
            String u = e.getKey(); Point p = e.getValue();
            boolean extremo = u.equals(r.getOrigen()) || u.equals(r.getDestino());
            Color relleno = extremo ? new Color(46, 125, 50)
                          : enCamino.contains(u) ? new Color(214, 40, 40)
                          : new Color(66, 133, 244);
            g.setColor(relleno);
            g.fillOval(p.x - radio, p.y - radio, 2 * radio, 2 * radio);
            g.setColor(Color.WHITE);
            g.drawString(u, p.x - fm.stringWidth(u) / 2, p.y + 4);
        }

        // Leyenda
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        int ly = alto - 14;
        g.setColor(new Color(46, 125, 50));  g.fillOval(20, ly - 10, 12, 12);
        g.setColor(Color.DARK_GRAY);         g.drawString("origen / destino", 38, ly);
        g.setColor(new Color(214, 40, 40));  g.fillOval(160, ly - 10, 12, 12);
        g.setColor(Color.DARK_GRAY);         g.drawString("camino mínimo", 178, ly);
        g.setColor(new Color(66, 133, 244)); g.fillOval(280, ly - 10, 12, 12);
        g.setColor(Color.DARK_GRAY);         g.drawString("otros usuarios explorados", 298, ly);

        g.dispose();
        ImageIO.write(img, "png", new File(archivo));
    }
}
