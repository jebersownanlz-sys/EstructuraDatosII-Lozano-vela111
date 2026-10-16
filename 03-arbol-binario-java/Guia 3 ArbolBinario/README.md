# Guía 3 – Árbol binario y modelado en Java

**Asignatura:** Estructura de Datos II  
**Programa:** Ingeniería de Software – Semestre IV  
**Ecosistema:** Java / GitHub  
**Laboratorio:** "Mi primer árbol binario" (tema: árbol de frutas)

## Objetivo

Diseñar e implementar de forma nativa un árbol binario utilizando referencias izquierda y derecha en Java.

## Archivos

| Archivo | Contenido |
|---|---|
| `NodoArbol.java` | Clase genérica `NodoArbol<T>`: dato, referencias izquierda/derecha, constructor, getters/setters, `esHoja()` y `cantidadHijos()`. |
| `ArbolBinario.java` | Guarda la raíz. Incluye `imprimirRaizYHijos()`, recorridos, altura, conteos y clasificación (lleno, completo, perfecto, degenerado). |
| `Main.java` | Construye manualmente el árbol de frutas de 7 nodos, los 4 árboles de clasificación y las pruebas de `null`. |
| `EjemploArbolExpresion.java` | Árbol de expresiones: `(3 * 1500) + (2 * 4000)`. |
| `salida_esperada.txt` | Salida de `Main`. |
| `salida_arbol_expresion.txt` | Salida de `EjemploArbolExpresion`. |

## Cómo compilar y ejecutar

```bash
javac NodoArbol.java ArbolBinario.java Main.java EjemploArbolExpresion.java
java Main
java EjemploArbolExpresion
```

Si en la consola se ven caracteres raros (tildes), ejecutar con:

```bash
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 Main
```

## Árbol de frutas (7 nodos)

```text
                 Frutas
               /        \
         Cítricas      Tropicales
         /     \        /      \
    Naranja   Limón   Mango    Piña
```

Clasificación: **perfecto** (también es lleno y completo): todos los nodos internos tienen 2 hijos y las 4 hojas están en el mismo nivel (nivel 2).

## Clasificación de árboles binarios

```text
LLENO (5 nodos)        COMPLETO (6 nodos)     PERFECTO (7 nodos)     DEGENERADO (5 nodos)
Cada nodo tiene        Niveles llenos de      Internos con 2 hijos   Cada nodo tiene
0 o 2 hijos            izquierda a derecha    y hojas al mismo nivel un solo hijo

      Manzana               Manzana                 Frutas               Sandía
      /     \               /     \               /      \                  \
   Pera     Uva          Pera     Uva        Cítricas  Tropicales          Melón
   /  \                  /  \     /          /   \      /    \                 \
Kiwi Fresa            Kiwi Fresa Cereza   Naranja Limón Mango Piña            Mora
                                                                                 \
                                                                                Durazno
                                                                                   \
                                                                                 Ciruela
```

| Tipo | Regla | Ejemplo |
|---|---|---|
| Lleno | Cada nodo tiene 0 o 2 hijos | Manzana, Pera, Uva, Kiwi, Fresa |
| Completo | Todos los niveles se llenan de izquierda a derecha | Manzana, Pera, Uva, Kiwi, Fresa, Cereza |
| Perfecto | Internos con 2 hijos y todas las hojas al mismo nivel | Árbol de frutas de 7 nodos |
| Degenerado | Cada nodo tiene un solo hijo (parece una lista) | Sandía → Melón → Mora → Durazno → Ciruela |

Un árbol puede cumplir varias clasificaciones a la vez: todo árbol perfecto es lleno y completo.

## Modelo en memoria

Cada `NodoArbol<String>` es un **objeto** en el *heap*. Las variables guardan **referencias** (direcciones) a esos objetos. El enlace entre nodos se hace guardando en `izquierdo` y `derecho` la referencia al hijo.

```text
 Variable local        HEAP
 ──────────────        ──────────────────────────────────────────────
 frutas ─────────────► [NodoArbol dato="Frutas"
                         izquierdo ───► [NodoArbol dato="Cítricas"
                                          izquierdo ───► [NodoArbol dato="Naranja"
                                                           izquierdo = null
                                                           derecho   = null]
                                          derecho   ───► [NodoArbol dato="Limón" ...]]
                         derecho   ───► [NodoArbol dato="Tropicales" ...]]
```

- `new NodoArbol<>("Frutas")` crea el objeto; la variable `frutas` solo guarda su referencia.
- `frutas.setIzquierdo(citricas)` **no copia** el nodo: guarda la misma referencia, por eso `frutas.getIzquierdo() == citricas` es `true`.
- Los nodos se pueden crear primero y enlazar después; el árbol existe porque las referencias los conectan.
- Un nodo sin hijos tiene `izquierdo = null` y `derecho = null`.

## `null` frente a nodo hoja

- Una **referencia `null`** significa que **no hay ningún nodo** en esa posición. No es un objeto: llamar `.getDato()` sobre ella lanza `NullPointerException`.
- Un **nodo hoja** es un **nodo que sí existe**, con dato, pero cuyas dos referencias hijas son `null`.

Ejemplo: `naranja` es una hoja (existe y `naranja.esHoja()` es `true`), pero `naranja.getIzquierdo()` es `null` (no hay nodo ahí).

### Cómo se evita el `NullPointerException`

- Antes de usar un hijo se valida: `if (nodo.getIzquierdo() != null) { ... }`.
- Los métodos recursivos tienen caso base `if (nodo == null) return ...`.
- `imprimirRaizYHijos()` informa "árbol vacío" si la raíz es `null` y escribe `null` cuando falta un hijo.
- `altura()` devuelve `-1` en un árbol vacío y `contarNodos()` devuelve `0`.

## Árbol de expresiones

```text
        +
      /   \
    *       *
   / \     / \
  3  1500 2  4000
```

Los operadores (`+`, `*`) son nodos internos y los operandos (números) son hojas. Se evalúa en postorden: `(3 * 1500) + (2 * 4000) = 12500`.

| Recorrido | Resultado |
|---|---|
| Preorden (prefija) | `+ * 3 1500 * 2 4000` |
| Inorden (infija) | `3 * 1500 + 2 * 4000` |
| Postorden (postfija) | `3 1500 * 2 4000 * +` |
