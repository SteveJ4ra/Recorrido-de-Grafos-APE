package ed.u3;

import java.util.*;

public class GraphAlgorithms {

    // --- BFS 
    public static BFSResult bfs(Graph graph, int startNode) {
        int n = graph.getNumVertices();
        BFSResult result = new BFSResult(n);

        if (startNode < 0 || startNode >= n) return result;

        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[n];

        visited[startNode] = true;
        result.distances[startNode] = 0;
        queue.add(startNode);
        result.visitOrder.add(startNode);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : graph.getNeighbors(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    result.distances[v] = result.distances[u] + 1;
                    result.parents[v] = u;
                    result.visitOrder.add(v);
                    queue.add(v);
                }
            }
        }
        return result;
    }

    // --- DFS ---
    public static List<Integer> dfs(Graph graph, int startNode) {
        List<Integer> visitOrder = new ArrayList<>();
        int n = graph.getNumVertices();
        if (startNode < 0 || startNode >= n) return visitOrder;

        boolean[] visited = new boolean[n];
        dfsRecursive(graph, startNode, visited, visitOrder);
        return visitOrder;
    }

    private static void dfsRecursive(Graph graph, int u, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                dfsRecursive(graph, v, visited, order);
            }
        }
    }
    
    /**
     * Cuenta el número de componentes conexas (islas) en el grafo.
     * Si retorna 1, el grafo está totalmente conectado.
     */
    public static int countConnectedComponents(Graph graph) {
        int n = graph.getNumVertices();
        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // Iniciamos un DFS desde aquí para marcar toda la isla
                count++;
                countComponentsDFS(graph, i, visited);
            }
        }
        return count;
    }

    private static void countComponentsDFS(Graph graph, int u, boolean[] visited) {
        visited[u] = true;
        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                countComponentsDFS(graph, v, visited);
            }
        }
    }

    /**
     * Detecta si existe al menos un ciclo en el grafo.
     * Maneja lógica diferente para Dirigidos vs No Dirigidos.
     */
    public static boolean hasCycle(Graph graph) {
        int n = graph.getNumVertices();

        if (graph.isDirected()) {
            // Lógica para GRAFO DIRIGIDO (Usa pila de recursión)
            boolean[] visited = new boolean[n];
            boolean[] recStack = new boolean[n]; // Nodos actualmente en la recursión

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    if (hasCycleDirected(graph, i, visited, recStack)) {
                        return true;
                    }
                }
            }
            return false;

        } else {
            // Lógica para GRAFO NO DIRIGIDO (Usa padre para no regresar inmeditamente)
            boolean[] visited = new boolean[n];
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    if (hasCycleUndirected(graph, i, visited, -1)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // Auxiliar para ciclos DIRIGIDOS
    private static boolean hasCycleDirected(Graph graph, int u, boolean[] visited, boolean[] recStack) {
        visited[u] = true;
        recStack[u] = true; // Agregamos al stack actual

        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                if (hasCycleDirected(graph, v, visited, recStack)) {
                    return true;
                }
            } else if (recStack[v]) {
                // Si el vecino YA está en el stack actual, es un ciclo 
                return true;
            }
        }
        recStack[u] = false; // Sacamos del stack al terminar
        return false;
    }

    // Auxiliar para ciclos NO DIRIGIDOS
    private static boolean hasCycleUndirected(Graph graph, int u, boolean[] visited, int parent) {
        visited[u] = true;

        for (int v : graph.getNeighbors(u)) {
            if (!visited[v]) {
                if (hasCycleUndirected(graph, v, visited, u)) {
                    return true;
                }
            } else if (v != parent) {
                // Si el vecino ya fue visitado Y NO es mi padre directo, es un ciclo
                return true;
            }
        }
        return false;
    }
    
     //  Estructuras para BFS 
    public static class BFSResult {
        public List<Integer> visitOrder = new ArrayList<>();
        public int[] distances;
        public int[] parents;

        public BFSResult(int n) {
            distances = new int[n];
            parents = new int[n];
            Arrays.fill(distances, -1);
            Arrays.fill(parents, -1);
        }
    }
}