/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import com.mycompany.tsp_solver.MenuView;
import static com.mycompany.tsp_solver.algorithms.WTM.add_new_wtm_graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author mic45
 */
public class Christofides {

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
        
        m.addLog(m.getTekstKonsoli(), "\nRozpoczęto algorytm Christofidesa\n");
        
        long start, stop;
        start = System.nanoTime();
        
        //budowa kopca
        m.addLog(m.getTekstKonsoli(), "\n- Tworzenie kopca -\n");
        int vertices_amount = gr.getVertices().size();
        Heap heap = new Heap();
        Heap back_up_heap = new Heap();
        for(int i = 0; i < gr.getEdges().size(); i++){
            Edge e = gr.getEdges().get(i);
            heap.add_to_heap(e);
            back_up_heap.add_to_heap(e);
        }
        if(log_enabled) {
            heap.print_heap_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time2_label(), start, stop, 0);
        start = System.nanoTime();
        
        //minimalne drzewo rozpinajace
        m.addLog(m.getTekstKonsoli(), "\n- Szukanie MST -\n");
        Graph MST = Two_approximation.Kruskal(heap, vertices_amount, log_enabled, m);
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop, 0);
        
        List<Integer> temp_path = new ArrayList<Integer>();
        for(int i = 0; i < MST.getEdges().size(); i++) {
            int beginning = MST.getEdges().get(i).getBeginning();
            int end = MST.getEdges().get(i).getEnd();
            temp_path.add(beginning);
            temp_path.add(end);
            temp_path.add(-2);
        }
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Minimalne drzewo rozpinające grafu",
                is_final_path, true, 
                final_path_list, temp_path,
                gr, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        //szukamy wierzchołków nieparzystego stopnia
        m.addLog(m.getTekstKonsoli(), "\n- Poszukiwanie wierzchołków nieparzystego stopnia w MST -\n");
        List<Integer> odd_vertices = get_odd_vertices_list(MST, log_enabled, m);
        //znajdujemy najtańsze połączenia między tymi wierzchołkami w grafie ogólnym i tworzymy nowy podgraf
        m.addLog(m.getTekstKonsoli(), "\n- Tworzenie podgrafu z najtańszymi połączeniami dla wierzchołków nieparzystego stopnia W MST -\n");
        Graph odd_graph = get_a_graph_for_odd_vertices(odd_vertices, vertices_amount,
                back_up_heap, log_enabled, m);
        if(log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Graf dla wierzchołków nieparzystego stopnia:\n");
            odd_graph.print_tree_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        List<Vertice> odd_vertices_data = get_odd_vertices_list_for_visualization(MST, gr);
        Graph odd_visualization = new Graph(odd_vertices, odd_graph.getEdges());
        odd_visualization.setVertices_data(odd_vertices_data);
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Graf pełny dla wierzchołów nieparzystego stopnia z MST",
                is_final_path, false, 
                final_path_list, new ArrayList<Integer>(),
                odd_visualization, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        //znajdujemy przykładowe skojarzenie dla tego podgrafu
        m.addLog(m.getTekstKonsoli(), "\n- Znalezienie przykładowego skojarzenia dla utworzonego podgrafu -\n");
        Graph initial_matching = find_initial_matching(odd_graph);
        if(log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Przykładowe skojarzenie:\n");
            initial_matching.print_tree_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        temp_path = new ArrayList<Integer>();
        double temp_cost = 0;
        for(int i = 0; i < initial_matching.getEdges().size(); i++) {
            int beginning = initial_matching.getEdges().get(i).getBeginning();
            int end = initial_matching.getEdges().get(i).getEnd();
            temp_path.add(beginning);
            temp_path.add(end);
            temp_path.add(-2);
            temp_cost += initial_matching.getEdges().get(i).getWeight();
        }
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations,
                "<html><p>Przykładowe skojarzenie doskonałe; koszt =</p><p>" +
                        String.valueOf(temp_cost) + "</p></html>",
                is_final_path, true, 
                final_path_list, temp_path,
                odd_visualization, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        //znajdujemy najtańsze skojarzenie doskonałe dla tego podgrafu
        m.addLog(m.getTekstKonsoli(), "\n- Poszukiwanie najtańszego skojarzenia doskonałego dla utworzonego podgrafu -\n");
        
        Another_helper perfect_matching_object = find_perfect_matching(odd_graph, initial_matching,
                log_enabled, m, wtm_graph_object, odd_visualization, start, stop);
        stop = perfect_matching_object.getStop();
        start = perfect_matching_object.getStart();
        Graph perfect_matching = perfect_matching_object.getGraph();
        if(log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Najtańsze skojarzenie doskonałe w podgrafie:\n");
            perfect_matching.print_tree_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        wtm_graph_object = perfect_matching_object.getWtm_graph_object();
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations,
                "Najtańsze skojarzenie doskonałe",
                is_final_path, true, 
                final_path_list, final_path_list.get(final_path_list.size() - 1),
                odd_visualization, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        //dodajemy krawędzie skojarzenia do grafu
        m.addLog(m.getTekstKonsoli(), "\n- Dodanie najtańszego skojarzenia doskonałego do MST -\n");
        for(int i = 0; i < perfect_matching.getEdges().size(); i++){
            MST.getEdges().add(perfect_matching.getEdges().get(i));
        }
        if(log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Graf powstały z połączenia MST z najtańszym skojarzeniem doskonałym dla wierzchołków nieparzystego stopnia:\n");
            MST.print_tree_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        List<Vertice> new_visualization_graph_vertices = get_MST_vertices_list_for_visualization(MST, gr);
        Graph new_visualization_graph = new Graph(MST.getVertices(), MST.getEdges());
        new_visualization_graph.setVertices_data(new_visualization_graph_vertices);
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations,
                "<html><p>Połączenie MST ze znalezionym</p>" +
                        "<p>najtańszym skojarzeniem doskonałym</p></html>",
                is_final_path, false, 
                final_path_list, new ArrayList<Integer>(),
                new_visualization_graph, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        //znajdowanie cyklu Eulera
        m.addLog(m.getTekstKonsoli(), "\n- Poszukiwanie cyklu Eulera -\n");
        List<Integer> path = Two_approximation.Hierholzer(MST, log_enabled, m,
                first_vertice_mode, first_vertice_id);
        List<Integer> rev_path = new ArrayList<Integer>();
        rev_path.addAll(path);
        Collections.reverse(rev_path);
        //usuwanie powtórzonych wierzchołków
        m.addLog(m.getTekstKonsoli(), "\n- Usuwanie powtórzonych wierzchołków -\n");
        path = new ArrayList<>(new LinkedHashSet<>(path));
        path.add(path.get(0));
        rev_path = new ArrayList<>(new LinkedHashSet<>(rev_path));
        rev_path.add(path.get(0));
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        m.addLog(m.getTekstKonsoli(), "Zakończono algorytm Christofidesa\n");
        Two_approximation.print_int_list(path, "Rozwiązanie", m);
        Two_approximation.print_int_list(rev_path, "Alternatywne rozwiązanie", m);
        m.addLog(m.getTekstKonsoli(), "\n--------------------\n");
        
        m.setPath1(path);
        m.setPath2(rev_path);
        m.setIs_path2(true);
        
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations,
                "Cykl Eulera w nowym grafie",
                is_final_path, true, 
                final_path_list, m.compare_two_path(new_visualization_graph, false),
                new_visualization_graph, new ArrayList<Vector<Double>>());
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozwiązanie problemu komiwojażera",
                is_final_path, true, 
                final_path_list, m.compare_two_path(gr, true),
                gr, new ArrayList<Vector<Double>>());
        
        return wtm_graph_object;
    }

    public static List<Integer> get_odd_vertices_list(Graph MST, boolean log_enabled, MenuView m) {
        List<Integer> odd_vertices = new ArrayList<Integer>();
        for (int i = 0; i < MST.getVertices().size(); i++) {
            int vertice = MST.getVertices().get(i);
            int degree = 0;
            for (int j = 0; j < MST.getEdges().size(); j++) {
                Edge edge = MST.getEdges().get(j);
                int beginning = edge.getBeginning();
                int end = edge.getEnd();
                if (vertice == beginning) {
                    degree++;
                }
                if (vertice == end) {
                    degree++;
                }
            }
            if (log_enabled) {
                m.addLog(m.getTekstKonsoli(), "Wierzchołek ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(vertice));
                m.addLog(m.getTekstKonsoli(), " jest stopnia: ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(degree));
                m.addLog(m.getTekstKonsoli(), "\n");
            }
            if (degree % 2 == 1) {
                odd_vertices.add(vertice);
            }
        }
        if(log_enabled) {
            Two_approximation.print_int_list(odd_vertices, "Wierzchołki stopnia nieparzystego", m);
        }
        return odd_vertices;
    }
    
    public static List<Vertice> get_odd_vertices_list_for_visualization(Graph MST, Graph gr) {
        List<Vertice> odd_vertices = new ArrayList<Vertice>();
        for (int i = 0; i < MST.getVertices().size(); i++) {
            int vertice = MST.getVertices().get(i);
            int degree = 0;
            for (int j = 0; j < MST.getEdges().size(); j++) {
                Edge edge = MST.getEdges().get(j);
                int beginning = edge.getBeginning();
                int end = edge.getEnd();
                if (vertice == beginning) {
                    degree++;
                }
                if (vertice == end) {
                    degree++;
                }
            }
            if (degree % 2 == 1) {
                for(int j = 0; j < gr.getVertices_data().size(); j++) {
                    if (gr.getVertices_data().get(j).getId() == vertice) {
                        Vertice v = new Vertice(vertice,
                                gr.getVertices_data().get(j).getX(),
                                gr.getVertices_data().get(j).getY(),
                                gr.getVertices_data().get(j).getName());
                        odd_vertices.add(v);
                        break;
                    }
                }

            }
        }
        return odd_vertices;
    }
    
    public static List<Vertice> get_MST_vertices_list_for_visualization(Graph MST, Graph gr) {
        List<Vertice> vertices = new ArrayList<Vertice>();
        for(int i = 0; i < MST.getVertices().size(); i++) {
            int vertice = MST.getVertices().get(i);
            for(int j = 0; j < gr.getVertices_data().size(); j++) {
                if (gr.getVertices_data().get(j).getId() == vertice) {
                    Vertice v = new Vertice(vertice,
                            gr.getVertices_data().get(j).getX(),
                            gr.getVertices_data().get(j).getY(),
                            gr.getVertices_data().get(j).getName());
                    vertices.add(v);
                    break;
                }
            }
        }
        return vertices;
    }

    public static Graph get_a_graph_for_odd_vertices(List<Integer> odd_vertices, int vertices_amount, Heap heap, boolean log_enabled, MenuView m) {
        //informacja w jakiej kolejności są przechowywane wierzchołki w grafie
        int[] labels_for_graph = get_labels_for_graph(vertices_amount, heap);
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = get_a_graph_as_adjacency_matrix(vertices_amount, heap, labels_for_graph);
        //informacja w jakiej kolejności są przechowywane wierzchołki w odd_vertices
        int[] labels_for_odd_vertices = get_labels_for_odd_vertices(odd_vertices,
                vertices_amount, labels_for_graph);

        if(log_enabled) {
            print_some_data(odd_vertices, vertices_amount, labels_for_graph,
                    labels_for_odd_vertices, graph, m);
        }

        Graph odd_graph = get_a_graph_with_shortest_paths(odd_vertices,
                labels_for_odd_vertices, graph, log_enabled, m);
        return odd_graph;
    }

    public static int[] get_labels_for_graph(int vertices_amount, Heap heap) {
        int[] labels_for_graph = new int[vertices_amount]; //informacja w jakiej kolejności
        //są przechowywane wierzchołki w grafie
        int counter = 0;

        for (int i = 0; i < heap.getHeap().size(); i++) {
            Edge edge = heap.getHeap().get(i);
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
            Heap heap, int[] labels_for_graph) {
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = new double[vertices_amount][vertices_amount];
        for (int i = 0; i < heap.getHeap().size(); i++) {
            Edge edge = heap.getHeap().get(i);
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

    public static int[] get_labels_for_odd_vertices(List<Integer> odd_vertices,
            int vertices_amount, int[] labels_for_graph) {
        //informacja w jakiej kolejności są przechowywane wierzchołki w odd_vertices
        int[] labels_for_odd_vertices = new int[odd_vertices.size()];

        for (int i = 0; i < odd_vertices.size(); i++) {
            for (int j = 0; j < vertices_amount; j++) {
                if (odd_vertices.get(i) == labels_for_graph[j]) {
                    labels_for_odd_vertices[i] = j;
                }
            }
        }
        return labels_for_odd_vertices;
    }

    public static void print_some_data(List<Integer> odd_vertices, int vertices_amount,
            int[] labels_for_graph, int[] labels_for_odd_vertices, double[][] graph, MenuView m) {
        m.addLog(m.getTekstKonsoli(), "Labels for graph:\n");
        for (int pr = 0; pr < vertices_amount; pr++) {
            m.addLog(m.getTekstKonsoli(), "vertice: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(pr));
            m.addLog(m.getTekstKonsoli(), " -> label: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_graph[pr]));
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "Labels for odd vertices:\n");
        for (int pr = 0; pr < odd_vertices.size(); pr++) {
            m.addLog(m.getTekstKonsoli(), "vertice: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(odd_vertices.get(pr)));
            m.addLog(m.getTekstKonsoli(), " -> label: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(labels_for_odd_vertices[pr]));
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

    public static Graph get_a_graph_with_shortest_paths(List<Integer> odd_vertices,
            int[] labels_for_odd_vertices, double[][] graph,
            boolean log_enabled, MenuView m) {
        Graph odd_graph = new Graph(odd_vertices);

        for (int i = 0; i < odd_vertices.size(); i++) {
            int start = labels_for_odd_vertices[i];
            double[] distance_data = Brute_force.Dijkstra(start, graph).getD();
            for (int j = i + 1; j < odd_vertices.size(); j++) {
                int end = labels_for_odd_vertices[j];
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), odd_vertices.get(i).toString());
                    m.addLog(m.getTekstKonsoli(), "-");
                    m.addLog(m.getTekstKonsoli(), odd_vertices.get(j).toString());
                    m.addLog(m.getTekstKonsoli(), " distance: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(distance_data[end]));
                    m.addLog(m.getTekstKonsoli(), "\n");
                }
                Edge edge = new Edge(odd_vertices.get(i), odd_vertices.get(j), distance_data[end]);
                odd_graph.getEdges().add(edge);
            }
        }
        return odd_graph;
    }

    public static Graph find_initial_matching(Graph odd_graph){
        Graph initial_matching = new Graph(new ArrayList<Integer>());
        int unmatched_vertices = odd_graph.getVertices().size();
        
        for(int i = 0; i < odd_graph.getEdges().size(); i++){
            boolean add_flag = true;
            Edge edge = odd_graph.getEdges().get(i);
            int beginning = edge.getBeginning();
            int end = edge.getEnd();
            for(int j = 0; j < initial_matching.getVertices().size(); j++) {
                int vertice = initial_matching.getVertices().get(j);
                if(beginning == vertice || end == vertice){
                    add_flag = false;
                    break;
                }
            }
            if(add_flag){
                initial_matching.getVertices().add(beginning);
                initial_matching.getVertices().add(end);
                initial_matching.getEdges().add(edge);
                if(initial_matching.getVertices().size() >= unmatched_vertices - 1){
                    break;
                }
            }
        }
        return initial_matching;
    }
    
    public static Another_helper find_perfect_matching(Graph odd_graph, Graph initial_matching,
            boolean log_enabled, MenuView m, WTM_graph_object wtm_graph_object,
            Graph odd_visualization, long start, long stop){
        List<Edge> edges = new ArrayList<Edge>();

        for(int i = 0; i < odd_graph.getEdges().size(); i++){
            edges.add(odd_graph.getEdges().get(i));
        }
        Collections.sort(edges);
        
        List<Integer> vertices_of_perfect_matching = new ArrayList<Integer>();
        for (int i = 0; i < initial_matching.getVertices().size(); i++) {
            vertices_of_perfect_matching.add(initial_matching.getVertices().get(i));
        }
        int amount_of_edges_in_matching = (int)(vertices_of_perfect_matching.size() / 2);

        double boundary_cost = 0;
        List<Edge> edges_of_perfect_matching = new ArrayList<Edge>();
        for (int i = 0; i < initial_matching.getEdges().size(); i++) {
            boundary_cost += initial_matching.getEdges().get(i).getWeight();
            edges_of_perfect_matching.add(initial_matching.getEdges().get(i));
        }
        
        List<Edge> edges_of_actual_matching = new ArrayList<Edge>();
        List<Integer> vertices_of_actual_matching = new ArrayList<Integer>();

        int[] pos = new int[amount_of_edges_in_matching];
        int depth = 0;
        pos[0] = 0;
        pos = update_next_pos(pos, 1);
        double current_cost = 0;
        Current_matching_helper_class result = find_cheap_matching(
                pos, amount_of_edges_in_matching, edges, current_cost, boundary_cost,
                vertices_of_actual_matching, edges_of_actual_matching,
                vertices_of_perfect_matching, edges_of_perfect_matching, depth,
                log_enabled, m, wtm_graph_object, odd_visualization, start, stop);
        stop = result.getStop();
        start = result.getStart();
        
        vertices_of_perfect_matching = result.getVertices_of_perfect_matching();
        edges_of_perfect_matching = result.getEdges_of_perfect_matching();
        boundary_cost = result.getBoundary_cost();
        wtm_graph_object = result.getWtm_graph_object();

        return new Another_helper(new Graph(vertices_of_perfect_matching, edges_of_perfect_matching),
                wtm_graph_object, start, stop);
    }

    public static Current_matching_helper_class find_cheap_matching(
            int[] pos, int amount_of_edges_to_add,
            List<Edge> edges, double current_cost, double boundary_cost,
            List<Integer> vertices_of_actual_matching, List<Edge> edges_of_actual_matching,
            List<Integer> vertices_of_perfect_matching, List<Edge> edges_of_perfect_matching,
            int depth, boolean log_enabled, MenuView m, WTM_graph_object wtm_graph_object,
            Graph odd_visualization, long start, long stop) {

        for (int x = pos[depth]; x + amount_of_edges_to_add - 1 < edges.size(); x++) {
            pos[depth] = x;
            double optimistic_cost = current_cost;
            for (int i = 0; i < amount_of_edges_to_add; i++) {
                optimistic_cost += edges.get(x + i).getWeight();
            }
            if (optimistic_cost >= boundary_cost) {
                Current_matching_helper_class result = new Current_matching_helper_class(
                        false, vertices_of_perfect_matching, edges_of_perfect_matching,
                        current_cost, boundary_cost, wtm_graph_object, start, stop);
                return result;
            }

            Edge edge = edges.get(x);
            int beginning = edge.getBeginning();
            int end = edge.getEnd();
            boolean add_flag = true;
            for (int i = 0; i < vertices_of_actual_matching.size(); i++) {
                if (vertices_of_actual_matching.get(i) == beginning
                        || vertices_of_actual_matching.get(i) == end) {
                    add_flag = false;
                    break;
                }
            }

            if (add_flag) {
                edges_of_actual_matching.add(edge);
                vertices_of_actual_matching.add(beginning);
                vertices_of_actual_matching.add(end);
                current_cost += edge.getWeight();
                amount_of_edges_to_add -= 1;
                
                if (amount_of_edges_to_add > 0) {
                    pos = update_next_pos(pos, depth+1);
                    Current_matching_helper_class result = find_cheap_matching(
                            pos,
                            amount_of_edges_to_add,
                            edges,
                            current_cost,
                            boundary_cost,
                            vertices_of_actual_matching,
                            edges_of_actual_matching,
                            vertices_of_perfect_matching,
                            edges_of_perfect_matching,
                            depth+1,
                            log_enabled,
                            m,
                            wtm_graph_object,
                            odd_visualization,
                            start,
                            stop);
                    stop = result.getStop();
                    start = result.getStart();
                    
                    vertices_of_perfect_matching = result.getVertices_of_perfect_matching();
                    edges_of_perfect_matching = result.getEdges_of_perfect_matching();
                    boundary_cost = result.getBoundary_cost();
                    wtm_graph_object = result.getWtm_graph_object();
                    
                    vertices_of_actual_matching.remove(vertices_of_actual_matching.size() - 1);
                    vertices_of_actual_matching.remove(vertices_of_actual_matching.size() - 1);
                    if(!(result.isSuccess())){
                        current_cost -= edges_of_actual_matching.get(edges_of_actual_matching.size() - 1).
                                getWeight();
                    }
                    edges_of_actual_matching.remove(edges_of_actual_matching.size() - 1);

                    amount_of_edges_to_add += 1;
                    
                    if(result.isSuccess()){
                        vertices_of_actual_matching.remove(vertices_of_actual_matching.size() - 1);
                        vertices_of_actual_matching.remove(vertices_of_actual_matching.size() - 1);

                        current_cost -= edges_of_actual_matching.get(edges_of_actual_matching.size() - 1).
                                getWeight();
                        edges_of_actual_matching.remove(edges_of_actual_matching.size() - 1);
                    }
                }
                if (amount_of_edges_to_add == 0) {
                    vertices_of_perfect_matching.clear();
                    for (int i = 0; i < vertices_of_actual_matching.size(); i++) {
                        vertices_of_perfect_matching.add(vertices_of_actual_matching.get(i));
                    }
                    
                    edges_of_perfect_matching.clear();
                    for (int i = 0; i < edges_of_actual_matching.size(); i++) {
                        edges_of_perfect_matching.add(edges_of_actual_matching.get(i));
                    }
                    
                    boundary_cost = current_cost;
                    
                    if (log_enabled) {
                        m.addLog(m.getTekstKonsoli(), "Found new perfect matching:\n");
                        new Graph(vertices_of_perfect_matching, edges_of_perfect_matching).print_tree_log(m);
                        m.addLog(m.getTekstKonsoli(), "New minimum cost: ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(boundary_cost));
                        m.addLog(m.getTekstKonsoli(), "\n");
                    }
                    
                    stop = System.nanoTime();
                    m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                            Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
                    
                    List<Integer> temp_path = new ArrayList<Integer>();
                    for (int i = 0; i < edges_of_perfect_matching.size(); i++) {
                        int v1 = edges_of_perfect_matching.get(i).getBeginning();
                        int v2 = edges_of_perfect_matching.get(i).getEnd();
                        temp_path.add(v1);
                        temp_path.add(v2);
                        temp_path.add(-2);
                    }
                    wtm_graph_object = add_new_wtm_graph(wtm_graph_object.getWtm_graphs(),
                            wtm_graph_object.getExplanations(),
                            "<html><p>Znaleziono nowe najtańsze skojarzenie doskonałe; koszt =</p><p>" +
                                    String.valueOf(boundary_cost) + "</p></html>",
                            wtm_graph_object.getIs_final_path(), true,
                            wtm_graph_object.getFinal_path_list(), temp_path,
                            odd_visualization, new ArrayList<Vector<Double>>());
                    
                    start = System.nanoTime();
                    
                    Current_matching_helper_class result = new Current_matching_helper_class(
                            true, vertices_of_perfect_matching, edges_of_perfect_matching,
                            current_cost, boundary_cost, wtm_graph_object, start, stop);
                    return result;
                }
            }
        }
        Current_matching_helper_class result = new Current_matching_helper_class(
                false, vertices_of_perfect_matching, edges_of_perfect_matching,
                current_cost, boundary_cost, wtm_graph_object, start, stop);
        return result;
    }
    
    public static int[] update_next_pos(int[] pos, int depth){
        for(int i = depth; i < pos.length; i++){
            pos[i] = pos[i-1] + 1;
        }
        return pos;
    }
}
