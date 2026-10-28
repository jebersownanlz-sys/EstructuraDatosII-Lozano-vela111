/** Producto del catálogo. El código (String) es la clave del BST. */
public class Producto {
    private final String codigo;
    private final String nombre;
    private final int precio;

    public Producto(String codigo, String nombre, int precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getPrecio()    { return precio; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " ($" + precio + ")";
    }
}
