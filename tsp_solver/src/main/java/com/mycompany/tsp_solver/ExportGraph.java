/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import com.mycompany.tsp_solver.algorithms.Vertice;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mic45
 */
public class ExportGraph {
    
    static void export_graph(Graph g, String path) {
        List<String> lines = new ArrayList<String>();
        int vertices_amount = g.getVertices().size();
        int edges_amount = g.getEdges().size();
        String line = String.valueOf(vertices_amount) + " " + String.valueOf(edges_amount) + "\n";
        lines.add(line);
        for(int i = 0; i < vertices_amount; i++) {
            Vertice vertice = g.getVertices_data().get(i);
            int id = vertice.getId();
            double x = vertice.getX();
            double y = vertice.getY();

            line = String.valueOf(id) + " " + String.valueOf(x) + " " + String.valueOf(y);
            if(vertice.getName() != null && vertice.getName().length() > 0) {
                line = line + " " + vertice.getName();
            }
            line = line + "\n";
            lines.add(line);
        }
        for(int i = 0; i < edges_amount; i++) {
            Edge edge = g.getEdges().get(i);
            int id = edge.getBeginning();
            int id2 = edge.getEnd();
            double weight = edge.getWeight();

            line = String.valueOf(id) + " " + String.valueOf(id2) + " " + String.valueOf(weight) + "\n";
            lines.add(line);
        }
        
        try (FileWriter writer = new FileWriter(path + ".txt");
                BufferedWriter bw = new BufferedWriter(writer)) {
            for(int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
    
}
