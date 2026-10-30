class Arista {

    /** Módulo que debe instalarse primero (la dependencia). */
    String origen;

    /** Módulo que depende de {@code origen} y se instala después. */
    String destino;

    /** Peso de la arista; 1.0 en un grafo no ponderado. */
    double peso;

    /** {@code true} si la arista solo se recorre de origen a destino. */
    boolean dirigida;

    /**
     * Crea una arista.
     *
     * @param o   módulo origen (se instala primero)
     * @param d   módulo destino (depende del origen)
     * @param p   peso; debe ser positivo
     * @param dir {@code true} si es dirigida
     */
    Arista(String o, String d, double p, boolean dir) {
        if (o == null || d == null) {
            throw new IllegalArgumentException("Origen y destino son obligatorios");
        }
        if (o.equals(d)) {
            throw new IllegalArgumentException("Un módulo no puede depender de sí mismo: " + o);
        }
        if (p <= 0) {
            throw new IllegalArgumentException("El peso debe ser positivo: " + p);
        }
        origen = o;
        destino = d;
        peso = p;
        dirigida = dir;
    }

    @Override
    public String toString() {
        return origen + (dirigida ? " --> " : " --- ") + destino;
    }
}
