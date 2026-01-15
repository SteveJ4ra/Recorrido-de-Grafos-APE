package ed.u3;

import java.io.*;
import java.util.*;

public class Graph {
    private int numVertices;
    private boolean isDirected;
    private List<List<Integer>> adjList; // Lista de adyacencia

    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
        this.adjList = new ArrayList<>();
        this.numVertices = 0;
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
        // Evita duplicados y verifica límites
        if (source >= 0 && source < numVertices && destination >= 0 && destination < numVertices) {
            this.adjList.get(source).add(destination);
            // Nota: Como leemos una matriz completa, si es no dirigido la matriz ya trae la simetría,
            // por lo que no es estrictamente necesario duplicar la arista manualmente aquí si leemos el archivo.
        }
    }

    // Carga el grafo desde un archivo de Matriz de Adyacencia
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
        initGraph(rows);

        for (int i = 0; i < rows; i++) {
            String[] tokens = lines.get(i);
            for (int j = 0; j < tokens.length; j++) {
                try {
                    int val = Integer.parseInt(tokens[j]);
                    if (val == 1) {
                        addEdge(i, j);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Valor no numérico en fila " + i + ", col " + j);
                }
            }
        }
        System.out.println(">> Grafo cargado exitosamente: " + numVertices + " vértices.");
    }

    // Getters
    public int getNumVertices() { return numVertices; }
    public List<Integer> getNeighbors(int v) { return adjList.get(v); }
    public boolean isDirected() { return isDirected; }
}
