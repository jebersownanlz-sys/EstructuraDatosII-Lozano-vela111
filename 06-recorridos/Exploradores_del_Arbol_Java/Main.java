public class Main {
    public static void main(String[] args) {

        ArbolBSTProducto arbol = new ArbolBSTProducto();

        // Orden de inserción que genera el árbol de la actividad
        arbol.insertar(new Producto(50, "Laptop",     2500));
        arbol.insertar(new Producto(30, "Monitor",     800));
        arbol.insertar(new Producto(70, "Tablet",     1200));
        arbol.insertar(new Producto(20, "Teclado",     300));
        arbol.insertar(new Producto(40, "Impresora",   600));
        arbol.insertar(new Producto(60, "Cámara",      900));
        arbol.insertar(new Producto(80, "Celular",    1500));
        arbol.insertar(new Producto(10, "Mouse",       150));
        arbol.insertar(new Producto(25, "Audífonos",   250));

        System.out.println("=== EXPLORADORES DEL ÁRBOL ===\n");

        arbol.preorden();      // Raíz - Izq - Der
        arbol.inorden();       // Izq - Raíz - Der
        arbol.postorden();     // Izq - Der - Raíz
        arbol.porNiveles();    // BFS

        System.out.println("\nCódigos en Inorden: " + arbol.codigosInorden());
        System.out.println("¿Inorden devuelve las claves ordenadas? "
                + (arbol.inordenEstaOrdenado() ? "SÍ" : "NO"));
    }
}
