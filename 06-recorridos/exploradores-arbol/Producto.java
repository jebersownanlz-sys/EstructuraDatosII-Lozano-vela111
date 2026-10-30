/**
 * Producto de la tienda. Su codigo es la clave por la que se ordena el BST.
 */
class Producto {

    private final int codigo;
    private final String nombre;
    private final double precio;

    Producto(int codigo, String nombre, double precio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo debe ser positivo: " + codigo);
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacio");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo: " + precio);
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    int getCodigo() {
        return codigo;
    }

    String getNombre() {
        return nombre;
    }

    double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return codigo + "-" + nombre;
    }
}
