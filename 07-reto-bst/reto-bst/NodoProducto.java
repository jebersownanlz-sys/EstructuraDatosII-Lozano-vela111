public class NodoProducto {
    private Producto producto;
    private NodoProducto izquierdo;
    private NodoProducto derecho;

    public NodoProducto(Producto producto) {
        this.producto = producto;
        this.izquierdo = null;
        this.derecho = null;
    }

    public Producto getProducto() { return producto; }
    public NodoProducto getIzquierdo() { return izquierdo; }
    public NodoProducto getDerecho() { return derecho; }

    public void setIzquierdo(NodoProducto izq) { this.izquierdo = izq; }
    public void setDerecho(NodoProducto der) { this.derecho = der; }
}
