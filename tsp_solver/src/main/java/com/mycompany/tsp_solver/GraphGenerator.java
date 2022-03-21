/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Brute_force;
import com.mycompany.tsp_solver.algorithms.Dijkstra_object;
import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import com.mycompany.tsp_solver.algorithms.Vertice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mic45
 */
public class GraphGenerator {
    
    static int min_x = 0;
    static int min_y = 0;
    static int max_x = 10000;
    static int max_y = 10000;
    
    static Graph generate_full_graph(int vertices_amount) {
        List<Integer> vertices = new ArrayList<Integer>();
        List<Vertice> vertice_data = new ArrayList<Vertice>();
        List<Edge> edges = new ArrayList<Edge>();
        Random rand = new Random();
        
        for(int i = 0; i < vertices_amount; i++) {
            double x = min_x + (max_x - min_x) * rand.nextDouble();
            double y = min_y + (max_y - min_y) * rand.nextDouble();
            vertices.add(i);
            vertice_data.add(new Vertice(i, x, y, String.valueOf(i))); 
        }
        
        for(int i = 0; i < vertices_amount; i++) {
            for(int j = i + 1; j < vertices_amount; j++) {
                double weight = Reader.calculate_distance(vertice_data.get(i).getX(),
                                                          vertice_data.get(j).getX(),
                                                          vertice_data.get(i).getY(),
                                                          vertice_data.get(j).getY());
                edges.add(new Edge(i, j, weight));
            }
        }
        
        Graph graph = new Graph(vertices, edges);
        graph.setVertices_data(vertice_data);
        return graph;
    }
    
    static Graph generate_graph(int vertices_amount, int edges_amount) {
        List<Integer> vertices = new ArrayList<Integer>();
        List<Vertice> vertice_data = new ArrayList<Vertice>();
        List<Edge> edges = new ArrayList<Edge>();
        Random rand = new Random();
        
        //dodawanie wierzchołków
        for(int i = 0; i < vertices_amount; i++) {
            double x = min_x + (max_x - min_x) * rand.nextDouble();
            double y = min_y + (max_y - min_y) * rand.nextDouble();
            vertices.add(i);
            vertice_data.add(new Vertice(i, x, y, String.valueOf(i))); 
        }
        
        List<Double> edges_between = new ArrayList<Double>();
        List<Double> edges_between_copy = new ArrayList<Double>();
        int all_possible_edges_amount = (vertices_amount * (vertices_amount - 1)) / 2;
        
        //ustalanie, które krawędzie zostaną dodane, a które nie
        for(int i = 0; i < all_possible_edges_amount; i++) {
            double chance = 0 + (100000 - 0) * rand.nextDouble();
            edges_between.add(chance);
            edges_between_copy.add(chance);
        }
        Collections.sort(edges_between_copy);
        Collections.reverse(edges_between_copy);
        double border_value = edges_between_copy.get(edges_amount - 1);
        
        //dodawanie krawędzi dla par wierzchołków, dla których border_value <= przypisanej wartości
        int counter = 0;
        for(int i = 0; i < vertices_amount; i++) {
            for(int j = i + 1; j < vertices_amount; j++) {
                if(edges_between.get(counter) >= border_value) {
                    double weight = Reader.calculate_distance(vertice_data.get(i).getX(),
                                                              vertice_data.get(j).getX(),
                                                              vertice_data.get(i).getY(),
                                                              vertice_data.get(j).getY());
                    edges.add(new Edge(i, j, weight));
                }
                counter++;
            }
        }
        
        //utworzenie grafu
        Graph graph = new Graph(vertices, edges);
        graph.setVertices_data(vertice_data);
        
        //sprawdzanie, czy graf jest spójny
        if(edges_amount + 1 >= vertices_amount) {
            List<Integer> how_many_neighbours = count_vertices_neighbours(vertices_amount, edges);

            while (true) {
                Double[][] data = Reader.convert_to_adjacency_matrix(graph);
                int size = data.length;
                double[][] _data = new double[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        _data[i][j] = data[i][j];
                    }
                }
                
                List<Graph> forest = new ArrayList<Graph>();
                forest.add(new Graph(new ArrayList<Integer>(), new ArrayList<Edge>()));
                forest.get(0).getVertices().add(0);
                
                List<Integer> vertices_not_in_forest = new ArrayList<Integer>();
                
                if (Brute_force.is_graph_connected(data) == false) {
                    Dijkstra_object dij_data = Brute_force.Dijkstra(0, _data);
                    double[] d = dij_data.getD();
                    int[] p = dij_data.getP();

                    for (int i = 1; i < d.length; i++) {
                        if(d[i] == 1000000) {
                            vertices_not_in_forest.add(i);
                        } else {
                            forest.get(0).getVertices().add(i);
                        }
                    }
                } else {
                    break;
                }
                
                counter = 1;
                while(vertices_not_in_forest.size() > 0) {
                    forest.add(new Graph(new ArrayList<Integer>(), new ArrayList<Edge>()));
                    int checked = vertices_not_in_forest.get(vertices_not_in_forest.size() - 1);
                    forest.get(counter).getVertices().add(checked);
                    vertices_not_in_forest.remove(vertices_not_in_forest.size() - 1);
                    
                    Dijkstra_object dij_data = Brute_force.Dijkstra(checked, _data);
                    double[] d = dij_data.getD();
                    int[] p = dij_data.getP();
                    
                    for (int i = vertices_not_in_forest.size() - 1; i >= 0; i--) {
                        if(i != checked) {
                            if(d[vertices_not_in_forest.get(i)] != 1000000) {
                                forest.get(counter).getVertices().add(vertices_not_in_forest.get(i));
                                vertices_not_in_forest.remove(i);
                            }
                        }
                    }
                    counter++;
                }
                
                List<Edge> new_edges = new ArrayList<Edge>();
                
                for(int i = 0; i < forest.size() - 1; i++) {
                    int vertice1 = forest.get(i).getVertices().get(0);
                    int vertice2 = forest.get(i + 1).getVertices().get(0);
                    double weight = Reader.calculate_distance(vertice_data.get(vertice1).getX(),
                            vertice_data.get(vertice2).getX(),
                            vertice_data.get(vertice1).getY(),
                            vertice_data.get(vertice2).getY());
                    edges.add(new Edge(vertice1, vertice2, weight));
                    new_edges.add(new Edge(vertice1, vertice2, weight));
                }

                for(int i = 0; i < forest.size() - 1; i++) {
                    how_many_neighbours = count_vertices_neighbours(vertices_amount, edges);

                    for(int j = 0; j < edges.size(); j++) {
                        Edge e = edges.get(j);
                        int vertice_1 = e.getBeginning();
                        int vertice_2 = e.getEnd();
                        boolean edge_to_remove = true;
                        for(int k = 0; k < new_edges.size(); k++) {
                            Edge ne = new_edges.get(k);
                            int ne1 = ne.getBeginning();
                            int ne2 = ne.getEnd();
                            if ((vertice_1 == ne1 && vertice_2 == ne2)
                                    || (vertice_1 == ne2 && vertice_2 == ne1)) {
                                edge_to_remove = false;
                                break;
                            }
                        }
                        if (edge_to_remove) {
                            if (how_many_neighbours.get(vertice_1) > 1
                                    && how_many_neighbours.get(vertice_2) > 1) {
                                edges.remove(j);
                                break;
                            }
                        }
                    }
                }
                
                graph.setEdges(edges);
            }
        }
        return graph;
    }
    
    static List<Integer> count_vertices_neighbours(int vertices_amount, List<Edge> edges) {
        List<Integer> how_many_neighbours = new ArrayList<Integer>();
        
        for(int i = 0; i < vertices_amount; i++) {
            how_many_neighbours.add(0);
        }
        for(int i = 0; i < edges.size(); i++) {
            int start = edges.get(i).getBeginning();
            int end = edges.get(i).getEnd();
            how_many_neighbours.set(start, how_many_neighbours.get(start) + 1);
            how_many_neighbours.set(end, how_many_neighbours.get(end) + 1);
        }
        
        return how_many_neighbours;
    }
    
    static List<Integer> vertices_with_most_neighbours(List<Integer> how_many_neighbours) {
        int winner = 0;
        int second_winner = 0;
        int max_value = how_many_neighbours.get(0);
        int second_max_value = 0;
        
        for(int i = 1; i < how_many_neighbours.size(); i++) {
            if (how_many_neighbours.get(i) > max_value) {
                second_winner = winner;
                second_max_value = max_value;
                winner = i;
                max_value = how_many_neighbours.get(i);
            } else if (how_many_neighbours.get(i) > second_max_value) {
                second_winner = i;
                second_max_value = how_many_neighbours.get(i);
            }
        }
        List<Integer> winners = new ArrayList<Integer>();
        winners.add(winner);
        winners.add(second_winner);
        return winners;
    }
}
