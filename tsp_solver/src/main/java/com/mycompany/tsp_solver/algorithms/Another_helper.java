/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

/**
 *
 * @author mic45
 */
public class Another_helper {

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
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
    
    private Graph graph;
    private WTM_graph_object wtm_graph_object;
    private long start;
    private long stop;
    
    public Another_helper(Graph graph,
            WTM_graph_object wtm_graph_object,
            long start,
            long stop) {
        this.graph = graph;
        this.wtm_graph_object = wtm_graph_object;
        this.start = start;
        this.stop = stop;
    }
}
