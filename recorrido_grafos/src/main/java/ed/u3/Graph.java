package ed.u3;

import java.io.*;
import java.util.*;

public class Graph {

    private int numVertices;
    private boolean isDirected;
    private List<List<Integer>> adjList; // Lista de adyacencia

    // Constructor vacío (la dirección se definirá al cargar el archivo)
    public Graph() {
        this.adjList = new ArrayList<>();
        this.numVertices = 0;
        this.isDirected = false; // Valor por defecto
    }

    // Inicializa listas vacías
    private void initGraph(int n) {
        this.numVertices = n;
        this.adjList.clear();
        for (int i = 0; i < n; i++) {
            this.adjList.add(new ArrayList<>());
        }
    }

    // Agrega una arista (Edge)
    public void addEdge(int source, int destination) {
        // FALTA VALIDAR RANGOS - CRÍTICO
        if (source < 0 || source >= numVertices
                || destination < 0 || destination >= numVertices) {
            throw new IndexOutOfBoundsException(
                    "Vértice fuera de rango. Máximo: " + (numVertices - 1)
            );
        }

        List<Integer> neighbors = adjList.get(source);
        if (!neighbors.contains(destination)) {
            neighbors.add(destination);
        }
        // Para grafos NO dirigidos
        if (!isDirected && source != destination) {
            List<Integer> inverseNeighbors = adjList.get(destination);
            if (!inverseNeighbors.contains(source)) {
                inverseNeighbors.add(source);
            }
        }
    }

    /**
     * Valida si una matriz es simétrica.
     */
    private static boolean isSymmetric(int matriz[][]) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = i; j < matriz.length; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Carga el grafo y define si es Dirigido o No Dirigido automáticamente
    public void loadFromMatrixFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("El archivo " + fileName + " no fue encontrado.");
        }

        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim().split("\\s+"));
                }
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("El archivo está vacío.");
        }

        int rows = lines.size();
        int[][] tempMatrix = new int[rows][rows]; // Matriz temporal

        // 1. Cargar datos y VALIDAR QUE SEA CUADRADA
        for (int i = 0; i < rows; i++) {
            String[] tokens = lines.get(i);

            if (tokens.length != rows) {
                throw new IOException("Error de formato: La matriz no es cuadrada. Fila " + (i + 1) + " tiene " + tokens.length + " columnas, se esperaban " + rows + ".");
            }

            for (int j = 0; j < rows; j++) {
                try {
                    tempMatrix[i][j] = Integer.parseInt(tokens[j]);
                } catch (NumberFormatException e) {
                    throw new IOException("Error: Valor no numérico en fila " + i + ", col " + j);
                }
            }
        }

        // 2. DETERMINAR SI ES DIRIGIDO O NO
        if (isSymmetric(tempMatrix)) {
            this.isDirected = false;
            System.out.println(">> Análisis: Matriz Simétrica. Configurando grafo como NO DIRIGIDO.");
        } else {
            this.isDirected = true;
            System.out.println(">> Análisis: Matriz No Simétrica. Configurando grafo como DIRIGIDO.");
        }

        // 3. Construir el Grafo
        initGraph(rows);
        if (isDirected) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    if (tempMatrix[i][j] != 0) {
                        addEdge(i, j);
                    }
                }
            }
        } else {
            // Solo triangular superior para evitar duplicados
            for (int i = 0; i < rows; i++) {
                for (int j = i; j < rows; j++) {
                    if (tempMatrix[i][j] != 0) {
                        addEdge(i, j);
                        // addEdge ya agregará la inversa si es no dirigido
                    }
                }
            }
        }

        System.out.println(">> Grafo cargado exitosamente: " + numVertices + " vértices.");
    }

    // Getters
    public int getNumVertices() {
        return numVertices;
    }

    public List<Integer> getNeighbors(int v) {
        return adjList.get(v);
    }

    public boolean isDirected() {
        return isDirected;
    }
}
