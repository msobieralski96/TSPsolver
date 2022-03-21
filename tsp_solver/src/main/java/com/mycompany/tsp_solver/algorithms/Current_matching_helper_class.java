/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import java.util.List;

/**
 *
 * @author mic45
 */
public class Current_matching_helper_class {

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return the vertices_of_perfect_matching
     */
    public List<Integer> getVertices_of_perfect_matching() {
        return vertices_of_perfect_matching;
    }

    /**
     * @return the edges_of_perfect_matching
     */
    public List<Edge> getEdges_of_perfect_matching() {
        return edges_of_perfect_matching;
    }

    /**
     * @return the boundary_cost
     */
    public double getBoundary_cost() {
        return boundary_cost;
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
    
    private boolean success;
    private List<Integer> vertices_of_perfect_matching;
    private List<Edge> edges_of_perfect_matching;
    private double current_cost;
    private double boundary_cost;
    private WTM_graph_object wtm_graph_object;
    private long start;
    private long stop;
    
    public Current_matching_helper_class(boolean success,
            List<Integer> vertices_of_perfect_matching,
            List<Edge> edges_of_perfect_matching,
            double current_cost,
            double boundary_cost,
            WTM_graph_object wtm_graph_object,
            long start,
            long stop) {
        this.success = success;
        this.vertices_of_perfect_matching = vertices_of_perfect_matching;
        this.edges_of_perfect_matching = edges_of_perfect_matching;
        this.current_cost = current_cost;
        this.boundary_cost = boundary_cost;
        this.wtm_graph_object = wtm_graph_object;
        this.start = start;
        this.stop = stop;
    }
}
