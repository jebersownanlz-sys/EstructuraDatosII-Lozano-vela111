public class Producto {
    private final int codigo;
    private final String nombre;
    private final double precio;

    public Producto(int codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return codigo + " (" + nombre + ", $" + (long) precio + ")";
    }
}
