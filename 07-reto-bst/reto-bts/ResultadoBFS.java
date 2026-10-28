import java.util.List;

/** Resultado de buscar el camino más corto entre dos usuarios. */
public class ResultadoBFS {
    private final String origen;
    private final String destino;
    private final int distancia;          // número de conexiones; -1 si no hay camino
    private final List<String> camino;    // origen ... destino (vacío si no hay camino)
    private final List<List<String>> niveles; // niveles explorados por BFS

    public ResultadoBFS(String origen, String destino, int distancia,
                        List<String> camino, List<List<String>> niveles) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.camino = camino;
        this.niveles = niveles;
    }

    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public int getDistancia() { return distancia; }
    public List<String> getCamino() { return camino; }
    public List<List<String>> getNiveles() { return niveles; }
    public boolean hayCamino() { return distancia >= 0; }
}
