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
public class Nearest_neighbour {
    public static WTM_graph_object algorithm(Graph gr, boolean log_enabled, MenuView m,
            boolean first_vertice_mode, int first_vertice_id) {
        
        List<Graph> wtm_graphs = new ArrayList<Graph>();
        List<String> explanations = new ArrayList<String>();
        List<Boolean> is_final_path = new ArrayList<Boolean>();
        List<List<Integer>> final_path_list = new ArrayList<List<Integer>>();
        WTM_graph_object wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Graf wejściowy",
                is_final_path, false,
                final_path_list, new ArrayList<Integer>(),
                gr, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        m.addLog(m.getTekstKonsoli(), "\nRozpoczęto algorytm najbliższego sąsiada\n");
        
        long start, stop;
        start = System.nanoTime();
        
        //budowa grafu pełnego w postaci macierzy sąsiedztwa
        m.addLog(m.getTekstKonsoli(), "\n- Przygotowywanie grafu pełnego -\n");
        int vertices_amount = gr.getVertices().size();
        //informacja w jakiej kolejności są przechowywane wierzchołki w grafie
        int[] labels_for_graph = Brute_force.get_labels_for_graph(vertices_amount, gr);
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = Brute_force.get_a_graph_as_adjacency_matrix(vertices_amount, gr, labels_for_graph);
        //graf w postaci macierzy sąsiedztwa z wyliczonymi dystansamy dla każdych dwóch punktów
        double[][] matrix = Brute_force.get_distances_between_all(vertices_amount,
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
        
        m.addLog(m.getTekstKonsoli(), "\n- Algorytm najbliższego sąsiada -\n");
        Another_helper_two result = nearest_neighbour_algorithm(first_point, matrix, labels_for_graph,
                log_enabled, m, gr, wtm_graph_object, start, stop);
        stop = result.getStop();
        start = result.getStart();
        List<Integer> path = result.getPath();
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        start = System.nanoTime();
        
        m.addLog(m.getTekstKonsoli(), "\n- Obliczanie kosztu rozwiązania -\n");
        double distance = Brute_force.calculate_distance_for_path(path, matrix);
        List<Integer> vertices_list = Brute_force.get_list_of_vertices_id(path, labels_for_graph);
        if (log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Znaleziono rozwiązanie.\nKoszt = ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(distance));
            m.addLog(m.getTekstKonsoli(), "\ntrasa:");
            Brute_force.print_lista_in_log(vertices_list, m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time4_label(), start, stop, 0);
        m.set_full_time(m.getWtm_panel());
        
        m.addLog(m.getTekstKonsoli(), "Zakończono algorytm najbliższego sąsiada\n");
        Two_approximation.print_int_list(vertices_list, "Rozwiązanie", m);
        m.addLog(m.getTekstKonsoli(), "\n--------------------\n");
        
        m.setPath1(vertices_list);
        m.setIs_path2(false);
        
        wtm_graph_object = result.getWtm_graph_object();
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozwiązanie problemu komiwojażera",
                is_final_path, true, 
                final_path_list, m.compare_two_path(gr, false),
                gr, new ArrayList<Vector<Double>>());
        
        return wtm_graph_object;
    }
    
    static Another_helper_two nearest_neighbour_algorithm(int start, double[][] matrix, int[] labels_for_graph,
            boolean log_enabled, MenuView m, Graph gr, WTM_graph_object wtm_graph_object,
            long start_time, long stop_time){
        List<Integer> path = new ArrayList<Integer>();
        List<Integer> visualization_path = new ArrayList<Integer>();
        
        List<Boolean> visited = new ArrayList<Boolean>();
        for(int i = 0; i < matrix.length; i++) {
            visited.add(false);
        }
        
        stop_time = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start_time, stop_time, 0);
        
        String vertice_name = "";
        for (int i = 0; i < gr.getVertices_data().size(); i++) {
            if (gr.getVertices_data().get(i).getId() == labels_for_graph[start]) {
                vertice_name = gr.getVertices_data().get(i).getName();
                visualization_path.add(gr.getVertices_data().get(i).getId());
                break;
            }
        }
        wtm_graph_object = add_new_wtm_graph(wtm_graph_object.getWtm_graphs(),
                wtm_graph_object.getExplanations(),
                "Wierzchołek startowy: " + vertice_name,
                wtm_graph_object.getIs_final_path(), false,
                wtm_graph_object.getFinal_path_list(), new ArrayList<Integer>(),
                gr, new ArrayList<Vector<Double>>());
        
        start_time = System.nanoTime();
        
        path.add(start);
        visited.set(start, true);
        int current = start;
        if (log_enabled) {
            for(int i = 0; i < gr.getVertices_data().size(); i++) {
                if(gr.getVertices_data().get(i).getId() == labels_for_graph[current]) {
                    m.addLog(m.getTekstKonsoli(), "\nWierzchołek początkowy: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(current));
                    m.addLog(m.getTekstKonsoli(), " (id: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(i).getId()));
                    m.addLog(m.getTekstKonsoli(), ", name: ");
                    m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(i).getName());
                    m.addLog(m.getTekstKonsoli(), ")\n");
                    break;
                }
            }
        }
        
        while(not_all_visited(visited)) {
            double min_value = 1000000;
            int closest_neighbour = -1;
            if (log_enabled) {
                m.addLog(m.getTekstKonsoli(), "\nNieodwiedzeni sąsiedzi: \n");
            }
            for(int i = 0; i < matrix.length; i++) {
                if (visited.get(i) == false) {
                    if (log_enabled) {
                        for(int j = 0; j < gr.getVertices_data().size(); j++) {
                            if (gr.getVertices_data().get(j).getId() == labels_for_graph[i]) {
                                m.addLog(m.getTekstKonsoli(), "Wierzchołek ");
                                m.addLog(m.getTekstKonsoli(), String.valueOf(i));
                                m.addLog(m.getTekstKonsoli(), " (id: ");
                                m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(j).getId()));
                                m.addLog(m.getTekstKonsoli(), ", name: ");
                                m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(j).getName());
                                m.addLog(m.getTekstKonsoli(), "); dystans: ");
                                m.addLog(m.getTekstKonsoli(), String.valueOf(matrix[current][i]));
                                m.addLog(m.getTekstKonsoli(), "\n");
                            }
                        }
                    }
                    if (matrix[current][i] < min_value) {
                        min_value = matrix[current][i];
                        closest_neighbour = i;
                    }
                }
            }
            if (log_enabled) {
                for (int i = 0; i < gr.getVertices_data().size(); i++) {
                    if (gr.getVertices_data().get(i).getId() == labels_for_graph[closest_neighbour]) {
                        m.addLog(m.getTekstKonsoli(), "\nNajbliższy sąsiad (nowy wierzchołek aktualny): ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(closest_neighbour));
                        m.addLog(m.getTekstKonsoli(), " (id: ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(i).getId()));
                        m.addLog(m.getTekstKonsoli(), ", name: ");
                        m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(i).getName());
                        m.addLog(m.getTekstKonsoli(), ")\n");
                    }
                }
            }
            path.add(closest_neighbour);
            visited.set(closest_neighbour, true);
            current = closest_neighbour;
            
            stop_time = System.nanoTime();
            m.set_time(m.getWtm_panel().get_time3_label(), start_time, stop_time,
                    Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
            
            m.setPath1(visualization_path);
            m.setIs_path2(false);
            for (int i = 0; i < gr.getVertices_data().size(); i++) {
                if (gr.getVertices_data().get(i).getId() == labels_for_graph[closest_neighbour]) {
                    vertice_name = gr.getVertices_data().get(i).getName();
                    visualization_path.add(gr.getVertices_data().get(i).getId());
                    break;
                }
            }
            wtm_graph_object = add_new_wtm_graph(wtm_graph_object.getWtm_graphs(),
                    wtm_graph_object.getExplanations(),
                    "Połączono z najbliższym sąsiadem: " + vertice_name,
                    wtm_graph_object.getIs_final_path(), true,
                    wtm_graph_object.getFinal_path_list(), m.compare_two_path(gr, false),
                    gr, new ArrayList<Vector<Double>>());
            
            start_time = System.nanoTime();
        }
        
        return new Another_helper_two(path, wtm_graph_object, start_time, stop_time);
    }
    
    static boolean not_all_visited(List<Boolean> visited) {
        for(int i = 0; i < visited.size(); i++) {
            if(visited.get(i) == false) {
                return true;
            }
        }
        return false;
    }
}
