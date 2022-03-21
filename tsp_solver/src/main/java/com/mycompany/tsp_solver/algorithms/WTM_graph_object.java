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
public class WTM_graph_object {

    /**
     * @return the wtm_graphs
     */
    public List<Graph> getWtm_graphs() {
        return wtm_graphs;
    }

    /**
     * @param wtm_graphs the wtm_graphs to set
     */
    public void setWtm_graphs(List<Graph> wtm_graphs) {
        this.wtm_graphs = wtm_graphs;
    }

    /**
     * @return the explanations
     */
    public List<String> getExplanations() {
        return explanations;
    }

    /**
     * @param explanations the explanations to set
     */
    public void setExplanations(List<String> explanations) {
        this.explanations = explanations;
    }

    /**
     * @return the is_final_path
     */
    public List<Boolean> getIs_final_path() {
        return is_final_path;
    }

    /**
     * @param is_final_path the is_final_path to set
     */
    public void setIs_final_path(List<Boolean> is_final_path) {
        this.is_final_path = is_final_path;
    }

    /**
     * @return the final_path_list
     */
    public List<List<Integer>> getFinal_path_list() {
        return final_path_list;
    }

    /**
     * @param final_path_list the final_path_list to set
     */
    public void setFinal_path_list(List<List<Integer>> final_path_list) {
        this.final_path_list = final_path_list;
    }
    
    private List<Graph> wtm_graphs;
    private List<String> explanations;
    private List<Boolean> is_final_path;
    private List<List<Integer>> final_path_list;
    
    public WTM_graph_object(List<Graph> wtm_graphs,
            List<String> explanations,
            List<Boolean> is_final_path,
            List<List<Integer>> final_path_list) {
        this.wtm_graphs = wtm_graphs;
        this.explanations = explanations;
        this.is_final_path = is_final_path;
        this.final_path_list = final_path_list;
    }
}
