class Vertice {

    /** Identificador único del módulo (se usa como llave en las aristas). */
    String nombre;

    /** Descripción breve de lo que hace el módulo. */
    String descripcion;

    /**
     * Crea un vértice.
     *
     * @param nombre      nombre único del módulo
     * @param descripcion para qué sirve el módulo
     */
    Vertice(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del vértice no puede ser vacío");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre + " (" + descripcion + ")";
    }
}
