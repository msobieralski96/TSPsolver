/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import com.mycompany.tsp_solver.algorithms.Vertice;
import java.io.BufferedReader;
import java.io.IOException;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mic45
 */
public class Reader { 
    //czytaj plik i zwróc w postaci listy linii
    public static List<String> read_lines(String path) throws IOException {
        List<String> list = new ArrayList<String>();

        Path file = Paths.get(path);
        try (BufferedReader br = Files.newBufferedReader(file)) {

            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //zczytaj wierzchołki z listy i utwórz graf
    public static Graph read_vertices(List<String> list) {
        List<Vertice> vertices = new ArrayList<Vertice>();
        List<Integer> ids = new ArrayList<Integer>();
        
        String line;
        Vertice v;
        int counter = 0;
        for(int x = 0; x < list.size(); x++) {
            line = list.get(x);
            String[] data = line.split("\\s+");
            if(data.length >= 3){
                v = new Vertice(counter,
                        Double.parseDouble(data[0]),
                        Double.parseDouble(data[1]),
                        data[2]);
            } else {
                v = new Vertice(counter,
                        Double.parseDouble(data[0]),
                        Double.parseDouble(data[1]),
                        String.valueOf(counter));
            }
            vertices.add(v);
            ids.add(counter);
            counter++;
        }
        
        List<Edge> edges = new ArrayList<Edge>();
        
        for(int i = 0; i < vertices.size(); i++){
            for(int j = i + 1; j < vertices.size(); j++){
                int a_id = vertices.get(i).getId();
                int b_id = vertices.get(j).getId();
                double x1 = vertices.get(i).getX();
                double y1 = vertices.get(i).getY();
                double x2 = vertices.get(j).getX();
                double y2 = vertices.get(j).getY();
                double d = calculate_distance(x1, x2, y1, y2);
                Edge e = new Edge(a_id, b_id, d);
                edges.add(e);
            }
        }
        
        Graph graph = new Graph(ids, edges);
        graph.setVertices_data(vertices);
        return graph;
    }
    
    //zczytaj wierzchołki i krawędzie z listy i utwórz graf
    public static Graph read_edges_n_vertices(List<String> list) {
        String line;
        
        int vertices_amount;
        int edges_amount;
        String[] data = list.get(0).split("\\s+");
        vertices_amount = Integer.parseInt(data[0]);
        edges_amount = Integer.parseInt(data[1]);
        
        List<Vertice> vertices = new ArrayList<Vertice>();
        List<Integer> ids = new ArrayList<Integer>();
        Vertice v;
        
        for(int x = 1; x < (1 + vertices_amount); x++) {
            line = list.get(x);
            data = line.split("\\s+");
            int vid = Integer.parseInt(data[0]);
            if(data.length >= 4){
                v = new Vertice(vid,
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        data[3]);
            } else {
                v = new Vertice(vid,
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        String.valueOf(vid));
            }
            vertices.add(v);
            ids.add(vid);
        }
        
        List<Edge> edges = new ArrayList<Edge>();
        Edge e = null;
        
        for(int x = (vertices_amount + 1);
                x < (1 + vertices_amount + edges_amount);
                x++) {
            line = list.get(x);
            data = line.split("\\s+");
            int beginning = Integer.parseInt(data[0]);
            int end = Integer.parseInt(data[1]);
            if(data.length >= 3) {
                e = new Edge(beginning, end, Double.parseDouble(data[2]));
            } else {
                int point_a = (-1);
                double x_a = (-1);
                double y_a = (-1);
                int point_b = (-1);
                double x_b = (-1);
                double y_b = (-1);
                for(int i = 0; i < vertices.size(); i++) {
                    if(vertices.get(i).getId() == beginning){
                        point_a = i;
                        x_a = vertices.get(i).getX();
                        y_a = vertices.get(i).getY();
                    }
                    if(vertices.get(i).getId() == end){
                        point_b = i;
                        x_b = vertices.get(i).getX();
                        y_b = vertices.get(i).getY();
                    }
                    if((point_a > (-1)) && (point_b > (-1))){
                        double d = calculate_distance(x_a, x_b, y_a, y_b);
                        e = new Edge(beginning, end, d);
                        break;
                    }
                }
            }
            edges.add(e);
        }
        
        Graph graph = new Graph(ids, edges);
        graph.setVertices_data(vertices);
        
        return graph;
    }
    
    //konwertuje graf na macierz sąsiedztwa
    public static Double[][] convert_to_adjacency_matrix(Graph g){
        
        int size = g.getVertices().size();
        int amount_of_edges = g.getEdges().size();
        
        Double[][] graph = new Double[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                graph[i][j] = 0.0;
            }
        }
        
        for (int i = 0; i < amount_of_edges; i++) {
            Edge e = g.getEdges().get(i);
            int beginning = e.getBeginning();
            int end = e.getEnd();
            double weight = e.getWeight();
            int point_a = (-1);
            int point_b = (-1);
            for(int j = 0; j < size; j++){
                if(g.getVertices().get(j) == beginning){
                    point_a = j;
                }
                if(g.getVertices().get(j) == end){
                    point_b = j;
                }
                if((point_a > (-1)) && (point_b > (-1))){
                    graph[point_a][point_b] = weight;
                    graph[point_b][point_a] = weight;
                    break;
                }
            }
        }
        return graph;
    }
    
    public static Double calculate_distance(Double x1, Double x2, Double y1, Double y2){
        /*System.out.println("sqrt((" + x2 + " - " + x1 + ")^2 + (" + y2 + " - " + y1 + ")^2) = "
                + "sqrt((" + (x2 - x1) + "^2 + " + (y2 - y1) + "^2) = "
                + "sqrt((" + pow((x2 - x1), 2) + " + " + pow((y2 - y1), 2) + ") = "
                + sqrt((x2 - x1)^2 + (y2 - y1)^2));*/
        return sqrt(pow((x2 - x1), 2) + pow((y2 - y1), 2));
    }
}
