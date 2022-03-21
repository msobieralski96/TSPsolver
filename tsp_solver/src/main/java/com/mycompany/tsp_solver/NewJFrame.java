/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mic45
 */
public class NewJFrame extends JFrame{
    
    GraphBuilder graph;
    JPanel graphPanel;
    
    WTM_panel wtm_panel;
    
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame(){
        super("TSP solver");
        
        setPreferredSize(new Dimension(1200, 710));
        setResizable(false);
 	setLocation(0, 0);
        setLayout(new FlowLayout());

        graph = new GraphBuilder(new Graph(new ArrayList<Integer>(), new ArrayList<Edge>()), 300, 300);
        graphPanel = new JPanel(new BorderLayout());
        
        JPanel left_margin = new JPanel();
        JPanel right_margin = new JPanel();
        JPanel up_margin = new JPanel();
        JPanel down_margin = new JPanel();
        graphPanel.add(left_margin, BorderLayout.WEST);
        left_margin.setPreferredSize(new Dimension(20, 350));
        graphPanel.add(right_margin, BorderLayout.EAST);
        right_margin.setPreferredSize(new Dimension(20, 350));
        graphPanel.add(up_margin, BorderLayout.NORTH);
        up_margin.setPreferredSize(new Dimension(400, 20));
        graphPanel.add(down_margin, BorderLayout.SOUTH);
        down_margin.setPreferredSize(new Dimension(400, 20));
        
        graphPanel.add(graph, BorderLayout.CENTER);
        
        wtm_panel = new WTM_panel(graph);
        graphPanel.add(wtm_panel, BorderLayout.SOUTH);
        
        JPanel menu = new MenuView(graph, wtm_panel);

        add(graphPanel);
        add(menu);
        pack();
 	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	setVisible(true);
    }
}
