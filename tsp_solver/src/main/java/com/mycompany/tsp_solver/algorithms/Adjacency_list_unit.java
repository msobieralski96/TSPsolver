/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver.algorithms;

import com.mycompany.tsp_solver.MenuView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mic45
 */
public class Adjacency_list_unit {

    /**
     * @return the vertice
     */
    public int getVertice() {
        return vertice;
    }

    /**
     * @return the neighboors
     */
    public List<Integer> getNeighbours() {
        return neighbours;
    }
    
    private int vertice;
    private List<Integer> neighbours;
    
    public Adjacency_list_unit(int vertice) {
        this.vertice = vertice;
        this.neighbours = new ArrayList<Integer>();
    }
    
    public void Add_neighbour(int vertice) {
        this.getNeighbours().add(vertice);
    }
    
    public void print_neighbours() {
        System.out.print("Wierzchołek: ");
        System.out.print(this.getVertice());
        System.out.print("; sąsiedzi: ");
        for(int i = 0; i < this.getNeighbours().size(); i++){
            System.out.print(this.getNeighbours().get(i));
            if(i+1 < this.getNeighbours().size()){
                System.out.print(", ");
            }
        }
        System.out.print("\n");
    }
    
    public void print_neighbours_log(MenuView m) {
        m.addLog(m.getTekstKonsoli(), "Wierzchołek: ");
        m.addLog(m.getTekstKonsoli(), String.valueOf(this.getVertice()));
        m.addLog(m.getTekstKonsoli(), "; sąsiedzi: ");
        for(int i = 0; i < this.getNeighbours().size(); i++){
            m.addLog(m.getTekstKonsoli(), this.getNeighbours().get(i).toString());
            if(i+1 < this.getNeighbours().size()){
                m.addLog(m.getTekstKonsoli(), ", ");
            }
        }
        m.addLog(m.getTekstKonsoli(), "\n");
    }
}
