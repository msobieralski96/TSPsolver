/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import com.mycompany.tsp_solver.MenuView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author mic45
 */
public class WTM {
    
    public static WTM_graph_object algorithm(Graph gr, boolean log_enabled, MenuView m,
            boolean first_vertice_mode, int first_vertice_id,
            int neurons_amount,
            int epochs_amount,
            double learning_rate_factor,
            double decrease_learning_rate_factor,
            double neighborhood_radius,
            double decrease_neighborhood_radius) {

        List<Graph> wtm_graphs = new ArrayList<Graph>();
        List<String> explanations = new ArrayList<String>();
        List<Boolean> is_final_path = new ArrayList<Boolean>();
        List<List<Integer>> final_path_list = new ArrayList<List<Integer>>();
        m.addLog(m.getTekstKonsoli(), "\nRozpoczęto algorytm WTM\n");
        
        long start, stop;
        start = System.nanoTime();
        
        m.addLog(m.getTekstKonsoli(), "\n- Przygotowywanie sieci neuronowej -\n");
        //wektory uczące - miasta
        List<Vector<Double>> learner_vectors = new ArrayList<Vector<Double>>();
        
        //neurony - losowe współrzędne
        List<Vector<Double>> neurons = new ArrayList<Vector<Double>>();
        
        //liczba wektorów (miast)
        int lv_amount = gr.getVertices_data().size();
        
        double min_x = 1000000;
        double max_x = -1000000;
        double min_y = 1000000;
        double max_y = -1000000;
        int first_vertice_pos = (-1);
        for(int i = 0; i < lv_amount; i++) {
            double x = gr.getVertices_data().get(i).getX();
            double y = gr.getVertices_data().get(i).getY();
            learner_vectors.add(new Vector<Double>(Arrays.asList(
                    Double.valueOf(x),
                    Double.valueOf(y))));
            if(x < min_x) {
                min_x = x;
            }
            else if(x > max_x) {
                max_x = x;
            }
            if(y < min_y) {
                min_y = y;
            }
            else if(y > max_y) {
                max_y = y;
            }
            if(first_vertice_mode &&
                    first_vertice_pos == (-1) &&
                    gr.getVertices_data().get(i).getId() == first_vertice_id) {
                first_vertice_pos = i;
            }
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time2_label(), start, stop, 0);
        
        WTM_graph_object wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Graf wejściowy",
                is_final_path, false, 
                final_path_list, new ArrayList<Integer>(),
                gr, neurons);
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        double range_x = Math.abs(max_x - min_x);
        double range_y = Math.abs(max_y - min_y);
        min_x -= range_x * 0.25;
        max_x += range_x * 0.25;
        min_y -= range_y * 0.25;
        max_y += range_y * 0.25;
        
        Random rand = new Random();
        double value, value2;
        for(int i = 0; i < neurons_amount; i++) {
            value = min_x + (max_x - min_x) * rand.nextDouble();
            value2 = min_y + (max_y - min_y) * rand.nextDouble();
            neurons.add(new Vector<Double>(Arrays.asList(value, value2)));
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time2_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time2_label().getText()));
        
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozmieszczenie neuronów przed procesem uczenia",
                is_final_path, false, 
                final_path_list, new ArrayList<Integer>(),
                gr, neurons);
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        start = System.nanoTime();
        
        m.addLog(m.getTekstKonsoli(), "\n- Rozpoczęto proces uczenia sieci neuronowej -\n");
        for(int i = 0; i < epochs_amount; i++) { //epoka
            if(log_enabled) {
                m.addLog(m.getTekstKonsoli(), "\n- Rozpoczęto epokę nr ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(i + 1));
                m.addLog(m.getTekstKonsoli(), " -\n");
                
                print_epoch_parameters(m, learner_vectors, neurons, epochs_amount,
                        learning_rate_factor, decrease_learning_rate_factor,
                        neighborhood_radius, decrease_neighborhood_radius, gr);
            }
            
            //kopia listy wektorów uczących
            List<Vector<Double>> learner_vectors_copy = new ArrayList<Vector<Double>>();
            for(int j = 0; j < learner_vectors.size(); j++) {
                value = learner_vectors.get(j).get(0);
                value2 = learner_vectors.get(j).get(1);
                learner_vectors_copy.add(new Vector<Double>(Arrays.asList(value, value2)));
            }
            
            while(learner_vectors_copy.size() > 0) {
                // losujemy wektor uczący (wszystkie muszą być wylosowane w epoce)
                int k = rand.nextInt(learner_vectors_copy.size());
                double learner_x = learner_vectors_copy.get(k).get(0);
                double learner_y = learner_vectors_copy.get(k).get(1);
                learner_vectors_copy.remove(k);
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "\nWybrano wektor uczący o współrzędnych: (");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(learner_x));
                    m.addLog(m.getTekstKonsoli(), ", ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(learner_y));
                    m.addLog(m.getTekstKonsoli(), ")\n");
                }
            
                // dla każdego neuronu obliczamy dystans od wektora uczącego
                double[] distance = new double[neurons_amount];
                double min = 999999999; //najmniejszy dystans
                int winner = -1; //wektor zwycięski (ma najmniejszy dystans do wektora uczącego)
                for(int j = 0; j < neurons_amount; j++) {
                    double neuron_x = neurons.get(j).get(0);
                    double neuron_y = neurons.get(j).get(1);
                    distance[j] = Math.pow((learner_x - neuron_x), 2) + Math.pow((learner_y - neuron_y), 2);
                    if(distance[j] < min) {
                        min = distance[j];
                        winner = j;
                    }
                }
            
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "Neuron zwycięski: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(winner));
                    m.addLog(m.getTekstKonsoli(), " (");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(neurons.get(winner).get(0)));
                    m.addLog(m.getTekstKonsoli(), ", ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(neurons.get(winner).get(1)));
                    m.addLog(m.getTekstKonsoli(), ")\n");
                    m.addLog(m.getTekstKonsoli(), "Uczenie neuronów\n");
                }
                
                //jeżeli neuron znajduje się w promieniu sąsiedztwa - zmień jego współrzędne
                for(int j = 0; j < neurons_amount; j++) {
                    if(Math.sqrt(
                        Math.pow((neurons.get(winner).get(0) - neurons.get(j).get(0)), 2) +
                            Math.pow((neurons.get(winner).get(1) - neurons.get(j).get(1)), 2))
                            <= neighborhood_radius) { //jeżeli funkcja sąsiedztwa == 1
                        double new_x = neurons.get(j).get(0) +
                                learning_rate_factor * (learner_x - neurons.get(j).get(0));
                        double new_y = neurons.get(j).get(1) +
                                learning_rate_factor * (learner_y - neurons.get(j).get(1));
                        neurons.set(j, new Vector<Double>(Arrays.asList(new_x, new_y)));
                    }
                }
            }
            
            // zmniejsz liniowo współczynnik szybkości uczenia
            learning_rate_factor -= decrease_learning_rate_factor;
            // zmniejsz liniowo promień sąsiedztwa
            neighborhood_radius -= decrease_neighborhood_radius;

            stop = System.nanoTime();
            m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                    Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
            
            wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                    explanations, "Rozmieszczenie neuronów po epoce " + String.valueOf(i+1),
                    is_final_path, false,
                    final_path_list, new ArrayList<Integer>(),
                    gr, neurons);
            wtm_graphs = wtm_graph_object.getWtm_graphs();
            explanations = wtm_graph_object.getExplanations();
            is_final_path = wtm_graph_object.getIs_final_path();
            final_path_list = wtm_graph_object.getFinal_path_list();
            
            start = System.nanoTime();
        }
        
        m.addLog(m.getTekstKonsoli(), "\n- Zakończono proces uczenia sieci neuronowej -\n");
        m.addLog(m.getTekstKonsoli(), "\n- Wyznaczanie ścieżki -\n");
        Graphs_n_path path_result = get_path(gr, learner_vectors, neurons, wtm_graph_object,
                log_enabled, m, first_vertice_mode, first_vertice_pos, start, stop);
        stop = path_result.getStop();
        start = path_result.getStart();
        List<Integer> path = path_result.getPath();
        wtm_graph_object = path_result.getWtm_graph_object();
        wtm_graphs = wtm_graph_object.getWtm_graphs();
        explanations = wtm_graph_object.getExplanations();
        is_final_path = wtm_graph_object.getIs_final_path();
        final_path_list = wtm_graph_object.getFinal_path_list();
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        m.addLog(m.getTekstKonsoli(), "Zakończono algorytm WTM\n");
        
        m.setPath1(path);
        m.setIs_path2(false);
        
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozwiązanie problemu komiwojażera",
                is_final_path, true,
                final_path_list, m.compare_two_path(gr, true),
                gr, new ArrayList<Vector<Double>>());
        
        return wtm_graph_object;
    }
    
    static void print_epoch_parameters(MenuView m,
            List<Vector<Double>> learner_vectors,
            List<Vector<Double>> neurons,
            int epochs_amount,
            double learning_rate_factor,
            double decrease_learning_rate_factor,
            double neighborhood_radius,
            double decrease_neighborhood_radius,
            Graph gr) {
        m.addLog(m.getTekstKonsoli(), "\nParametry epoki:\n");
        m.addLog(m.getTekstKonsoli(), "Wektory uczące (");
        m.addLog(m.getTekstKonsoli(), String.valueOf(learner_vectors.size()));
        m.addLog(m.getTekstKonsoli(), ") :\n");
        for (int i = 0; i < learner_vectors.size(); i++) {
            m.addLog(m.getTekstKonsoli(), "Wektor ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(i));
            m.addLog(m.getTekstKonsoli(), ": ");
            m.addLog(m.getTekstKonsoli(), learner_vectors.get(i).get(0).toString());
            m.addLog(m.getTekstKonsoli(), ", ");
            m.addLog(m.getTekstKonsoli(), learner_vectors.get(i).get(1).toString());
            m.addLog(m.getTekstKonsoli(), " (id: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(i).getId()));
            m.addLog(m.getTekstKonsoli(), ", name: ");
            m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(i).getName());
            m.addLog(m.getTekstKonsoli(), ")\n");
        }
        m.addLog(m.getTekstKonsoli(), "Neurony (");
        m.addLog(m.getTekstKonsoli(), String.valueOf(neurons.size()));
        m.addLog(m.getTekstKonsoli(), ") :\n");
        for (int i = 0; i < neurons.size(); i++) {
            m.addLog(m.getTekstKonsoli(), "Neuron ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(i));
            m.addLog(m.getTekstKonsoli(), ": ");
            m.addLog(m.getTekstKonsoli(), neurons.get(i).get(0).toString());
            m.addLog(m.getTekstKonsoli(), ", ");
            m.addLog(m.getTekstKonsoli(), neurons.get(i).get(1).toString());
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        m.addLog(m.getTekstKonsoli(), "----------");
        m.addLog(m.getTekstKonsoli(), "\nliczba epok: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(epochs_amount));
        m.addLog(m.getTekstKonsoli(), "\nwspółczynnik szybkości uczenia: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(learning_rate_factor));
        m.addLog(m.getTekstKonsoli(), "\nwartość pomniejszająca współczynnik szybkości uczenia co każdą epokę: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(decrease_learning_rate_factor));
        m.addLog(m.getTekstKonsoli(), "\npromień sąsiedztwa: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(neighborhood_radius));
        m.addLog(m.getTekstKonsoli(), "\nwartość pomniejszająca promień sąsiedztwa co każdą epokę: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(decrease_neighborhood_radius));
        m.addLog(m.getTekstKonsoli(), "\n----------\n");
    }
    
    static Graphs_n_path get_path(Graph gr,
            List<Vector<Double>> learner_vectors,
            List<Vector<Double>> neurons,
            WTM_graph_object wtm_graph_object, boolean log_enabled, MenuView m,
            boolean first_vertice_mode, int first_vertice_pos,
            long start, long stop){
                
        List<Vector<Double>> neurons_cp = new ArrayList<Vector<Double>>();
        for(int i = 0; i < neurons.size(); i++) {
            double value = neurons.get(i).get(0);
            double value2 = neurons.get(i).get(1);
            neurons_cp.add(new Vector<Double>(Arrays.asList(value, value2)));
        }
        
        // 1) dla każdego miasta znajdź najbliższy neuron - oznaczmy neurony miast
        m.addLog(m.getTekstKonsoli(), "\n- Step 1 - znajdowanie neuronów najbliższych wektorom uczącym -\n");
        List<Integer> closest_to_learner_list = new ArrayList<Integer>();
        for(int i = 0; i < learner_vectors.size(); i++) {
            int winner = 0;
            double distance = 1000000;
            for(int j = 0; j < neurons_cp.size(); j++) {
                double current_distance = Math.sqrt(
                        Math.pow(learner_vectors.get(i).get(0) - neurons_cp.get(j).get(0), 2) +
                        Math.pow(learner_vectors.get(i).get(1) - neurons_cp.get(j).get(1), 2));
                if (current_distance < distance) {
                    distance = current_distance;
                    winner = j;
                }
            }
            closest_to_learner_list.add(winner);
        }
        
        if(log_enabled) {
            for (int i = 0; i < closest_to_learner_list.size(); i++) {
                m.addLog(m.getTekstKonsoli(), "Najbliższy neuron wektora ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(i));
                m.addLog(m.getTekstKonsoli(), " (id: ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(i).getId()));
                m.addLog(m.getTekstKonsoli(), ", name: ");
                m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(i).getName());
                m.addLog(m.getTekstKonsoli(), ") : neuron ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(closest_to_learner_list.get(i)));
                m.addLog(m.getTekstKonsoli(), "\n");
            }
        }
        
        // 2) połącz ze sobą neurony, przy czym każdy łącz z najbliższym (niepołączonym)
        m.addLog(m.getTekstKonsoli(), "\n- Step 2 - łączenie 'sąsiednich' neuronów w ścieżkę -\n");
        List<Integer> neuron_path = new ArrayList<Integer>();
        List<Boolean> neuron_in_path = new ArrayList<Boolean>();
        
        for(int i = 0; i < neurons_cp.size(); i++) {
            neuron_in_path.add(false);
        }
        int next = 0;
        if(first_vertice_mode) {
            next = closest_to_learner_list.get(first_vertice_pos);
        }
        if(log_enabled) {
            m.addLog(m.getTekstKonsoli(), "Pierwszy neuron: neuron ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(next));
            m.addLog(m.getTekstKonsoli(), "\n");
        }
        neuron_path.add(next);
        neuron_in_path.set(next, true);
        int prev = next;
        while(not_all_neurons_connected(neuron_in_path)) {
            double distance = 1000000;
            for(int i = 0; i < neurons_cp.size(); i++) {
                if(neuron_in_path.get(i) == true) {
                    continue;
                }
                double current_distance = Math.sqrt(
                    Math.pow(neurons_cp.get(prev).get(0) - neurons_cp.get(i).get(0), 2) +
                    Math.pow(neurons_cp.get(prev).get(1) - neurons_cp.get(i).get(1), 2));
                if(current_distance < distance) {
                    distance = current_distance;
                    next = i;
                }
            }
            if(log_enabled) {
                m.addLog(m.getTekstKonsoli(), "Połączono z: neuron ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(next));
                m.addLog(m.getTekstKonsoli(), " \t(odległość: ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(distance));
                m.addLog(m.getTekstKonsoli(), ")\n");
            }
            neuron_path.add(next);
            neuron_in_path.set(next, true);
            prev = next;
        }
        
        // 3) odczytując trasę neuronów, jeśli napotkamy na neuron miasta -
        //    dodajemy do rozwiązania miasto
        m.addLog(m.getTekstKonsoli(), "\n- Step 3 - wyznaczanie trasy na podstawie ścieżki neuronów -\n");
        
        List<Integer> path = new ArrayList<Integer>();
        
        for(int i = 0; i < neuron_path.size(); i++) {
            if (log_enabled) {
                m.addLog(m.getTekstKonsoli(), "Neuron ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(neuron_path.get(i)));
                m.addLog(m.getTekstKonsoli(), "\n");
            }
            for(int j = 0; j < closest_to_learner_list.size(); j++) {
                if(neuron_path.get(i).equals(closest_to_learner_list.get(j))) {
                    path.add(gr.getVertices_data().get(j).getId());
                    if (log_enabled) {
                        m.addLog(m.getTekstKonsoli(), "Wykryto wektor: ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(j));
                        m.addLog(m.getTekstKonsoli(), " (id: ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(gr.getVertices_data().get(j).getId()));
                        m.addLog(m.getTekstKonsoli(), ", name: ");
                        m.addLog(m.getTekstKonsoli(), gr.getVertices_data().get(j).getName());
                        m.addLog(m.getTekstKonsoli(), ")\n");
                    }
                }
            }
        }
        path.add(path.get(0));
        
        //tworzenie grafu z neuronami i ścieżką
        List<Edge> edges = new ArrayList<Edge>();
        List<Integer> vertices = new ArrayList<Integer>();
        List<Vertice> vertices_data = new ArrayList<Vertice>();
        
        for(int i = 0; i < gr.getEdges().size(); i++){
            edges.add(gr.getEdges().get(i));
        }
        for(int i = 0; i < gr.getVertices().size(); i++){
            vertices.add(gr.getVertices().get(i));
        }
        for(int i = 0; i < gr.getVertices_data().size(); i++){
            vertices_data.add(gr.getVertices_data().get(i));
        }
        
        int not_neuron_vertices_size = vertices.size();
        int counter_id = 0;
        for(int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i) >= counter_id) {
                counter_id = vertices.get(i) + 1;
            }
        }
        
        for(int i = 0; i < neurons.size(); i++) {
            vertices.add(counter_id);
            Vertice new_vertice = new Vertice(counter_id, neurons.get(i).get(0), neurons.get(i).get(1), "Neuron " + String.valueOf(i));
            vertices_data.add(new_vertice);
            counter_id += 1;
        }
        
        int curr;
        int point_a, point_b;
        for(int i = 0; i < neuron_path.size(); i++) {
            curr = neuron_path.get(i);

            if (i < neuron_path.size() - 1) {
                next = neuron_path.get(i + 1);
            } else {
                next = neuron_path.get(0);
            }
            point_a = -1;
            point_b = -1;
            for (int j = not_neuron_vertices_size; j < vertices.size(); j++) {
                int neuron_number = j - not_neuron_vertices_size;
                if (curr == neuron_number) {
                    point_a = vertices.get(j);
                }
                if (next == neuron_number) {
                    point_b = vertices.get(j);
                }
                if (point_a != -1 && point_b != -1) {
                    edges.add(new Edge(point_a, point_b, -1));
                    break;
                }
            }
        }
        
        Graph graph_with_neurons = new Graph(vertices, edges);
        graph_with_neurons.setVertices_data(vertices_data);
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time3_label(), start, stop,
                Double.parseDouble(m.getWtm_panel().get_time3_label().getText()));
        
        List<Graph> wtm_graphs = wtm_graph_object.getWtm_graphs();
        List<String> explanations = wtm_graph_object.getExplanations();
        List<Boolean> is_final_path = wtm_graph_object.getIs_final_path();
        List<List<Integer>> final_path_list = wtm_graph_object.getFinal_path_list();
        wtm_graphs.add(graph_with_neurons);
        explanations.add("Ścieżka wyznaczona przez neurony");
        is_final_path.add(false);
        final_path_list.add(null);
        wtm_graph_object.setWtm_graphs(wtm_graphs);
        wtm_graph_object.setExplanations(explanations);
        wtm_graph_object.setIs_final_path(is_final_path);
        wtm_graph_object.setFinal_path_list(final_path_list);
        
        start = System.nanoTime();
        
        return new Graphs_n_path(path, wtm_graph_object, start, stop);
    }
    
    static boolean not_all_neurons_connected(List<Boolean> neuron_in_path) {
        for(int i = 0; i < neuron_in_path.size(); i++) {
            if(neuron_in_path.get(i) == false) {
                return true;
            }
        }
        return false;
    }
    
    static WTM_graph_object add_new_wtm_graph(
            List<Graph> wtm_graphs,
            List<String> explanations,
            String new_explanation,
            List<Boolean> is_final_path,
            Boolean this_is_final_path,
            List<List<Integer>> final_path_list,
            List<Integer> final_path,
            Graph gr,
            List<Vector<Double>> neurons) {
        
        List<Integer> vertices = new ArrayList<Integer>();
        List<Vertice> vertices_data = new ArrayList<Vertice>();
        List<Edge> edges = new ArrayList<Edge>();
        for(int i = 0; i < gr.getVertices().size(); i++) {
            vertices.add(gr.getVertices().get(i));
        }
        for(int i = 0; i < gr.getVertices_data().size(); i++) {
            vertices_data.add(gr.getVertices_data().get(i));
        }
        for(int i = 0; i < gr.getEdges().size(); i++) {
            edges.add(gr.getEdges().get(i));
        }
        
        int counter_id = 0;
        for(int i = 0; i < vertices.size(); i++) {
            if(vertices.get(i) >= counter_id) {
                counter_id = vertices.get(i) + 1;
            }
        }
        for(int i = 0; i < neurons.size(); i++) {
            vertices.add(counter_id);
            Vertice new_vertice = new Vertice(counter_id, neurons.get(i).get(0), neurons.get(i).get(1), "Neuron " + String.valueOf(i));
            vertices_data.add(new_vertice);
            counter_id += 1;
        }
        Graph epoch_graph = new Graph(vertices, edges);
        epoch_graph.setVertices_data(vertices_data);
        wtm_graphs.add(epoch_graph);
        explanations.add(new_explanation);
        is_final_path.add(this_is_final_path);
        final_path_list.add(final_path);
        WTM_graph_object wtm_graph_object = new WTM_graph_object(wtm_graphs,
                explanations, is_final_path, final_path_list);
        return wtm_graph_object;
    }
}
