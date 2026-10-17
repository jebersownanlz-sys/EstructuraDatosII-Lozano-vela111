import java.util.*;

public class Main {
    static ArbolBST construir(int[] datos) { ArbolBST a = new ArbolBST(); for (int d : datos) a.insertar(d); return a; }

    static void titulo(String t) { System.out.println("\n==== " + t + " ===="); }

    public static void main(String[] args) {
        // ================= GUÍA 4: Catálogo inteligente =================
        int[] aleatorios = {4520, 1830, 7210, 3050, 9100, 2210, 5870, 6400, 1200, 8300, 3900, 7800, 2900, 5100, 9700};
        int[] ordenados = aleatorios.clone(); Arrays.sort(ordenados);

        titulo("BST con 15 códigos en orden ALEATORIO");
        ArbolBST aleat = construir(aleatorios);
        System.out.println("Inserción: " + Arrays.toString(aleatorios));
        System.out.print(aleat);
        System.out.println("Inorden: " + aleat.inorden() + "  | ¿Cumple BST? " + aleat.esBST());

        titulo("BST con los mismos 15 códigos ORDENADOS");
        ArbolBST orden = construir(ordenados);
        System.out.println("Inserción: " + Arrays.toString(ordenados));
        System.out.print(orden);

        titulo("Cinco búsquedas (3 existentes, 2 inexistentes) en cada árbol");
        int[] busquedas = {5870, 1200, 9700, 4000, 9999};
        for (int b : busquedas) {
            boolean r1 = aleat.buscar(b); int c1 = aleat.getComparaciones();
            boolean r2 = orden.buscar(b); int c2 = orden.getComparaciones();
            System.out.printf("Buscar %d | aleatorio: %-12s comps=%2d | ordenado: %-12s comps=%2d%n",
                    b, r1 ? "encontrado" : "no encontrado", c1, r2 ? "encontrado" : "no encontrado", c2);
            System.out.println("   camino aleatorio: " + aleat.camino(b));
            System.out.println("   camino ordenado : " + orden.camino(b));
        }

        titulo("Tabla comparativa (n = 15)");
        System.out.printf("%-22s %-8s %-28s %-12s%n", "Tipo de inserción", "Altura", "Prom. comparaciones (éxito)", "Complejidad");
        System.out.printf("%-22s %-8d %-28.2f %-12s%n", "Aleatoria", aleat.altura(), aleat.promedioComparaciones(), "~O(log n)");
        System.out.printf("%-22s %-8d %-28.2f %-12s%n", "Ordenada", orden.altura(), orden.promedioComparaciones(), "O(n)");

        titulo("Ejemplo de la guía: 50,30,70,20,40,60,80");
        ArbolBST g = construir(new int[]{50, 30, 70, 20, 40, 60, 80});
        System.out.print(g);
        System.out.println("Buscar 60 -> " + g.camino(60) + " | Buscar 65 -> " + g.camino(65));

        // ================= GUÍA 5: Eliminación =================
        int[] base = {50, 30, 70, 20, 40, 60, 80};

        titulo("Caso 1: eliminar HOJA (20)");
        ArbolBST t1 = construir(base);
        System.out.println("ANTES:\n" + t1); t1.eliminar(20);
        System.out.println("DESPUÉS:\n" + t1 + "Inorden: " + t1.inorden() + " | ¿BST? " + t1.esBST());

        titulo("Caso 2: eliminar nodo con UN HIJO (60, que tiene hijo derecho 65)");
        ArbolBST t2 = construir(new int[]{50, 30, 70, 20, 40, 60, 80, 65});
        System.out.println("ANTES:\n" + t2); t2.eliminar(60);
        System.out.println("DESPUÉS:\n" + t2 + "Inorden: " + t2.inorden() + " | ¿BST? " + t2.esBST());

        titulo("Caso 3: eliminar nodo con DOS HIJOS (la raíz 50)");
        ArbolBST t3 = construir(base);
        System.out.println("ANTES:\n" + t3); t3.eliminar(50);
        System.out.println("DESPUÉS (nueva raíz = sucesor inorden 60):\n" + t3 + "Inorden: " + t3.inorden() + " | ¿BST? " + t3.esBST());

        titulo("Caso extra: dos hijos en nodo interno (70) y valor inexistente (99)");
        ArbolBST t4 = construir(base); t4.eliminar(70); t4.eliminar(99);
        System.out.print(t4); System.out.println("Inorden: " + t4.inorden() + " | ¿BST? " + t4.esBST());

        titulo("Prueba de robustez: eliminar todos los elementos del árbol aleatorio, validando BST en cada paso");
        ArbolBST r = construir(aleatorios); boolean ok = true;
        for (int v : aleatorios) { r.eliminar(v); ok &= r.esBST() && !r.buscar(v); }
        System.out.println("Árbol vacío al final: " + r.inorden().isEmpty() + " | Propiedad BST preservada siempre: " + ok);
    }
}
