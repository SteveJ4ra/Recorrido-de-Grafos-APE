# Implementación de Algoritmos de Recorrido de Grafos (BFS/DFS)

## Descripción del Proyecto
Este proyecto implementa una estructura de datos de **Grafo** en Java, utilizando **Listas de Adyacencia** para la gestión eficiente de la memoria. La aplicación permite cargar grafos desde archivos de texto (formato Matriz de Adyacencia), detectar automáticamente si son dirigidos o no dirigidos, y ejecutar algoritmos fundamentales de recorrido y análisis.

## Funcionalidades
1.  **Carga de Grafos desde Archivo:**
    * Lectura de matrices de adyacencia desde archivos `.txt`.
    * **Auto-detección:** El sistema analiza la simetría de la matriz para determinar automáticamente si el grafo es *Dirigido* o *No Dirigido*.
    * Validación de formato (matriz cuadrada).

2.  **Algoritmos de Recorrido:**
    * **BFS (Breadth-First Search):** Implementado con `Queue`. Calcula el orden de visita, las distancias mínimas (niveles) desde el nodo origen y el padre de cada nodo.
    * **DFS (Depth-First Search):** Implementación recursiva para explorar la profundidad del grafo.

3.  **Análisis Estructural (Extra):**
    * **Detección de Ciclos:** Algoritmos diferenciados para grafos dirigidos (pila de recursión) y no dirigidos (verificación de padre).
    * **Conectividad:** Conteo de componentes conexas (islas) para determinar si el grafo está totalmente conectado.

## Estructura del Proyecto

El código fuente se encuentra bajo el paquete `ed.u3`:

* `Graph.java`: Definición de la estructura del grafo, validaciones de simetría y conversión de Matriz a Lista de Adyacencia.
* `GraphAlgorithms.java`: Lógica pura de los algoritmos (BFS, DFS, Ciclos, Conectividad).
* `Main.java`: Interfaz de línea de comandos (CLI) para interactuar con el usuario.
* `data/`: Carpeta contenedora de los archivos `.txt` de prueba.

## Requisitos y Ejecución

### Requisitos Previos
* Java Development Kit (JDK) 17 o superior.
* IDE recomendado: IntelliJ IDEA o Visual Studio Code.

### Compilación y Ejecución
Desde la raíz del proyecto:

```bash
# Compilar
javac -d bin src/main/java/ed/u3/*.java

# Ejecutar
java -cp bin ed.u3.Main
