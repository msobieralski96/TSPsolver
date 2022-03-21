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
public class Dijkstra_object {

    /**
     * @return the d
     */
    public double[] getD() {
        return d;
    }

    /**
     * @return the p
     */
    public int[] getP() {
        return p;
    }

    private double[] d; //tablica odległości
    private int[] p; //tablica poprzedników
    
    public Dijkstra_object(double[] d, int[] p) {
        this.d = d;
        this.p = p;
    }
}
