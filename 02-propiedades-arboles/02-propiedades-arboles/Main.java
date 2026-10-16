public class Main {

    public static void main(String[] args) {

        // ============================================================
        // 1. CREACIÓN DE LOS NODOS
        // ============================================================

        NodoGeneral<String> consolas =
                new NodoGeneral<>("Consolas de Videojuegos");

        // Nivel 1
        NodoGeneral<String> sony =
                new NodoGeneral<>("Sony");

        NodoGeneral<String> nintendo =
                new NodoGeneral<>("Nintendo");

        NodoGeneral<String> microsoft =
                new NodoGeneral<>("Microsoft");

        // Nivel 2
        NodoGeneral<String> ps5 =
                new NodoGeneral<>("PlayStation 5");

        NodoGeneral<String> ps4 =
                new NodoGeneral<>("PlayStation 4");

        NodoGeneral<String> portal =
                new NodoGeneral<>("PlayStation Portal");

        NodoGeneral<String> switchC =
                new NodoGeneral<>("Switch");

        NodoGeneral<String> wii =
                new NodoGeneral<>("Wii");

        NodoGeneral<String> xboxSeriesX =
                new NodoGeneral<>("Xbox Series X");

        NodoGeneral<String> xboxOne =
                new NodoGeneral<>("Xbox One");

        // Nivel 3
        NodoGeneral<String> ps5Slim =
                new NodoGeneral<>("PS5 Slim");

        NodoGeneral<String> ps5Pro =
                new NodoGeneral<>("PS5 Pro");

        NodoGeneral<String> ps4Pro =
                new NodoGeneral<>("PS4 Pro");

        // ============================================================
        // 2. CONSTRUCCIÓN DE LA JERARQUÍA
        // ============================================================

        consolas.agregarHijo(sony);
        consolas.agregarHijo(nintendo);
        consolas.agregarHijo(microsoft);

        sony.agregarHijo(ps5);
        sony.agregarHijo(ps4);
        sony.agregarHijo(portal);

        nintendo.agregarHijo(switchC);
        nintendo.agregarHijo(wii);

        microsoft.agregarHijo(xboxSeriesX);
        microsoft.agregarHijo(xboxOne);

        ps5.agregarHijo(ps5Slim);
        ps5.agregarHijo(ps5Pro);

        ps4.agregarHijo(ps4Pro);

        // ============================================================
        // 3. MOSTRAR EL ÁRBOL
        // ============================================================

        System.out.println("=== ÁRBOL DE CONSOLAS DE VIDEOJUEGOS ===");
        MetricasArbol.mostrarArbol(consolas, 0);

        // ============================================================
        // 4. PROPIEDADES GENERALES
        // ============================================================

        System.out.println();
        System.out.println("=== PROPIEDADES DEL ÁRBOL ===");

        System.out.println(
                "Raíz: " + consolas.getDato()
        );

        System.out.println(
                "Número total de nodos: "
                        + MetricasArbol.contarNodos(consolas)
        );

        System.out.println(
                "Número de hojas: "
                        + MetricasArbol.contarHojas(consolas)
        );

        System.out.println(
                "Grado del árbol: "
                        + MetricasArbol.gradoArbol(consolas)
        );

        System.out.println(
                "Altura del árbol: "
                        + MetricasArbol.altura(consolas)
        );

        System.out.println(
                "Número de niveles: "
                        + (MetricasArbol.altura(consolas) + 1)
        );

        // ============================================================
        // 5. PROFUNDIDAD DE CINCO NODOS
        // ============================================================

        System.out.println();
        System.out.println("=== PROFUNDIDAD DE CINCO NODOS ===");

        mostrarProfundidad(consolas, sony);
        mostrarProfundidad(consolas, ps5);
        mostrarProfundidad(consolas, ps5Slim);
        mostrarProfundidad(consolas, switchC);
        mostrarProfundidad(consolas, xboxOne);

        // ============================================================
        // 6. GRADO DE NODOS
        // ============================================================

        System.out.println();
        System.out.println("=== GRADO DE ALGUNOS NODOS ===");

        mostrarGrado(consolas);
        mostrarGrado(sony);
        mostrarGrado(ps5);
        mostrarGrado(ps4);
        mostrarGrado(ps5Slim);

        // ============================================================
        // 7. TABLA COMPLETA DE MÉTRICAS
        // ============================================================

        System.out.println();
        System.out.println("=== TABLA DE MÉTRICAS ===");
        MetricasArbol.mostrarTablaMetricas(consolas);

        // ============================================================
        // 8. DOS CAMINOS Y SU LONGITUD
        // ============================================================

        System.out.println();
        System.out.println("=== CAMINOS ===");

        mostrarCamino(consolas, ps5Pro);
        mostrarCamino(consolas, wii);

        // ============================================================
        // 8.1 EJERCICIO 1: PROFUNDIDAD DE CADA HOJA
        // ============================================================

        System.out.println();
        System.out.println("=== PROFUNDIDAD DE CADA HOJA ===");
        mostrarProfundidadHojas(consolas, consolas);

        // ============================================================
        // 9. COMPARACIÓN MANUAL VS. CÓDIGO
        // ============================================================

        System.out.println();
        System.out.println("=== COMPARACIÓN ===");

        int alturaManual = 3;
        int alturaCalculada =
                MetricasArbol.altura(consolas);

        System.out.println(
                "Altura calculada manualmente: "
                        + alturaManual
        );

        System.out.println(
                "Altura calculada por Java: "
                        + alturaCalculada
        );

        System.out.println(
                alturaManual == alturaCalculada
                        ? "Resultado: COINCIDEN."
                        : "Resultado: REVISAR CÁLCULO."
        );

        // ============================================================
        // 8.2 EJERCICIO 3: AGREGAR UN NUEVO NIVEL
        // ============================================================

        System.out.println();
        System.out.println("=== NUEVO NIVEL: Edición Digital ===");
        System.out.println("Altura ANTES: " + MetricasArbol.altura(consolas));
        System.out.println("Niveles ANTES: " + (MetricasArbol.altura(consolas) + 1));

        NodoGeneral<String> edicionDigital =
                new NodoGeneral<>("Edición Digital");
        ps5Slim.agregarHijo(edicionDigital);

        System.out.println("Altura DESPUÉS: " + MetricasArbol.altura(consolas));
        System.out.println("Niveles DESPUÉS: " + (MetricasArbol.altura(consolas) + 1));
        System.out.println("Altura de PS5 Slim: " + MetricasArbol.altura(ps5Slim));
        System.out.println("Altura de PlayStation 5: " + MetricasArbol.altura(ps5));
        System.out.println("Altura de Sony: " + MetricasArbol.altura(sony));
        System.out.println("Profundidad de Edición Digital: "
                + MetricasArbol.profundidad(consolas, edicionDigital));
        System.out.println("Altura de Nintendo (sin cambio): "
                + MetricasArbol.altura(nintendo));
    }

    private static void mostrarProfundidad(
            NodoGeneral<?> raiz,
            NodoGeneral<?> nodo) {

        System.out.printf(
                "%-28s -> %d%n",
                nodo.getDato(),
                MetricasArbol.profundidad(raiz, nodo)
        );
    }

    private static void mostrarProfundidadHojas(
            NodoGeneral<?> raiz,
            NodoGeneral<?> actual) {

        if (actual.esHoja()) {
            System.out.printf(
                    "%-28s -> profundidad %d%n",
                    actual.getDato(),
                    MetricasArbol.profundidad(raiz, actual)
            );
            return;
        }

        for (NodoGeneral<?> hijo : actual.getHijos()) {
            mostrarProfundidadHojas(raiz, hijo);
        }
    }

    private static void mostrarGrado(
            NodoGeneral<?> nodo) {

        System.out.printf(
                "%-28s -> %d%n",
                nodo.getDato(),
                MetricasArbol.gradoNodo(nodo)
        );
    }

    private static void mostrarCamino(
            NodoGeneral<?> raiz,
            NodoGeneral<?> destino) {

        var ruta =
                MetricasArbol.camino(raiz, destino);

        System.out.println(
                MetricasArbol.rutaComoTexto(ruta)
        );

        System.out.println(
                "Longitud: "
                        + MetricasArbol.longitudCamino(
                                raiz,
                                destino
                        )
                        + " aristas"
        );
    }
}
