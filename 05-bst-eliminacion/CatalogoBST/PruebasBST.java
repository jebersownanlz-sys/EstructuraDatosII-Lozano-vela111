import java.util.List;

/** Pruebas sin librerías externas: java PruebasBST */
public class PruebasBST {
    private static int ok = 0, fallos = 0;

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) { ok++; System.out.println("  OK     " + nombre); }
        else           { fallos++; System.out.println("  FALLÓ  " + nombre); }
    }

    private static Producto p(String cod) { return new Producto(cod, "Item " + cod, 1000); }

    public static void main(String[] args) {
        System.out.println("== Pruebas CatalogoBST ==");

        // 1. Árbol vacío
        CatalogoBST vacio = new CatalogoBST();
        verificar("vacío: tamaño 0", vacio.tamano() == 0);
        verificar("vacío: altura 0", vacio.altura() == 0);
        verificar("vacío: buscar devuelve null", vacio.buscar("A") == null);

        // 2. Inserción y búsqueda básicas
        CatalogoBST t = new CatalogoBST();
        for (String c : new String[]{"M", "F", "T", "B", "H", "P", "Z"}) t.insertar(p(c));
        verificar("tamaño 7 tras insertar 7", t.tamano() == 7);
        verificar("altura 3 (árbol completo)", t.altura() == 3);
        verificar("busca raíz 'M'", t.buscar("M") != null && t.getComparacionesUltimaBusqueda() == 1);
        verificar("busca hoja 'Z' (3 comparaciones)", t.buscar("Z") != null && t.getComparacionesUltimaBusqueda() == 3);
        verificar("busca inexistente 'C' devuelve null", t.buscar("C") == null);
        verificar("inexistente 'C' recorre M,F,B (3 comp.)", t.getComparacionesUltimaBusqueda() == 3);

        // 3. Duplicados
        verificar("duplicado devuelve false", !t.insertar(p("M")));
        verificar("duplicado no cambia el tamaño", t.tamano() == 7);

        // 4. Inorden debe salir ordenado
        List<Producto> orden = t.inorden();
        boolean ordenado = true;
        for (int i = 1; i < orden.size(); i++)
            if (orden.get(i - 1).getCodigo().compareTo(orden.get(i).getCodigo()) >= 0) ordenado = false;
        verificar("inorden ascendente", ordenado && orden.size() == 7);

        // 5. Inserción ordenada -> árbol degenerado (lista enlazada)
        CatalogoBST asc = new CatalogoBST();
        for (int i = 1; i <= 50; i++) asc.insertar(p(String.format("C%03d", i)));
        verificar("ordenado: altura = n (50)", asc.altura() == 50);
        asc.buscar("C050");
        verificar("ordenado: buscar el último = 50 comparaciones", asc.getComparacionesUltimaBusqueda() == 50);

        // 6. Orden descendente también degenera
        CatalogoBST desc = new CatalogoBST();
        for (int i = 50; i >= 1; i--) desc.insertar(p(String.format("C%03d", i)));
        verificar("descendente: altura = n (50)", desc.altura() == 50);

        // 7. Altura mínima teórica
        verificar("alturaMinima(7) = 3", CatalogoBST.alturaMinima(7) == 3);
        verificar("alturaMinima(8) = 4", CatalogoBST.alturaMinima(8) == 4);
        verificar("alturaMinima(1000) = 10", CatalogoBST.alturaMinima(1000) == 10);

        // 8. Null
        boolean lanzo = false;
        try { t.insertar(null); } catch (IllegalArgumentException e) { lanzo = true; }
        verificar("insertar(null) lanza excepción", lanzo);

        System.out.println("\nResultado: " + ok + " OK, " + fallos + " fallos");
        if (fallos > 0) System.exit(1);
    }
}
