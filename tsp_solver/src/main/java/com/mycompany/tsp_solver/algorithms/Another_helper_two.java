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
public class Another_helper_two {

    /**
     * @return the path
     */
    public List<Integer> getPath() {
        return path;
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
    
    private List<Integer> path;
    private WTM_graph_object wtm_graph_object;
    private long start;
    private long stop;
    
    public Another_helper_two(List<Integer> path,
            WTM_graph_object wtm_graph_object,
            long start,
            long stop) {
        this.path = path;
        this.wtm_graph_object = wtm_graph_object;
        this.start = start;
        this.stop = stop;
    }
}
