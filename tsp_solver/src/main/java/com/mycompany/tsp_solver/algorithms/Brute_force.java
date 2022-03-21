/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import com.mycompany.tsp_solver.MenuView;
import static com.mycompany.tsp_solver.algorithms.WTM.add_new_wtm_graph;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author mic45
 */
public class Brute_force {
    
    public static WTM_graph_object algorithm(Graph g, boolean log_enabled, MenuView m,
            boolean first_vertice_mode, int first_vertice_id) {
        
        List<Graph> wtm_graphs = new ArrayList<Graph>();
        List<String> explanations = new ArrayList<String>();
        List<Boolean> is_final_path = new ArrayList<Boolean>();
        List<List<Integer>> final_path_list = new ArrayList<List<Integer>>();
        WTM_graph_object wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Graf wejściowy",
                is_final_path, false, 
                final_path_list, new ArrayList<Integer>(),
                g, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        m.addLog(m.getTekstKonsoli(), "\nRozpoczęto algorytm Brute force\n");
        
        long start, stop;
        start = System.nanoTime();
        
        //budowa macierzy sąsiedztwa
        m.addLog(m.getTekstKonsoli(), "\n- Przygotowywanie macierzy sąsiedztwa -\n");
        int vertices_amount = g.getVertices().size();
        //informacja w jakiej kolejności są przechowywane wierzchołki w grafie
        int[] labels_for_graph = Brute_force.get_labels_for_graph(vertices_amount, g);
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = Brute_force.get_a_graph_as_adjacency_matrix(vertices_amount, g, labels_for_graph);
        //graf w postaci macierzy sąsiedztwa z wyliczonymi dystansamy dla każdych dwóch punktów
        double[][] matrix = get_distances_between_all(vertices_amount,
                labels_for_graph, graph);
        
        if(log_enabled) {
            Brute_force.print_some_brute_data(vertices_amount, labels_for_graph,
                    graph, matrix, m);
        }
        
        int first_point = 0;
        if(first_vertice_mode) {
            for(int i = 0; i < labels_for_graph.length; i++) {
                if(labels_for_graph[i] == first_vertice_id) {
                    first_point = i;
                    break;
                }
            }
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time2_label(), start, stop, 0);
        start = System.nanoTime();
        
        //przeszukujemy wszystkie permutacje (bez powtórzeń)
        m.addLog(m.getTekstKonsoli(), "\n- Sprawdzanie wszystkich premutacji -\n");
        
        //w permutacji przechowujemy kolejnosc wierzcholkow w grafie; NIE ich id
        List<Integer> first_permutation = new ArrayList<Integer>();
        if (first_vertice_mode) {
            first_permutation.add(first_point);
            for (int i = 0; i < vertices_amount; i++) {
                if(i != first_point) {
                    first_permutation.add(i);
                }
            }
        } else {
            for (int i = 0; i < vertices_amount; i++) {
                first_permutation.add(i);
            }
        }
        
        List<Integer> permutation = new ArrayList<Integer>();
        
        Shortest_path shortest_path = new Shortest_path(999999999);
        shortest_path = check_permutation(first_permutation, permutation, matrix,
                1, vertices_amount, shortest_path, labels_for_graph,
                log_enabled, m, wtm_graph_object, g, start, stop);
        
        stop = shortest_path.getStop();
        start = shortest_path.getStart();

        List<Integer> path = shortest_path.getVertices();
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        m.set_time(m.getWtm_panel().get_time4_label(), 0, 0, 0);
        m.set_full_time(m.getWtm_panel());
        
        m.addLog(m.getTekstKonsoli(), "Zakończono algorytm Brute force\n");
        Two_approximation.print_int_list(path, "Rozwiązanie", m);
        m.addLog(m.getTekstKonsoli(), "\n--------------------\n");
        
        m.setPath1(path);
        m.setIs_path2(false);
        
        wtm_graph_object = shortest_path.getWtm_graph_object();
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozwiązanie problemu komiwojażera",
                is_final_path, true, 
                final_path_list, m.compare_two_path(g, false),
                g, new ArrayList<Vector<Double>>());
        
        return wtm_graph_object;
    }
    
    public static Shortest_path check_permutation(List<Integer> list_of_indexes,
            List<Integer> permutation,
            double[][] matrix,
            int depth,
            int max_depth,
            Shortest_path shortest_path,
            int[] labels_for_graph,
            boolean log_enabled,
            MenuView m,
            WTM_graph_object wtm_graph_object,
            Graph g,
            long start,
            long stop) {
        for(int i = 0; i < list_of_indexes.size(); i++) {
            if(depth == 1 && i > 0) {
                break;
            }
            permutation.add(list_of_indexes.get(i));
            if(depth < max_depth) {
                List<Integer> reduced_list = new ArrayList<Integer>();
                for(int j = 0; j < list_of_indexes.size(); j++) {
                    if(j != i){
                        reduced_list.add(list_of_indexes.get(j));
                    }
                }
                shortest_path = check_permutation(reduced_list, permutation, matrix,
                        depth+1, max_depth, shortest_path, labels_for_graph,
                        log_enabled, m, wtm_graph_object, g, start, stop);
                stop = shortest_path.getStop();
                start = shortest_path.getStart();
            } else {
                double distance = calculate_distance_for_path(permutation, matrix);
                if(distance < shortest_path.getCost()){
                    List<Integer> vertices_list = get_list_of_vertices_id(permutation, labels_for_graph);
                    
                    stop = System.nanoTime();
                    m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                            Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
                    
                    m.setPath1(vertices_list);
                    m.setIs_path2(false);
                    wtm_graph_object = add_new_wtm_graph(wtm_graph_object.getWtm_graphs(),
                            wtm_graph_object.getExplanations(),
                            "<html><p>Znaleziono najkrótszą permutację o koszcie =</p><p>" +
                                    String.valueOf(distance) + "</p></html>",
                            wtm_graph_object.getIs_final_path(), true,
                            wtm_graph_object.getFinal_path_list(), m.compare_two_path(g, false),
                            g, new ArrayList<Vector<Double>>());
                    
                    start = System.nanoTime();
                    
                    shortest_path = new Shortest_path(vertices_list, distance, wtm_graph_object,
                            start, stop);
                    if (log_enabled) {
                        m.addLog(m.getTekstKonsoli(), "Znaleziono najkrótszą permutację.\nKoszt = ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(distance));
                        m.addLog(m.getTekstKonsoli(), "\ntrasa:");
                        print_lista_in_log(vertices_list, m);
                    }
                }
            }
            permutation.remove(permutation.size() - 1);
        }
        return shortest_path;
    }
    
    public static void print_lista_in_log(List<Integer> permutation, MenuView m){
        for (int i = 0; i < permutation.size(); i++) {
            m.addLog(m.getTekstKonsoli(), " ");
            m.addLog(m.getTekstKonsoli(), permutation.get(i).toString());
        }
        m.addLog(m.getTekstKonsoli(), "\n");
    }
    
    public static List<Integer> get_list_of_vertices_id(List<Integer> permutation,
            int[] labels_for_graph) {
        List<Integer> vertices_list = new ArrayList<Integer>();
        for(int i = 0; i < permutation.size(); i++) {
            vertices_list.add(labels_for_graph[permutation.get(i)]);
        }
        vertices_list.add(labels_for_graph[permutation.get(0)]);
        return vertices_list;
    }
    
    public static double calculate_distance_for_path(List<Integer> permutation, double[][] matrix) {
        double distance = 0;
        for(int i = 0; i < permutation.size() - 1; i++){
            distance += matrix[permutation.get(i)][permutation.get(i+1)];
        }
        distance += matrix[permutation.get(permutation.size() - 1)][permutation.get(0)];
        return distance;
    }
    
    public static int[] get_labels_for_graph(int vertices_amount, Graph g) {
        int[] labels_for_graph = new int[vertices_amount]; //informacja w jakiej kolejności
        //są przechowywane wierzchołki w grafie
        int counter = 0;

        for (int i = 0; i < g.getEdges().size(); i++) {
            Edge edge = g.getEdges().get(i);
            int beginning = edge.getBeginning();
            int end = edge.getEnd();

            boolean add_b_label = true;
            boolean add_e_label = true;
            for (int j = 0; j < counter; j++) {
                if (labels_for_graph[j] == beginning) {
                    add_b_label = false;
                }
                if (labels_for_graph[j] == end) {
                    add_e_label = false;
                }
            }
            if (add_b_label) {
                labels_for_graph[counter] = beginning;
                counter++;
            }
            if (add_e_label) {
                labels_for_graph[counter] = end;
                counter++;
            }
            if (counter >= vertices_amount) {
                break;
            }
        }
        return labels_for_graph;
    }
    
    public static double[][] get_a_graph_as_adjacency_matrix(int vertices_amount,
            Graph g, int[] labels_for_graph) {
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = new double[vertices_amount][vertices_amount];
        for (int i = 0; i < g.getEdges().size(); i++) {
            Edge edge = g.getEdges().get(i);
            int beginning = edge.getBeginning();
            int end = edge.getEnd();
            int beginning_label = -1;
            int end_label = -1;
            for (int j = 0; j < vertices_amount; j++) {
                if (labels_for_graph[j] == beginning) {
                    beginning_label = j;
                }
                if (labels_for_graph[j] == end) {
                    end_label = j;
                }
                if (beginning_label != (-1) && end_label != (-1)) {
                    break;
                }
            }
            double weight = edge.getWeight();
            graph[beginning_label][end_label] = weight;
            graph[end_label][beginning_label] = weight;
        }
        return graph;
    }
    
    public static int[] get_labels_for_path(List<Integer> path,
            int vertices_amount, int[] labels_for_graph) {
        //informacja w jakiej kolejności są przechowywane wierzchołki w odd_vertices
        int[] labels_for_path = new int[path.size()];

        for (int i = 0; i < path.size(); i++) {
            for (int j = 0; j < vertices_amount; j++) {
                if (path.get(i) == labels_for_graph[j]) {
                    labels_for_path[i] = j;
                }
            }
        }
        return labels_for_path;
    }
    
    public static void print_some_data(List<Integer> path, int vertices_amount,
            int[] labels_for_graph, int[] labels_for_path, double[][] graph, MenuView m) {
        m.addLog(m.getTekstKonsoli(), "Labels for graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "vertice: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(pr));
            m.addLog(m.getTekstKonsoli(), " -> label: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "Labels for odd vertices:\n");
        for (int pr = 0; pr < path.size(); pr++) {
            m.addLog(m.getTekstKonsoli(), "vertice: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(path.get(pr)));
            m.addLog(m.getTekstKonsoli(), " -> label: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_path[pr]));
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "Graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "\t");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
        }
        m.addLog(m.getTekstKonsoli(), "\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\t");
            for (int pr2 = 0; pr2 < vertices_amount; pr2++) {
                m.addLog(m.getTekstKonsoli(), String.valueOf(graph[pr][pr2]));
                m.addLog(m.getTekstKonsoli(), "\t");
            }
            m.addLog(m.getTekstKonsoli(), "\n");
        }
    }
    
    public static void print_some_brute_data(int vertices_amount,
            int[] labels_for_graph, double[][] graph, double[][] matrix, MenuView m) {
        m.addLog(m.getTekstKonsoli(), "Labels for graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "vertice: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(pr));
            m.addLog(m.getTekstKonsoli(), " -> label: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "Graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "\t");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
        }
        m.addLog(m.getTekstKonsoli(), "\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\t");
            for (int pr2 = 0; pr2 < vertices_amount; pr2++) {
                m.addLog(m.getTekstKonsoli(), String.valueOf(graph[pr][pr2]));
                m.addLog(m.getTekstKonsoli(), "\t");
            }
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "Calculated graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "\t");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
        }
        m.addLog(m.getTekstKonsoli(), "\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\t");
            for (int pr2 = 0; pr2 < vertices_amount; pr2++) {
                m.addLog(m.getTekstKonsoli(), String.valueOf(matrix[pr][pr2]));
                m.addLog(m.getTekstKonsoli(), "\t");
            }
            m.addLog(m.getTekstKonsoli(), "\n");
        }
    }
    
    public static Distance_data calculate_distance_n_full_path(List<Integer> path, int[] labels_for_graph,
            int[] labels_for_path, double[][] graph, boolean log_enabled, MenuView m) {
        double distance = 0;
        List<Integer> full_path = new ArrayList<Integer>();
        
        full_path.add(labels_for_path[0]);
        for (int i = 0; i < (path.size() - 1); i++) {
            int start = labels_for_path[i];
            int end = labels_for_path[i+1];
            Dijkstra_object dijkstra_object = Dijkstra(start, graph);
            double[] distance_data = dijkstra_object.getD();
            full_path = update_full_path(full_path, start, end, dijkstra_object.getP());
            
            if (log_enabled) {
                m.addLog(m.getTekstKonsoli(), path.get(i).toString());
                m.addLog(m.getTekstKonsoli(), "-");
                m.addLog(m.getTekstKonsoli(), path.get(i+1).toString());
                m.addLog(m.getTekstKonsoli(), " distance: ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(distance_data[end]));
                m.addLog(m.getTekstKonsoli(), "\n");
            }
            distance += distance_data[end];
        }
        full_path = convert_from_labels(full_path, labels_for_graph);
        return new Distance_data(distance, full_path);
    }
    
    public static List<Integer> update_full_path(List<Integer> full_path, int start, int end, int[] p) {
        List<Integer> from_end_to_start = new ArrayList<Integer>();
        int current = end;
        while(current != start) {
            from_end_to_start.add(current);
            current = p[current];
        }
        for(int i = from_end_to_start.size() - 1; i >= 0; i--){
            full_path.add(from_end_to_start.get(i));
        }
        return full_path;
    }
    
    public static List<Integer> convert_from_labels(List<Integer> full_path, int[] labels_for_graph) {
        List<Integer> converted_list = new ArrayList<Integer>();
        for(int i = 0; i < full_path.size(); i++) {
            converted_list.add(labels_for_graph[full_path.get(i)]);
        }
        return converted_list;
    }
    
    public static double[][] get_distances_between_all(int vertices_amount,
            int[] labels_for_graph, double[][] graph){
        double[][] matrix = new double[vertices_amount][vertices_amount];
        for(int i = 0; i < vertices_amount; i++){
            double[] distance_data = Dijkstra(i, graph).getD();
            matrix[i] = distance_data;
        }
        return matrix;
    }
    
    public static Dijkstra_object Dijkstra(int start, double[][] graph) {
        int vertices_amount = graph[0].length;
        double[] d = new double[vertices_amount]; //tablica odległości
        boolean[] f = new boolean[vertices_amount]; //tablica pomocnicza;
        //informuje czy do wierzchołka może istnieć jeszcze krótsza droga
        int[] p = new int[vertices_amount]; //tablica poprzedników
        int r; //badany wierzchołek

        for (int v = 0; v < vertices_amount; v++) {
            d[v] = 1000000;
            f[v] = false;
            p[v] = -1;
        }
        d[start] = 0;
        f[start] = true;
        r = start;

        while (if_any_vertice_not_checked(f)) {
            for (int v = 0; v < vertices_amount; v++) {
                if (graph[r][v] > 0.0) {
                    if (!f[v]) {
                        double value = d[r] + graph[r][v];
                        if (value < d[v]) {
                            d[v] = value;
                            p[v] = r;
                        }
                    }
                }
            }
            double y_value = 999999;
            int next = -1;
            for (int y = 0; y < vertices_amount; y++) {
                if (!f[y] && d[y] < y_value) {
                    y_value = d[y];
                    next = y;
                }
            }
            if(next == (-1)) {
                break;
            }
            f[next] = true;
            r = next;
        }
        Dijkstra_object result = new Dijkstra_object(d, p);
        return result;
    }
    
    public static boolean if_any_vertice_not_checked(boolean[] f) {
        for (int i = 0; i < f.length; i++) {
            if (!f[i]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean is_graph_connected(Double[][] graph) {
        int size = graph.length;
        double[][] g = new double[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                g[i][j] = graph[i][j];
            }
        }
        
        double[] d = Dijkstra(0, g).getD();
        
        for(int i = 0; i < size; i++) {
            if(d[i] == 1000000) {
                return false;
            }
        }
        return true;
    }
}
