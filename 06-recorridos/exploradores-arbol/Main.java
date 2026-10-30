import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * Actividad "Exploradores del arbol" (Guia 6 - Recorridos en arboles binarios).
 *
 * Construye un BST de 10 productos y ejecuta los cuatro recorridos sobre el MISMO
 * arbol: preorden, inorden, postorden y por niveles. Comprueba que inorden devuelve
 * los codigos ordenados y ejecuta pruebas automaticas.
 *
 * Compilar: javac -encoding UTF-8 -d out *.java
 * Ejecutar: java -cp out Main
 */
public class Main {

    private static int pruebasOk = 0;
    private static int pruebasTotal = 0;

    public static void main(String[] args) throws IOException {
        ArbolProductos arbol = construirArbol();

        mostrarProductos();
        mostrarRecorridos(arbol);
        comprobarInorden(arbol);
        mostrarUsos();

        DibujoArbol.guardar(arbol, "arbol.png");
        System.out.println();
        System.out.println("Imagen generada: arbol.png");

        ejecutarPruebas();

        if (pruebasOk != pruebasTotal) {
            System.exit(1);
        }
    }

    /** Orden de insercion que define la forma del arbol (no esta ordenado a proposito). */
    static Producto[] productos() {
        return new Producto[] {
            new Producto(50, "Arroz", 4800),
            new Producto(30, "Leche", 4200),
            new Producto(70, "Pan", 3500),
            new Producto(20, "Huevos", 14000),
            new Producto(40, "Aceite", 12500),
            new Producto(60, "Sal", 1800),
            new Producto(80, "Azucar", 5200),
            new Producto(35, "Cafe", 9800),
            new Producto(65, "Atun", 6900),
            new Producto(90, "Jabon", 3100)
        };
    }

    static ArbolProductos construirArbol() {
        ArbolProductos arbol = new ArbolProductos();
        for (Producto p : productos()) {
            arbol.insertar(p);
        }
        return arbol;
    }

    // ------------------------------------------------------------------
    // Salidas
    // ------------------------------------------------------------------

    static void titulo(String t) {
        System.out.println();
        System.out.println("=== " + t + " ===");
    }

    static void mostrarProductos() {
        titulo("Productos (en el orden en que se insertan)");
        System.out.printf("%-8s %-10s %s%n", "Codigo", "Nombre", "Precio");
        for (Producto p : productos()) {
            System.out.printf("%-8d %-10s %.0f%n", p.getCodigo(), p.getNombre(), p.getPrecio());
        }
    }

    static void mostrarRecorridos(ArbolProductos arbol) {
        titulo("Los cuatro recorridos sobre el mismo arbol (" + arbol.tamano() + " nodos, altura "
                + arbol.altura() + " niveles)");
        System.out.println("Preorden   (raiz-izq-der): " + DibujoArbol.codigos(arbol.preorden()));
        System.out.println("Inorden    (izq-raiz-der): " + DibujoArbol.codigos(arbol.inorden()));
        System.out.println("Postorden  (izq-der-raiz): " + DibujoArbol.codigos(arbol.postorden()));
        System.out.println("Por niveles (cola)       : " + DibujoArbol.codigos(arbol.porNiveles()));

        System.out.println();
        System.out.println("Por niveles, agrupado:");
        List<List<Producto>> niveles = arbol.porNivelesAgrupados();
        for (int k = 0; k < niveles.size(); k++) {
            System.out.println("  Nivel " + k + ": " + niveles.get(k));
        }

        System.out.println();
        System.out.println("Con nombres, inorden: " + arbol.inorden());
    }

    static void comprobarInorden(ArbolProductos arbol) {
        titulo("Comprobacion: inorden vs lista ordenada");
        List<Integer> deInorden = codigos(arbol.inorden());
        List<Integer> ordenada = new ArrayList<>();
        for (Producto p : productos()) ordenada.add(p.getCodigo());
        Collections.sort(ordenada);

        System.out.println("Inorden        : " + deInorden);
        System.out.println("Lista ordenada : " + ordenada);
        System.out.println("Son iguales?   : " + (deInorden.equals(ordenada) ? "SI" : "NO"));
    }

    static void mostrarUsos() {
        titulo("Que informacion produce cada recorrido y para que sirve");
        System.out.println("Preorden   : la raiz primero. Sirve para copiar o guardar el arbol (reinsertar en este orden da el mismo BST).");
        System.out.println("Inorden    : en un BST, los codigos de menor a mayor. Sirve para listados ordenados y rangos.");
        System.out.println("Postorden  : los hijos antes que el padre. Sirve para eliminar/liberar el arbol y calcular totales.");
        System.out.println("Por niveles: capa por capa desde la raiz. Sirve para ver la forma del arbol y la altura.");
    }

    // ------------------------------------------------------------------
    // Pruebas automaticas
    // ------------------------------------------------------------------

    static void verificar(String descripcion, boolean condicion) {
        pruebasTotal++;
        if (condicion) pruebasOk++;
        System.out.println((condicion ? "[OK]    " : "[FALLO] ") + descripcion);
    }

    static List<Integer> codigos(List<Producto> lista) {
        List<Integer> r = new ArrayList<>();
        for (Producto p : lista) r.add(p.getCodigo());
        return r;
    }

    static boolean estrictamenteCreciente(List<Integer> l) {
        for (int i = 0; i + 1 < l.size(); i++) {
            if (l.get(i) >= l.get(i + 1)) return false;
        }
        return true;
    }

    static void ejecutarPruebas() {
        titulo("Pruebas automaticas");
        ArbolProductos a = construirArbol();

        verificar("el arbol tiene 10 productos y 4 niveles", a.tamano() == 10 && a.altura() == 4);
        verificar("preorden = [50, 30, 20, 40, 35, 70, 60, 65, 80, 90]",
                codigos(a.preorden()).equals(List.of(50, 30, 20, 40, 35, 70, 60, 65, 80, 90)));
        verificar("inorden = [20, 30, 35, 40, 50, 60, 65, 70, 80, 90]",
                codigos(a.inorden()).equals(List.of(20, 30, 35, 40, 50, 60, 65, 70, 80, 90)));
        verificar("postorden = [20, 35, 40, 30, 65, 60, 90, 80, 70, 50]",
                codigos(a.postorden()).equals(List.of(20, 35, 40, 30, 65, 60, 90, 80, 70, 50)));
        verificar("por niveles = [50, 30, 70, 20, 40, 60, 80, 35, 65, 90]",
                codigos(a.porNiveles()).equals(List.of(50, 30, 70, 20, 40, 60, 80, 35, 65, 90)));

        List<Integer> ordenada = new ArrayList<>();
        for (Producto p : productos()) ordenada.add(p.getCodigo());
        Collections.sort(ordenada);
        verificar("inorden es igual a la lista de codigos ordenada", codigos(a.inorden()).equals(ordenada));
        verificar("inorden esta estrictamente creciente", estrictamenteCreciente(codigos(a.inorden())));

        verificar("preorden y por niveles empiezan en la raiz (50); postorden termina en la raiz",
                a.preorden().get(0).getCodigo() == 50 && a.porNiveles().get(0).getCodigo() == 50
                        && a.postorden().get(9).getCodigo() == 50);

        List<List<Producto>> niv = a.porNivelesAgrupados();
        verificar("niveles con 1, 2, 4 y 3 nodos",
                niv.size() == 4 && niv.get(0).size() == 1 && niv.get(1).size() == 2
                        && niv.get(2).size() == 4 && niv.get(3).size() == 3);

        TreeSet<Integer> conjunto = new TreeSet<>(codigos(a.preorden()));
        verificar("los cuatro recorridos visitan exactamente los mismos 10 nodos",
                conjunto.equals(new TreeSet<>(codigos(a.postorden())))
                        && conjunto.equals(new TreeSet<>(codigos(a.porNiveles())))
                        && conjunto.equals(new TreeSet<>(codigos(a.inorden()))) && conjunto.size() == 10);

        // Reinsertar el preorden reconstruye el mismo arbol
        ArbolProductos copia = new ArbolProductos();
        for (Producto p : a.preorden()) copia.insertar(p);
        verificar("reinsertar el preorden reconstruye el mismo arbol (mismo preorden y por niveles)",
                codigos(copia.preorden()).equals(codigos(a.preorden()))
                        && codigos(copia.porNiveles()).equals(codigos(a.porNiveles())));

        verificar("buscar(35) = Cafe y buscar(99) = null",
                a.buscar(35).getNombre().equals("Cafe") && a.buscar(99) == null);
        verificar("insertar un codigo repetido devuelve false y no cambia el tamano",
                !a.insertar(new Producto(50, "Otro", 1)) && a.tamano() == 10);

        ArbolProductos vacio = new ArbolProductos();
        verificar("arbol vacio: los cuatro recorridos devuelven lista vacia y altura 0",
                vacio.preorden().isEmpty() && vacio.inorden().isEmpty() && vacio.postorden().isEmpty()
                        && vacio.porNiveles().isEmpty() && vacio.altura() == 0);

        ArbolProductos uno = new ArbolProductos();
        uno.insertar(new Producto(7, "Unico", 1));
        verificar("arbol de un solo nodo: los cuatro recorridos devuelven [7]",
                codigos(uno.preorden()).equals(List.of(7)) && codigos(uno.inorden()).equals(List.of(7))
                        && codigos(uno.postorden()).equals(List.of(7)) && codigos(uno.porNiveles()).equals(List.of(7)));

        ArbolProductos cadena = new ArbolProductos();
        for (int c = 1; c <= 5; c++) cadena.insertar(new Producto(c, "P" + c, 1));
        verificar("arbol degenerado (1..5 en orden): altura 5, inorden ordenado, postorden inverso",
                cadena.altura() == 5 && codigos(cadena.inorden()).equals(List.of(1, 2, 3, 4, 5))
                        && codigos(cadena.postorden()).equals(List.of(5, 4, 3, 2, 1)));

        // Prueba aleatoria con semilla fija: 200 arboles de hasta 60 codigos
        Random rnd = new Random(42);
        boolean todoBien = true;
        for (int t = 0; t < 200 && todoBien; t++) {
            ArbolProductos r = new ArbolProductos();
            TreeSet<Integer> esperado = new TreeSet<>();
            int n = 1 + rnd.nextInt(60);
            for (int i = 0; i < n; i++) {
                int c = 1 + rnd.nextInt(200);
                r.insertar(new Producto(c, "P" + c, 1));
                esperado.add(c);
            }
            List<Integer> ino = codigos(r.inorden());
            todoBien = ino.equals(new ArrayList<>(esperado))
                    && r.tamano() == esperado.size()
                    && codigos(r.preorden()).size() == esperado.size()
                    && codigos(r.postorden()).get(esperado.size() - 1) == codigos(r.preorden()).get(0).intValue()
                    && codigos(r.porNiveles()).get(0) == codigos(r.preorden()).get(0).intValue();
        }
        verificar("200 arboles aleatorios: inorden siempre es igual al conjunto ordenado (TreeSet)", todoBien);

        List<Integer> liberados = codigos(a.vaciar());
        verificar("vaciar() libera en postorden y deja el arbol vacio",
                liberados.equals(List.of(20, 35, 40, 30, 65, 60, 90, 80, 70, 50))
                        && a.estaVacio() && a.tamano() == 0);

        boolean lanzo = false;
        try {
            new Producto(-1, "Malo", 1);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar("un producto con codigo negativo lanza IllegalArgumentException", lanzo);

        System.out.println();
        System.out.printf("Resultado: %d/%d pruebas correctas%n", pruebasOk, pruebasTotal);
    }
}
