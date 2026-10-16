/**
 * Árbol de expresiones: los operadores son nodos internos y los
 * operandos son hojas.
 *
 * Ejemplo de una frutería:
 *   3 manzanas a $1500 y 2 piñas a $4000
 *   Total = (3 * 1500) + (2 * 4000)
 *
 *            +
 *          /   \
 *        *       *
 *       / \     / \
 *      3  1500 2  4000
 */
public class EjemploArbolExpresion {

    public static void main(String[] args) {

        NodoArbol<String> suma = new NodoArbol<>("+");
        NodoArbol<String> multManzanas = new NodoArbol<>("*");
        NodoArbol<String> multPinas = new NodoArbol<>("*");

        NodoArbol<String> cantManzanas = new NodoArbol<>("3");
        NodoArbol<String> precioManzana = new NodoArbol<>("1500");
        NodoArbol<String> cantPinas = new NodoArbol<>("2");
        NodoArbol<String> precioPina = new NodoArbol<>("4000");

        suma.setIzquierdo(multManzanas);
        suma.setDerecho(multPinas);

        multManzanas.setIzquierdo(cantManzanas);
        multManzanas.setDerecho(precioManzana);

        multPinas.setIzquierdo(cantPinas);
        multPinas.setDerecho(precioPina);

        ArbolBinario<String> arbol = new ArbolBinario<>(suma);

        System.out.println("=== ÁRBOL DE EXPRESIÓN: TOTAL DE LA COMPRA DE FRUTAS ===");
        System.out.println("3 manzanas a $1500 y 2 piñas a $4000");
        System.out.println("(el árbol se dibuja acostado: derecha arriba, izquierda abajo)");
        arbol.mostrar();

        System.out.println();
        System.out.println("=== RECORRIDOS ===");
        System.out.println("Preorden  (prefija):  " + arbol.preorden());
        System.out.println("Inorden   (infija):   " + arbol.inorden());
        System.out.println("Postorden (postfija): " + arbol.postorden());
        System.out.println("Con paréntesis: " + aTexto(suma));

        System.out.println();
        System.out.println("=== EVALUACIÓN ===");
        System.out.println(aTexto(suma) + " = " + evaluar(suma));

        System.out.println();
        System.out.println("=== PROPIEDADES ===");
        System.out.println("Nodos: " + arbol.contarNodos()
                + " | Hojas (operandos): " + arbol.contarHojas()
                + " | Altura: " + arbol.altura());
        System.out.println("Clasificación: " + arbol.clasificar());

        System.out.println();
        System.out.println("=== PRUEBA CON ÁRBOL VACÍO ===");
        System.out.println("Evaluar null -> " + evaluar(null));
    }

    /** Evalúa la expresión de forma recursiva (postorden). */
    public static int evaluar(NodoArbol<String> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.esHoja()) {
            return Integer.parseInt(nodo.getDato());
        }
        int izq = evaluar(nodo.getIzquierdo());
        int der = evaluar(nodo.getDerecho());

        switch (nodo.getDato()) {
            case "+": return izq + der;
            case "-": return izq - der;
            case "*": return izq * der;
            case "/": return der == 0 ? 0 : izq / der;
            default:
                throw new IllegalArgumentException(
                        "Operador desconocido: " + nodo.getDato());
        }
    }

    /** Escribe la expresión en notación infija con paréntesis. */
    public static String aTexto(NodoArbol<String> nodo) {
        if (nodo == null) {
            return "";
        }
        if (nodo.esHoja()) {
            return nodo.getDato();
        }
        return "(" + aTexto(nodo.getIzquierdo())
                + " " + nodo.getDato() + " "
                + aTexto(nodo.getDerecho()) + ")";
    }
}
