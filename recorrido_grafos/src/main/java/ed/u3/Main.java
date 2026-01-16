package ed.u3;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    // Rutas de archivos
    private static final String FILE_DIRECTED = "src/main/java/ed/u3/data/g_dirigido_matriz.txt";
    private static final String FILE_UNDIRECTED = "src/main/java/ed/u3/data/g_nodirigido_matriz.txt";
    private static final String CUSTOM_FILE = "src/main/java/ed/u3/data/         .txt";

    private static Graph currentGraph = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printHeader("LABORATORIO 8: ALGORITMOS DE GRAFOS (BFS/DFS)");
            System.out.println("Ruta configurada: src/main/java/ed/u3/data/");
            System.out.println("0. Cargar Grafo desde archivo personalizado");
            System.out.println("1. Cargar Grafo DIRIGIDO");
            System.out.println("2. Cargar Grafo NO DIRIGIDO");
            System.out.println("3. Ejecutar BFS (Anchura)");
            System.out.println("4. Ejecutar DFS (Profundidad)");
            System.out.println("5. Analizar Grafo (Ciclos y Conectividad) [EXTRA]");
            System.out.println("6. Salir");
            System.out.print("\nSeleccione una opción: ");

            try {
                String input = scanner.nextLine();
                int option = Integer.parseInt(input);

                switch (option) {
                    case 0:
                        loadGraph(CUSTOM_FILE);
                        break;
                    case 1:
                        loadGraph(FILE_DIRECTED);
                        break;
                    case 2:
                        loadGraph(FILE_UNDIRECTED);
                        break;
                    case 3:
                        runBFS();
                        break;
                    case 4:
                        runDFS();
                        break;
                    case 5:
                        runAnalysis();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Cerrando aplicación...");
                        break;
                    default:
                        System.out.println("Error: Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }

            if (running) {
                System.out.println("\nPresione ENTER para continuar...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void loadGraph(String filename) {
        System.out.println("Intentando cargar: " + filename);
        try {
            currentGraph = new Graph();
            currentGraph.loadFromMatrixFile(filename);
            System.out.println("Tipo: " + (currentGraph.isDirected() ? "Dirigido" : "No Dirigido"));
            System.out.println("Vértices cargados: 1 al " + currentGraph.getNumVertices());
        } catch (IOException e) {
            System.err.println("Error crítico cargando archivo: " + e.getMessage());
            System.err.println("Verifique que la ruta sea correcta.");
            currentGraph = null;
        }
    }

    private static void runBFS() {
        if (currentGraph == null) {
            System.out.println("Error: Primero cargue un grafo.");
            return;
        }

        int startNodeInternal = askForStartNode();
        if (startNodeInternal == -1) {
            return;
        }

        long startTime = System.nanoTime();
        GraphAlgorithms.BFSResult result = GraphAlgorithms.bfs(currentGraph, startNodeInternal);
        long endTime = System.nanoTime();

        printHeader("RESULTADOS BFS (Inicio: Nodo " + (startNodeInternal + 1) + ")");
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ns");
        System.out.println("Orden de Visita: " + formatListToBase1(result.visitOrder));

        System.out.println("\n" + String.format("%-10s | %-10s | %-10s", "Vértice", "Distancia", "Padre"));
        System.out.println(new String (new char[36]).replace("\0", "-"));

        for (int i = 0; i < currentGraph.getNumVertices(); i++) {
            int vertexDisplay = i + 1;
            String distStr = (result.distances[i] == -1) ? "Inf" : String.valueOf(result.distances[i]);
            String parentStr = (result.parents[i] == -1) ? "-" : String.valueOf(result.parents[i] + 1);

            if (result.distances[i] != -1) {
                System.out.printf("%-10d | %-10s | %-10s%n", vertexDisplay, distStr, parentStr);
            } else {
                System.out.printf("%-10d | %-10s | %-10s (No alcanzable)%n", vertexDisplay, distStr, parentStr);
            }
        }
    }

    private static void runDFS() {
        if (currentGraph == null) {
            System.out.println("Error: Primero cargue un grafo.");
            return;
        }

        int startNodeInternal = askForStartNode();
        if (startNodeInternal == -1) {
            return;
        }

        long startTime = System.nanoTime();
        List<Integer> order = GraphAlgorithms.dfs(currentGraph, startNodeInternal);
        long endTime = System.nanoTime();

        printHeader("RESULTADOS DFS (Inicio: Nodo " + (startNodeInternal + 1) + ")");
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ns");
        System.out.println("Orden de Visita: " + formatListToBase1(order));
    }

    private static void runAnalysis() {
        if (currentGraph == null) {
            System.out.println("Error: Primero cargue un grafo.");
            return;
        }

        printHeader("ANÁLISIS ESTRUCTURAL DEL GRAFO");

        // 1. Análisis de Conectividad
        int components = GraphAlgorithms.countConnectedComponents(currentGraph);
        System.out.println("1. CONECTIVIDAD:");
        System.out.println("   - Número de Componentes Conexas: " + components);
        if (components == 1) {
            System.out.println("   - Conclusión: El grafo es TOTALMENTE CONEXO (se puede llegar a todos lados).");
        } else {
            System.out.println("   - Conclusión: El grafo es DESCONECTADO (tiene islas separadas).");
        }

        // 2. Análisis de Ciclos
        System.out.println("\n2. CICLICIDAD:");
        boolean hasCycle = GraphAlgorithms.hasCycle(currentGraph);
        if (hasCycle) {
            System.out.println("   - Resultado: ¡SÍ tiene ciclos!");
            System.out.println("   - Detalle: Existen caminos cerrados (loops) en la estructura.");
        } else {
            System.out.println("   - Resultado: NO tiene ciclos (Acíclico).");
            System.out.println("   - Detalle: Es una estructura tipo Árbol o Bosque.");
        }
    }

    private static int askForStartNode() {
        int maxNode = currentGraph.getNumVertices();
        System.out.print("Ingrese el nodo de inicio (1 - " + maxNode + "): ");
        try {
            String input = scanner.nextLine();
            int node = Integer.parseInt(input);
            if (node >= 1 && node <= maxNode) {
                return node - 1;
            } else {
                System.out.println("Error: Nodo fuera de rango.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Entrada no válida.");
            return -1;
        }
    }

    private static String formatListToBase1(List<Integer> list) {
        return list.stream()
                .map(n -> String.valueOf(n + 1))
                .collect(Collectors.joining(" -> ", "[", "]"));
    }

    private static void printHeader(String title) {
        String border = new String(new char[title.length() + 8]).replace("\0", "=");
        System.out.println("\n" + border);
        System.out.println("    " + title);
        System.out.println(border);
    }
}
