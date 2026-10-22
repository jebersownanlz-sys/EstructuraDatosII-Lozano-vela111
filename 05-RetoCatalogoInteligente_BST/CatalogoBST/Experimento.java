import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Compara ordenado vs aleatorio para varios tamaños y genera la tabla. */
public class Experimento {

    private static final String[] CATEGORIAS = {"ELE", "HOG", "OFI", "RED", "ALM"};
    private static final int REPETICIONES_ALEATORIAS = 30;  // se promedia

    /** Catálogo ya ordenado por código: ELE-0001, HOG-0002 ... */
    static List<Producto> generar(int n) {
        List<Producto> lista = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            String cat = CATEGORIAS[i % CATEGORIAS.length];
            lista.add(new Producto(String.format("P%05d-%s", i, cat), "Producto " + i, 1000 + i * 10));
        }
        return lista;   // el prefijo numérico garantiza orden lexicográfico = orden de creación
    }

    static CatalogoBST construir(List<Producto> productos) {
        CatalogoBST arbol = new CatalogoBST();
        for (Producto p : productos) arbol.insertar(p);
        return arbol;
    }

    /** Promedio de comparaciones buscando TODOS los códigos existentes. */
    static double promedioBusqueda(CatalogoBST arbol, List<Producto> productos) {
        long total = 0;
        for (Producto p : productos) {
            arbol.buscar(p.getCodigo());
            total += arbol.getComparacionesUltimaBusqueda();
        }
        return (double) total / productos.size();
    }

    public static void main(String[] args) {
        // ---------- Demo pequeña (12 productos) para ver la forma ----------
        List<Producto> demo = generar(12);
        List<Producto> demoAleatoria = new ArrayList<>(demo);
        Collections.shuffle(demoAleatoria, new Random(7));

        CatalogoBST aDemoOrd = construir(demo);
        CatalogoBST aDemoRnd = construir(demoAleatoria);

        System.out.println("=== DEMO CON 12 PRODUCTOS ===");
        System.out.println("\nOrden de inserción aleatorio:");
        for (Producto p : demoAleatoria) System.out.print(p.getCodigo() + "  ");
        System.out.println("\n\nBST aleatorio (altura " + aDemoRnd.altura() + "):");
        System.out.print(aDemoRnd.dibujar());
        System.out.println("\nBST ordenado (altura " + aDemoOrd.altura() + "):");
        System.out.print(aDemoOrd.dibujar());

        String objetivo = demo.get(11).getCodigo();   // el código mayor
        aDemoRnd.buscar(objetivo);
        int cRnd = aDemoRnd.getComparacionesUltimaBusqueda();
        aDemoOrd.buscar(objetivo);
        int cOrd = aDemoOrd.getComparacionesUltimaBusqueda();
        System.out.println("\nBuscar " + objetivo + ": aleatorio=" + cRnd
                + " comparaciones | ordenado=" + cOrd + " comparaciones");

        // ---------- Tabla comparativa ----------
        int[] tamanos = {15, 100, 500, 1000, 2000};
        System.out.println("\n=== TABLA COMPARATIVA (aleatorio = promedio de "
                + REPETICIONES_ALEATORIAS + " ejecuciones) ===\n");
        System.out.println("| n    | Altura mín. teórica | Altura ordenado | Altura aleatorio (prom.) "
                + "| Comp. prom. ordenado | Comp. prom. aleatorio | Factor de altura |");
        System.out.println("|------|---------------------|-----------------|--------------------------"
                + "|----------------------|-----------------------|------------------|");

        for (int n : tamanos) {
            List<Producto> base = generar(n);

            CatalogoBST ord = construir(base);
            double alturaOrd = ord.altura();
            double compOrd = promedioBusqueda(ord, base);

            double sumaAltura = 0, sumaComp = 0;
            for (int semilla = 1; semilla <= REPETICIONES_ALEATORIAS; semilla++) {
                List<Producto> mezcla = new ArrayList<>(base);
                Collections.shuffle(mezcla, new Random(semilla));
                CatalogoBST rnd = construir(mezcla);
                sumaAltura += rnd.altura();
                sumaComp += promedioBusqueda(rnd, base);
            }
            double alturaRnd = sumaAltura / REPETICIONES_ALEATORIAS;
            double compRnd = sumaComp / REPETICIONES_ALEATORIAS;

            System.out.printf("| %-4d | %-19d | %-15.0f | %-24.1f | %-20.1f | %-21.2f | %-16s |%n",
                    n, CatalogoBST.alturaMinima(n), alturaOrd, alturaRnd, compOrd, compRnd,
                    String.format("%.1fx", alturaOrd / alturaRnd));
        }

        System.out.println("\nConclusión: con datos ordenados el BST se convierte en una lista "
                + "(altura = n, búsqueda O(n)); con datos aleatorios la altura queda cerca de "
                + "O(log n), aproximadamente el doble de la mínima teórica.");
    }
}
