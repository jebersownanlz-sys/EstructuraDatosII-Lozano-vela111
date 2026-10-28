# Reto: "Seis grados de separación" (BFS)

Red social simulada en Java. Se usa **BFS (búsqueda en anchura)** para hallar el número mínimo de
conexiones entre dos usuarios, el camino exacto y el recorrido por niveles.

## Archivos

| Archivo | Función |
|---|---|
| `RedSocial.java` | Grafo no dirigido (lista de adyacencia) y algoritmo BFS |
| `ResultadoBFS.java` | Distancia, camino y niveles de una búsqueda |
| `GraficoRed.java` | Diagrama de niveles en consola y gráfico PNG del recorrido |
| `Main.java` | Demostración con 16 usuarios |
| `PruebasRed.java` | 29 pruebas (sin JUnit) |
| `salida_main.txt`, `salida_pruebas.txt` | Salidas reales de la ejecución |
| `recorrido_*.png` | Representación gráfica del recorrido |

## Compilar y ejecutar

```bash
javac *.java
java Main          # demostración + genera los PNG
java PruebasRed    # pruebas
```

## Cómo funciona el BFS

1. Se mete el usuario origen en una **cola** con nivel 0.
2. Se saca un usuario y se visitan sus amigos aún no visitados; cada uno queda a nivel + 1 y se guarda su **padre**.
3. Como la cola procesa nivel por nivel, **la primera vez que se llega a un usuario es por el camino más corto**.
4. Al descubrir el destino se detiene; el camino se reconstruye siguiendo los padres hacia atrás.
5. Si la cola se vacía sin llegar al destino, no hay camino (distancia -1).

Complejidad: **O(V + E)** (V usuarios, E conexiones) en tiempo y O(V) en espacio.

## Red de ejemplo

`Ana` y `Luis` están a **6 grados**: Ana → Beto → Diego → Fabi → Hugo → Jorge → Luis.
`Nora` (sin amigos) y `Olga–Pablo` (isla aparte) sirven para probar el caso "sin camino".

## Representación gráfica

Cada columna es un nivel del BFS. Gris = amistades, **rojo = camino mínimo**, verde = origen/destino.

## Pruebas

Distancias (0, 1, 2, 4, 6, 7), validez del camino, simetría, usuarios sin camino, niveles exactos,
errores (usuario inexistente, autoconexión) y **verificación cruzada con Floyd-Warshall** sobre todos
los pares de la red base y 20 redes aleatorias.
