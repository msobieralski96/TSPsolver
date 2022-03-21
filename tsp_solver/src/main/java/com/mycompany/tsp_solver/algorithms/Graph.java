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
public class Graph {

    /**
     * @return the vertices
     */
    public List<Integer> getVertices() {
        return vertices;
    }

    /**
     * @return the vertices_data
     */
    public List<Vertice> getVertices_data() {
        return vertices_data;
    }

    /**
     * @param vertices_data the vertices_data to set
     */
    public void setVertices_data(List<Vertice> vertices_data) {
        this.vertices_data = vertices_data;
    }
    
    /**
     * @return the edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set
     */
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
    
    private List<Integer> vertices = new ArrayList<Integer>();
    private List<Vertice> vertices_data = new ArrayList<Vertice>();
    private List<Edge> edges = new ArrayList<Edge>();
    
    public Graph(List<Integer> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }
    
    public Graph(List<Integer> vertices) {
        this.vertices = vertices;
    }
    
    public void print_tree() {
        System.out.println("Drzewo:");
        System.out.print("\tWierzchołki: ");
        for(int i = 0; i < vertices.size(); i++) {
            System.out.print(vertices.get(i));
            if (i + 1 != vertices.size()) {
                System.out.print(", ");
            }
        }
        System.out.print("\n\tKrawędzie: ");
        for(int i = 0; i < edges.size(); i++) {
            System.out.print("{(");
            System.out.print(edges.get(i).getBeginning());
            System.out.print(", ");
            System.out.print(edges.get(i).getEnd());
            System.out.print("): ");
            System.out.print(edges.get(i).getWeight());
            System.out.print("}");
            if (i + 1 != edges.size()) {
                System.out.print(", ");
            }
        }
        System.out.print("\n");
    }
    
    public void print_tree_log(MenuView m) {
        m.addLog(m.getTekstKonsoli(), "Drzewo:\n");
        m.addLog(m.getTekstKonsoli(), "\tWierzchołki: ");
        for(int i = 0; i < vertices.size(); i++) {
            m.addLog(m.getTekstKonsoli(), vertices.get(i).toString());
            if (i + 1 != vertices.size()) {
                m.addLog(m.getTekstKonsoli(), ", ");
            }
        }
        m.addLog(m.getTekstKonsoli(), "\n\tKrawędzie: ");
        for(int i = 0; i < edges.size(); i++) {
            m.addLog(m.getTekstKonsoli(), "{(");
            m.addLog(m.getTekstKonsoli(), String.valueOf(edges.get(i).getBeginning()));
            m.addLog(m.getTekstKonsoli(), ", ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(edges.get(i).getEnd()));
            m.addLog(m.getTekstKonsoli(), "): ");
            m.addLog(m.getTekstKonsoli(), String.valueOf(edges.get(i).getWeight()));
            m.addLog(m.getTekstKonsoli(), "}");
            if (i + 1 != edges.size()) {
                m.addLog(m.getTekstKonsoli(), ", ");
            }
        }
        m.addLog(m.getTekstKonsoli(), "\n");
    }
}
