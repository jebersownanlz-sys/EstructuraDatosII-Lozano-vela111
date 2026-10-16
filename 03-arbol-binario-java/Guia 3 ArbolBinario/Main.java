public class Main {

    public static void main(String[] args) {

        // ============================================================
        // 1. ÁRBOL DE FRUTAS DE 7 NODOS (construcción manual)
        // ============================================================
        //
        //                  Frutas
        //                /        \
        //          Cítricas      Tropicales
        //          /     \        /      \
        //      Naranja  Limón   Mango    Piña
        //
        NodoArbol<String> frutas = new NodoArbol<>("Frutas");
        NodoArbol<String> citricas = new NodoArbol<>("Cítricas");
        NodoArbol<String> tropicales = new NodoArbol<>("Tropicales");
        NodoArbol<String> naranja = new NodoArbol<>("Naranja");
        NodoArbol<String> limon = new NodoArbol<>("Limón");
        NodoArbol<String> mango = new NodoArbol<>("Mango");
        NodoArbol<String> pina = new NodoArbol<>("Piña");

        // Enlace de referencias izquierda / derecha
        frutas.setIzquierdo(citricas);
        frutas.setDerecho(tropicales);

        citricas.setIzquierdo(naranja);
        citricas.setDerecho(limon);

        tropicales.setIzquierdo(mango);
        tropicales.setDerecho(pina);

        ArbolBinario<String> arbolFrutas = new ArbolBinario<>(frutas);

        titulo("1. ÁRBOL DE FRUTAS (7 NODOS)");
        System.out.println("(el árbol se dibuja acostado: derecha arriba, izquierda abajo)");
        arbolFrutas.mostrar();

        titulo("2. imprimirRaizYHijos() - SOLO LA RAÍZ");
        arbolFrutas.imprimirRaizYHijos();

        titulo("3. TODOS LOS NODOS CON SUS HIJOS");
        arbolFrutas.imprimirTodosConHijos();

        titulo("4. PROPIEDADES Y RECORRIDOS");
        System.out.println("Raíz: " + arbolFrutas.getRaiz().getDato());
        System.out.println("Total de nodos: " + arbolFrutas.contarNodos());
        System.out.println("Hojas: " + arbolFrutas.contarHojas());
        System.out.println("Altura: " + arbolFrutas.altura());
        System.out.println("Preorden:   " + arbolFrutas.preorden());
        System.out.println("Inorden:    " + arbolFrutas.inorden());
        System.out.println("Postorden:  " + arbolFrutas.postorden());
        System.out.println("Por niveles: " + arbolFrutas.porNiveles());

        titulo("5. CLASIFICACIÓN DEL ÁRBOL DE 7 NODOS");
        System.out.println("¿Lleno?      " + arbolFrutas.esLleno());
        System.out.println("¿Completo?   " + arbolFrutas.esCompleto());
        System.out.println("¿Perfecto?   " + arbolFrutas.esPerfecto());
        System.out.println("¿Degenerado? " + arbolFrutas.esDegenerado());
        System.out.println("Clasificación: " + arbolFrutas.clasificar());

        // ============================================================
        // 2. CUATRO ÁRBOLES PEQUEÑOS DE CLASIFICACIÓN
        // ============================================================

        // --- LLENO (5 nodos): cada nodo tiene 0 o 2 hijos ---
        NodoArbol<String> l1 = new NodoArbol<>("Manzana");
        NodoArbol<String> l2 = new NodoArbol<>("Pera");
        NodoArbol<String> l3 = new NodoArbol<>("Uva");
        NodoArbol<String> l4 = new NodoArbol<>("Kiwi");
        NodoArbol<String> l5 = new NodoArbol<>("Fresa");
        l1.setIzquierdo(l2);
        l1.setDerecho(l3);
        l2.setIzquierdo(l4);
        l2.setDerecho(l5);
        ArbolBinario<String> lleno = new ArbolBinario<>(l1);

        // --- COMPLETO (6 nodos): niveles llenos de izquierda a derecha ---
        NodoArbol<String> c1 = new NodoArbol<>("Manzana");
        NodoArbol<String> c2 = new NodoArbol<>("Pera");
        NodoArbol<String> c3 = new NodoArbol<>("Uva");
        NodoArbol<String> c4 = new NodoArbol<>("Kiwi");
        NodoArbol<String> c5 = new NodoArbol<>("Fresa");
        NodoArbol<String> c6 = new NodoArbol<>("Cereza");
        c1.setIzquierdo(c2);
        c1.setDerecho(c3);
        c2.setIzquierdo(c4);
        c2.setDerecho(c5);
        c3.setIzquierdo(c6);
        ArbolBinario<String> completo = new ArbolBinario<>(c1);

        // --- PERFECTO (7 nodos): es el árbol de frutas de la sección 1 ---
        ArbolBinario<String> perfecto = arbolFrutas;

        // --- DEGENERADO (5 nodos): cada nodo tiene un solo hijo ---
        NodoArbol<String> d1 = new NodoArbol<>("Sandía");
        NodoArbol<String> d2 = new NodoArbol<>("Melón");
        NodoArbol<String> d3 = new NodoArbol<>("Mora");
        NodoArbol<String> d4 = new NodoArbol<>("Durazno");
        NodoArbol<String> d5 = new NodoArbol<>("Ciruela");
        d1.setDerecho(d2);
        d2.setDerecho(d3);
        d3.setDerecho(d4);
        d4.setDerecho(d5);
        ArbolBinario<String> degenerado = new ArbolBinario<>(d1);

        titulo("6. CUATRO ÁRBOLES DE CLASIFICACIÓN");
        mostrarClasificacion("Árbol LLENO", lleno);
        mostrarClasificacion("Árbol COMPLETO", completo);
        mostrarClasificacion("Árbol PERFECTO", perfecto);
        mostrarClasificacion("Árbol DEGENERADO", degenerado);

        // ============================================================
        // 3. MANEJO DE null (evitar NullPointerException)
        // ============================================================

        titulo("7. PRUEBAS DE null");

        ArbolBinario<String> vacio = new ArbolBinario<>();
        System.out.println("-- Árbol vacío --");
        vacio.imprimirRaizYHijos();
        System.out.println("Nodos: " + vacio.contarNodos()
                + " | Altura: " + vacio.altura()
                + " | Clasificación: " + vacio.clasificar());

        System.out.println();
        System.out.println("-- Imprimir un nodo null --");
        arbolFrutas.imprimirRaizYHijos(null);

        System.out.println();
        System.out.println("-- Hoja vs referencia null --");
        System.out.println("Naranja es hoja: " + naranja.esHoja());
        System.out.println("naranja == null: " + (naranja == null)
                + "  (el nodo Naranja SÍ existe)");
        NodoArbol<String> inexistente = naranja.getIzquierdo();
        System.out.println("naranja.getIzquierdo() == null: " + (inexistente == null)
                + "  (no hay nodo a la izquierda)");

        System.out.println();
        System.out.println("-- Acceso seguro con validación --");
        if (naranja.getIzquierdo() != null) {
            System.out.println("Hijo izquierdo: " + naranja.getIzquierdo().getDato());
        } else {
            System.out.println("Naranja no tiene hijo izquierdo: se evita el error.");
        }

        System.out.println();
        System.out.println("-- Qué pasaría SIN validar --");
        try {
            System.out.println(naranja.getIzquierdo().getDato());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException capturado: "
                    + "se intentó usar un hijo que es null.");
        }

        // ============================================================
        // 4. MODELO EN MEMORIA: LAS REFERENCIAS APUNTAN A OBJETOS
        // ============================================================

        titulo("8. REFERENCIAS EN MEMORIA");
        System.out.println("frutas.getIzquierdo() == citricas   -> "
                + (frutas.getIzquierdo() == citricas));
        System.out.println("frutas.getDerecho() == tropicales   -> "
                + (frutas.getDerecho() == tropicales));
        System.out.println("citricas.getDerecho() == limon      -> "
                + (citricas.getDerecho() == limon));
        System.out.println("tropicales.getIzquierdo() == mango  -> "
                + (tropicales.getIzquierdo() == mango));
        System.out.println("tropicales.getIzquierdo() == citricas -> "
                + (tropicales.getIzquierdo() == citricas)
                + "  (son nodos distintos)");
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void mostrarClasificacion(
            String nombre, ArbolBinario<String> arbol) {

        System.out.println();
        System.out.println("--- " + nombre + " ---");
        arbol.mostrar();
        System.out.println("Nodos: " + arbol.contarNodos()
                + " | Altura: " + arbol.altura());
        System.out.println("Lleno: " + arbol.esLleno()
                + " | Completo: " + arbol.esCompleto()
                + " | Perfecto: " + arbol.esPerfecto()
                + " | Degenerado: " + arbol.esDegenerado());
        System.out.println("Clasificación: " + arbol.clasificar());
    }
}
