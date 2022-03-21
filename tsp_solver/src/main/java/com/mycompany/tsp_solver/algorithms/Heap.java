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
public class Heap {

    /**
     * @return the id_counter
     */
    public int getId_counter() {
        return id_counter;
    }

    /**
     * @param aId_counter the id_counter to set
     */
    public void setId_counter(int aId_counter) {
        id_counter = aId_counter;
    }

    /**
     * @return the heap
     */
    public List<Edge> getHeap() {
        return heap;
    }

    private int id_counter = 0;
    private List<Edge> heap = new ArrayList<Edge>();
    
    public Heap(){
        this.heap = new ArrayList<Edge>();
    }
    
    public void add_to_heap(Edge node){
        this.getHeap().add(null);
        int node_id = getId_counter();
        this.setId_counter(this.getId_counter() + 1);
        int parent_id = (node_id - 1) / 2;
        while ((node_id > 0) && (this.getHeap().get(parent_id).getWeight() > node.getWeight())) {
            this.getHeap().set(node_id, this.getHeap().get(parent_id));
            node_id = parent_id;
            parent_id = (node_id - 1) / 2;
        }
        this.getHeap().set(node_id, node);
    }
    
    public Edge pop_from_heap(){
        if(this.getId_counter() == 0){
            return null;
        }
        Edge root = this.getHeap().get(0);
        this.setId_counter(this.getId_counter() - 1);
        Edge node = this.getHeap().get(getId_counter());
        int node_id = 0;
        int left_son_id = 1;
        while(left_son_id < getId_counter()){
            if ((left_son_id + 1 < getId_counter()) &&
                    (this.getHeap().get(left_son_id + 1).getWeight() <
                    this.getHeap().get(left_son_id).getWeight())){
                left_son_id++;
            }
            if (node.getWeight() <= this.getHeap().get(left_son_id).getWeight()){
                break;
            }
            this.getHeap().set(node_id, this.getHeap().get(left_son_id));
            node_id = left_son_id;
            left_son_id = (2 * left_son_id) + 1;
        }
        this.getHeap().set(node_id, node);
        return root;
    }
    
    public void print_heap(){
        for(int i = 0; i < getId_counter(); i++){
            String info = "Węzeł ";
            info += String.valueOf(i);
            info += ": Krawędź (";
            info += this.getHeap().get(i).getBeginning();
            info += ", ";
            info += this.getHeap().get(i).getEnd();
            info += ") o wadze ";
            info += this.getHeap().get(i).getWeight();
            info += ".";
            System.out.println(info);
        }
    }
    
    public void print_heap_log(MenuView m){
        m.addLog(m.getTekstKonsoli(), "KOPIEC:\n");
        for(int i = 0; i < getId_counter(); i++){
            String info = "Węzeł ";
            info += String.valueOf(i);
            info += ": Krawędź (";
            info += this.getHeap().get(i).getBeginning();
            info += ", ";
            info += this.getHeap().get(i).getEnd();
            info += ") o wadze ";
            info += this.getHeap().get(i).getWeight();
            info += ".\n";
            m.addLog(m.getTekstKonsoli(), info);
        }
    }
}
