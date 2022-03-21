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
public class Distance_data {

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the full_path
     */
    public List<Integer> getFull_path() {
        return full_path;
    }
    
    private double distance;
    private List<Integer> full_path;
    
    public Distance_data(double distance, List<Integer> full_path){
        this.distance = distance;
        this.full_path = full_path;
    }
}
