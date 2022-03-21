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
public class Two_approximation {
    
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
        
        m.addLog(m.getTekstKonsoli(), "\nRozpoczęto algorytm 2-aproksymacyjny\n");
        
        long start, stop;
        start = System.nanoTime();
        
        //budowa kopca
        int vertices_amount = gr.getVertices().size();
        m.addLog(m.getTekstKonsoli(), "\n- Tworzenie kopca -\n");
        Heap heap = new Heap();
        for(int i = 0; i < gr.getEdges().size(); i++){
            Edge e = gr.getEdges().get(i);
            heap.add_to_heap(e);
        }
        if(log_enabled) {
            heap.print_heap_log(m);
        }
        
        stop = System.nanoTime();
        m.set_time(m.getWtm_panel().get_time2_label(), start, stop, 0);
        start = System.nanoTime();
        
        //minimalne drzewo rozpinajace
        m.addLog(m.getTekstKonsoli(), "\n- Szukanie MST -\n");
        Graph MST = Kruskal(heap, vertices_amount, log_enabled, m);
        
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
        
        //podwajanie krawedzi
        m.addLog(m.getTekstKonsoli(), "\n- Podwajanie krawędzi drzewa MST -\n");
        MST = double_edges(MST);
        //znajdowanie cyklu Eulera
        m.addLog(m.getTekstKonsoli(), "\n- Poszukiwanie cyklu Eulera -\n");
        List<Integer> path = Hierholzer(MST, log_enabled, m, first_vertice_mode, first_vertice_id);
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
        
        m.addLog(m.getTekstKonsoli(), "Zakończono algorytm 2-aproksymacyjny\n");
        print_int_list(path, "Rozwiązanie", m);
        print_int_list(rev_path, "Alternatywne rozwiązanie", m);
        m.addLog(m.getTekstKonsoli(), "\n--------------------\n");
        
        m.setPath1(path);
        m.setPath2(rev_path);
        m.setIs_path2(true);
        
        wtm_graph_object = add_new_wtm_graph(wtm_graphs,
                explanations, "Rozwiązanie problemu komiwojażera",
                is_final_path, true, 
                final_path_list, m.compare_two_path(gr, true),
                gr, new ArrayList<Vector<Double>>());
        
        return wtm_graph_object;
    }
    
    public static Graph Kruskal(Heap heap, int vertices_amount, boolean log_enabled, MenuView m) {
        //budowa lasu drzew skladajacych sie z pojedynczych wierzcholkow
        List<Graph> forest = new ArrayList<Graph>();
        for(int i = 0; i < vertices_amount; i++){
            List<Integer> vertices = new ArrayList<Integer>();
            vertices.add(i);
            Graph tree = new Graph(vertices);
            forest.add(tree);
        }
        if(log_enabled) {
            print_forest(forest, m);
        }
        int krok = 1;
        while(heap.getId_counter() > 0){
            Edge node = heap.pop_from_heap();
            
            if(log_enabled) {
                m.addLog(m.getTekstKonsoli(), "Krok ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(krok));
                m.addLog(m.getTekstKonsoli(), ":\n");
                m.addLog(m.getTekstKonsoli(), "Wybrano: (");
                m.addLog(m.getTekstKonsoli(), String.valueOf(node.getBeginning()));
                m.addLog(m.getTekstKonsoli(), ", ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(node.getEnd()));
                m.addLog(m.getTekstKonsoli(), ") : ");
                m.addLog(m.getTekstKonsoli(), String.valueOf(node.getWeight()));
                m.addLog(m.getTekstKonsoli(), "\n");
                heap.print_heap_log(m);
            }
            
            List<Integer> node_trees = a_find(node, forest, log_enabled, m);
            if (!(node_trees.get(0).equals(node_trees.get(1)))){
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "\nDodanie krawędzi (");
                    m.addLog(m.getTekstKonsoli(), node_trees.get(0).toString());
                    m.addLog(m.getTekstKonsoli(), ", ");
                    m.addLog(m.getTekstKonsoli(), node_trees.get(1).toString());
                    m.addLog(m.getTekstKonsoli(), ") do drzewa.\n\n");
                }
                Integer info = kompresja_sciezek(node, forest, node_trees.get(0), node_trees.get(1));
                forest = an_union(node, forest, node_trees.get(0), node_trees.get(1), info);
            } else if (log_enabled) {
                m.addLog(m.getTekstKonsoli(), "\nNie można dodać krawędzi (");
                m.addLog(m.getTekstKonsoli(), node_trees.get(0).toString());
                m.addLog(m.getTekstKonsoli(), ", ");
                m.addLog(m.getTekstKonsoli(), node_trees.get(1).toString());
                m.addLog(m.getTekstKonsoli(), "), gdyż tworzy cykl w drzewie.\n\n");
            }
            if (log_enabled) {
                print_forest(forest, m);
            }
            if(forest.size() == 1){
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "\nZnaleziono MST.\n");
                }
                break;
            }
            krok++;
        }
        return forest.get(0);
    }
    
    public static void print_forest(List<Graph> forest, MenuView m){
        m.addLog(m.getTekstKonsoli(), "LAS:\n");
        for(int i = 0; i < forest.size(); i++){
            forest.get(i).print_tree_log(m);
        }
    }
    
    //sprawdza, w ktorych poddrzewach leza wierzcholki krawedzi
    //jesli w tym samym - krawedz tworzy cykl i nie mozna jej dodac do drzewa
    //jesli w roznych - nalezy dodac krawedz do drzewa
    public static List<Integer> a_find(Edge node, List<Graph> forest, boolean log_enabled, MenuView m) {
        Integer beginning = node.getBeginning();
        Integer end = node.getEnd();
        Integer beginning_tree = -1;
        Integer end_tree = -1;
        boolean found = false;
        for(int i = 0; i < forest.size(); i++){
            List<Integer> tree_vertices = forest.get(i).getVertices();
            for(int j = 0; j < tree_vertices.size(); j++){
                if(beginning.equals(tree_vertices.get(j))) {
                    beginning_tree = i;
                    if(log_enabled) {
                        m.addLog(m.getTekstKonsoli(), "Found ");
                        m.addLog(m.getTekstKonsoli(), beginning.toString());
                        m.addLog(m.getTekstKonsoli(), " in ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(i));
                        m.addLog(m.getTekstKonsoli(), " as ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(j));
                        m.addLog(m.getTekstKonsoli(), "\n");
                    }
                    if(end_tree != -1){
                        found = true;
                    }
                }
                if(end.equals(tree_vertices.get(j))) {
                    end_tree = i;
                    if (log_enabled) {
                        m.addLog(m.getTekstKonsoli(), "Found ");
                        m.addLog(m.getTekstKonsoli(), end.toString());
                        m.addLog(m.getTekstKonsoli(), " in ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(i));
                        m.addLog(m.getTekstKonsoli(), " as ");
                        m.addLog(m.getTekstKonsoli(), String.valueOf(j));
                        m.addLog(m.getTekstKonsoli(), "\n");
                    }
                    if(beginning_tree != -1){
                        found = true;
                    }
                }
                if(found){
                    break;
                }
            }
            if(found){
                break;
            }
        }
        List<Integer> answer = new ArrayList<Integer>();
        answer.add(beginning_tree);
        answer.add(end_tree);
        return answer;
    }
    
    //zwraca informacje, do ktorego poddrzewa lasu nalezy dodac zawartosc drugiego poddrzewa
    //zawartosc Iszego drzewa >= zawartosc IIgiego drzewa -> 1
    //zawartosc Iszego drzewa <  zawartosc IIgiego drzewa -> 2
    public static Integer kompresja_sciezek(Edge node, List<Graph> forest,
            int beginning_tree, int end_tree) {
        Graph tree_one = forest.get(beginning_tree);
        Graph tree_two = forest.get(end_tree);
        if(tree_one.getVertices().size() >= tree_two.getVertices().size()){
            return 1;
        } else {
            return 2;
        }
    }
    
    //laczy dwa poddrzewa w jedno (scala dodajac mniejsze do wiekszego)
    public static List<Graph> an_union(Edge node, List<Graph> forest,
            int beginning_tree, int end_tree, Integer info) {
        if(info == 2) {
            int temp = end_tree;
            end_tree = beginning_tree;
            beginning_tree = temp;
        }
        Graph tree_one = forest.get(beginning_tree);
        Graph tree_two = forest.get(end_tree);
        for(int i = 0; i < tree_two.getVertices().size(); i++){
            tree_one.getVertices().add(tree_two.getVertices().get(i));
        }
        for(int i = 0; i < tree_two.getEdges().size(); i++){
            tree_one.getEdges().add(tree_two.getEdges().get(i));
        }
        tree_one.getEdges().add(node);
        forest.set(beginning_tree, tree_one);
        forest.remove(end_tree);
        return forest;
    }
    
    public static Graph double_edges(Graph MST) {
        int edges_amount = MST.getEdges().size();
        for(int i = 0; i < edges_amount; i++) {
            Edge new_edge = MST.getEdges().get(i);
            MST.getEdges().add(new_edge);
        }
        return MST;
    }
    
    public static List<Integer> Hierholzer(Graph MST, boolean log_enabled, MenuView m,
            boolean first_vertice_mode, int first_vertice_id) {
        List<Adjacency_list_unit> tree = convert_to_adjacency_list(MST, log_enabled, m);
        List<Integer> stack;
        List<Integer> Q;
        
        stack = new ArrayList<Integer>();
        Q = new ArrayList<Integer>();
        if (log_enabled) {
            print_int_list(stack, "S", m);
            print_int_list(Q, "Q", m);
        }
        int p = 0; //ustaw punkt startowy
        if(first_vertice_mode) {
            for(int i = 0; i < tree.size(); i++) {
                if(tree.get(i).getVertice() == first_vertice_id) {
                    p = i;
                    break;
                }
            }
        }
        int v = tree.get(p).getVertice();
        if (log_enabled) {
            m.addLog(m.getTekstKonsoli(), "-> Wybrano wierzchołek: ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(v));
            m.addLog(m.getTekstKonsoli(), "\n");
        }

        if (log_enabled) {
            tree.get(p).print_neighbours_log(m);
        }
        stack.add(v);
        if (log_enabled) {
            print_int_list(stack, "S", m);
            print_int_list(Q, "Q", m);
        }
        while (stack.size() > 0){
            if(tree.get(p).getNeighbours().size() > 0){
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "Istnieje krawędź wychodząca z wierzchołka ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(v));
                    m.addLog(m.getTekstKonsoli(), "\n");
                }
                int neighbour = tree.get(p).getNeighbours().get(0);
                if (log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "Wybrano krawędź: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(v));
                    m.addLog(m.getTekstKonsoli(), "-");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(neighbour));
                    m.addLog(m.getTekstKonsoli(), "\n");
                }
                tree = remove_edge(tree, p, v, neighbour);

                int last_element = tree.size() - 1;
                int j = tree.get(last_element).getVertice(); //pointer na sąsiada
                tree.remove(last_element); //usuwamy ostatni element
                                           //utworzony sztucznie by zwrócić pointer sąsiada
                p = j;
                v = tree.get(p).getVertice();
                stack.add(v);
                if (log_enabled) {
                    print_int_list(stack, "S", m);
                    print_int_list(Q, "Q", m);
                    m.addLog(m.getTekstKonsoli(), "\n");
                    m.addLog(m.getTekstKonsoli(), "-> Wybrano wierzchołek: ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(v));
                    m.addLog(m.getTekstKonsoli(), "\n");
                    tree.get(p).print_neighbours_log(m);
                }
            } else {
                if(log_enabled) {
                    m.addLog(m.getTekstKonsoli(), "Nie istnieje krawędź wychodząca z wierzchołka ");
                    m.addLog(m.getTekstKonsoli(), String.valueOf(v));
                    m.addLog(m.getTekstKonsoli(), "\n");
                }
                for(int i = stack.size() - 1; i >= 0; i--) {
                    if(stack.get(i) == v) {
                        stack.remove(i);
                        break;
                    }
                }
                Q.add(v);
                if(log_enabled) {
                    print_int_list(stack, "S", m);
                    print_int_list(Q, "Q", m);
                }
                if(stack.size() > 0) {
                    v = stack.get(stack.size() - 1);
                    for (int k = 0; k < tree.size(); k++) {
                        if (tree.get(k).getVertice() == v) {
                            p = k;
                        }
                    }
                }
            }
        }
        return Q;
    }
    
    public static List<Adjacency_list_unit> convert_to_adjacency_list(Graph tree, boolean log_enabled, MenuView m) {
        List<Adjacency_list_unit> adjacency_list = new ArrayList<Adjacency_list_unit>();
        for(int i = 0; i < tree.getVertices().size(); i++) {
            int v = tree.getVertices().get(i);
            Adjacency_list_unit x = new Adjacency_list_unit(v);
            for(int j = 0; j < tree.getEdges().size(); j++) {
                Edge e = tree.getEdges().get(j);
                if(e.getBeginning() == v) {
                    x.Add_neighbour(e.getEnd());
                } else if(e.getEnd() == v) {
                    x.Add_neighbour(e.getBeginning());
                }
            }
            adjacency_list.add(x);
        }
        if(log_enabled) {
            print_adjacency_list(adjacency_list, m);
        }
        return adjacency_list;
    }
    
    public static List<Adjacency_list_unit> remove_edge(List<Adjacency_list_unit> tree,
            int pointer, int point_a, int point_b) {
        boolean quit_loop = false;
        tree.get(pointer).getNeighbours().remove(0);
        for(int i = 0; i < tree.size(); i++) {
            if(tree.get(i).getVertice() == point_b) {
                for(int j = 0; j < tree.get(i).getNeighbours().size(); j++) {
                    if(tree.get(i).getNeighbours().get(j) == point_a) {
                        tree.get(i).getNeighbours().remove(j);
                        tree.add(new Adjacency_list_unit(i));//potrzebny 'i' pointer
                        quit_loop = true;
                        break;
                    }
                }
                if(quit_loop){
                    break;
                }
            }
        }
        return tree;
    }
    
    public static void print_adjacency_list(List<Adjacency_list_unit> adjacency_list, MenuView m) {
        for(int i = 0; i < adjacency_list.size(); i++) {
            adjacency_list.get(i).print_neighbours_log(m);
        }
    }
    
    public static void print_int_list(List<Integer> list, String name, MenuView m) {
        m.addLog(m.getTekstKonsoli(), name);
        m.addLog(m.getTekstKonsoli(), ": ");
        for (int i = 0; i < list.size(); i++) {
            m.addLog(m.getTekstKonsoli(), list.get(i).toString());
            if (i + 1 < list.size()) {
                m.addLog(m.getTekstKonsoli(), ", ");
            }
        }
        m.addLog(m.getTekstKonsoli(), "\n");
    }
}
