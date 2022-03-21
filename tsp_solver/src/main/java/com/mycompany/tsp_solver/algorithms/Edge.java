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
public class Edge implements Comparable<Edge>{

    /**
     * @return the beginning
     */
    public int getBeginning() {
        return beginning;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }
    
    private int beginning;
    private int end;
    private double weight;
    
    
    public Edge(int beginning, int end, double weight) {
        this.beginning = beginning;
        this.end = end;
        this.weight = weight;
    }
    
    @Override
    public int compareTo(Edge e) {
        Double own_weight = this.weight;
        Double its_weight = e.weight;
        return own_weight.compareTo(its_weight);
    }
}
