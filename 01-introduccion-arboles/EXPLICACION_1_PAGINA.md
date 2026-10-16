# De lo lineal a lo jerárquico

**Caso elegido:** menú de una aplicación · **Diagrama:** `menu-aplicacion.drawio` (exportado en `diagrama-menu-aplicacion.pdf`)

## Clasificación

| Situación | Estructura | Justificación |
| --- | --- | --- |
| Sistema de archivos | Jerárquica | Una carpeta raíz contiene subcarpetas y archivos organizados por niveles. |
| Organigrama empresarial | Jerárquica | Los cargos se relacionan por autoridad y dependencia. |
| Menú de aplicación | Jerárquica | Las opciones principales se dividen en submenús y acciones. |
| Árbol genealógico | Jerárquica | Las personas se organizan por generaciones y descendencia. |

## Explicación del diagrama

El diagrama representa el menú de una aplicación con tres niveles. En el **nivel 0** está la raíz, «Menú principal». En el **nivel 1** están sus cuatro hijos: Inicio, Perfil, Configuración y Ayuda. En el **nivel 2** están las nueve hojas, que son las opciones finales (por ejemplo, Cuenta, Seguridad y Notificaciones dentro de Configuración).

Es una estructura jerárquica porque cada nodo, salvo la raíz, tiene exactamente un padre, y un padre puede tener varios hijos. En una lista lineal las opciones se recorrerían una tras otra, sin niveles ni dependencias; si aplanáramos este menú, perderíamos la información de que «Seguridad» pertenece a «Configuración».

La implementación en Java (`MenuAplicacion.java`) usa una clase `NodoMenu` con una lista de hijos. Permite imprimir el árbol, contar nodos y hojas, calcular la altura y buscar la ruta hasta una opción, por ejemplo: *Menú principal > Configuración > Seguridad*.

Comprender esta diferencia ayuda a elegir la estructura correcta: una lista sirve para datos secuenciales; un árbol, cuando existen categorías, subcategorías y niveles.
