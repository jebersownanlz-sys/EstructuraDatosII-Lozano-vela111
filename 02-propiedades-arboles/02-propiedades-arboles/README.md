# Propiedades de los árboles: Consolas de Videojuegos

Programa en Java que modela un **árbol general (n-ario)** con una jerarquía de consolas de videojuegos y calcula sus propiedades: nodos, hojas, grado, altura, profundidad, niveles y caminos.

## Estructura del árbol

```
Consolas de Videojuegos
├── Sony
│   ├── PlayStation 5
│   │   ├── PS5 Slim
│   │   └── PS5 Pro
│   ├── PlayStation 4
│   │   └── PS4 Pro
│   └── PlayStation Portal
├── Nintendo
│   ├── Switch
│   └── Wii
└── Microsoft
    ├── Xbox Series X
    └── Xbox One
```

## Archivos

| Archivo | Descripción |
|---|---|
| `NodoGeneral.java` | Nodo genérico `<T>` con un dato y una lista de hijos. |
| `MetricasArbol.java` | Métodos recursivos para calcular las propiedades del árbol. |
| `Main.java` | Construye el árbol, ejecuta las pruebas y los ejercicios. |
| `salida_ejecucion.txt` | Salida del programa al ejecutarlo. |
| `arbol_consolas.drawio` | Diagrama editable del árbol. |
| `tabla_metricas_manual.csv` | Tabla de métricas calculada a mano. |

## Convenciones usadas

- La **raíz** tiene profundidad 0.
- Una **hoja** tiene altura 0.
- La **altura** se mide en número de aristas.
- El **nivel** es igual a la profundidad.
- El **grado** de un nodo es su número de hijos directos.
- El **grado del árbol** es el mayor grado entre todos sus nodos.

## Resultados principales

| Propiedad | Valor |
|---|---|
| Raíz | Consolas de Videojuegos |
| Número de nodos | 14 |
| Número de hojas | 8 |
| Grado del árbol | 3 |
| Altura del árbol | 3 |
| Número de niveles | 4 |

La altura calculada a mano (3) coincide con la calculada por Java (3).

## Ejercicio del nuevo nivel

Se agrega el nodo **Edición Digital** como hijo de **PS5 Slim**:

- La altura del árbol pasa de 3 a 4 y los niveles de 4 a 5.
- Suben las alturas de PS5 Slim (1), PlayStation 5 (2) y Sony (3).
- La altura de Nintendo no cambia (1), porque su rama no se modificó.
- La profundidad de Edición Digital es 4.

## Cómo compilar y ejecutar

Desde la carpeta donde están los archivos `.java`:

```bash
javac *.java
java Main
```

Requiere tener instalado el JDK (Java 10 o superior, porque se usa `var`).
