/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mic45
 */
public class Shortest_path {

    /**
     * @return the vertices
     */
    public List<Integer> getVertices() {
        return vertices;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return the wtm_graph_object
     */
    public WTM_graph_object getWtm_graph_object() {
        return wtm_graph_object;
    }
    
    /**
     * @return the start
     */
    public long getStart() {
        return start;
    }
    
    /**
     * @return the stop
     */
    public long getStop() {
        return stop;
    }

    private List<Integer> vertices = new ArrayList<Integer>();
    private double cost;
    private WTM_graph_object wtm_graph_object;
    private long start;
    private long stop;
    
    public Shortest_path(List<Integer> vertices,
            double cost,
            WTM_graph_object wtm_graph_object,
            long start,
            long stop) {
        this.vertices = vertices;
        this.cost = cost;
        this.wtm_graph_object = wtm_graph_object;
        this.start = start;
        this.stop = stop;
    }
    
    public Shortest_path(double cost) {
        this.cost = cost;
    }
}
