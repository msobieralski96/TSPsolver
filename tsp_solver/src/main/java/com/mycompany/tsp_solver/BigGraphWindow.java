/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Graph;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mic45
 */
public class BigGraphWindow extends JFrame implements ActionListener{
    
    GraphBuilder graph;
    JPanel graphPanel;

    List<Graph> wtm_graphs;
    List<String> explanations;
    List<Boolean> is_final_path;
    List<List<Integer>> final_path_list;
    JButton prev_epoch;
    JLabel wtm_epoch;
    JButton next_epoch;
    JLabel shown_explanation;
    int collection_size;
    int current_epoch;
    
    JPanel score_panel;
    JPanel header_panel;
    JLabel score_text;
    JPanel track_panel;
    JLabel track_text;
    JPanel print_track_panel;
    JLabel print_track_text;
    JPanel cost_panel;
    JLabel cost_text;
    JLabel print_cost_text;
    JPanel time1_panel;
    JLabel time1_text;
    JLabel print_time1_text;
    JPanel time2_panel;
    JLabel time2_text;
    JLabel print_time2_text;
    JPanel time3_panel;
    JLabel time3_text;
    JLabel print_time3_text;
    JPanel time4_panel;
    JLabel time4_text;
    JLabel print_time4_text;
    JPanel time5_panel;
    JLabel time5_text;
    JLabel print_time5_text;

    public BigGraphWindow(String algorithm_name,
            GraphBuilder graph,
            List<Graph> wtm_graphs,
            List<String> explanations,
            int collection_size,
            List<Boolean> is_final_path,
            List<List<Integer>> final_path_list,
            String path_text,
            String path_cost_text,
            String time1_txt,
            String time2_txt,
            String time3_txt,
            String time4_txt,
            String time5_txt) {
        super("Wizualizacja: " + algorithm_name);
        this.graph = graph;
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(1150, 730));
        setResizable(false);
        setLocation(0, 0);
        
        graphPanel = new JPanel(new BorderLayout());
        
        JPanel left_margin = new JPanel();
        JPanel right_margin = new JPanel();
        JPanel up_margin = new JPanel();
        JPanel down_margin = new JPanel();
        graphPanel.add(left_margin, BorderLayout.WEST);
        left_margin.setPreferredSize(new Dimension(20, 580));
        graphPanel.add(right_margin, BorderLayout.EAST);
        right_margin.setPreferredSize(new Dimension(20, 580));
        graphPanel.add(up_margin, BorderLayout.NORTH);
        up_margin.setPreferredSize(new Dimension(580, 20));
        graphPanel.add(down_margin, BorderLayout.SOUTH);
        down_margin.setPreferredSize(new Dimension(580, 20));
        
        graphPanel.add(this.graph, BorderLayout.CENTER);
        
        JPanel scroll_panel = new JPanel();
        scroll_panel.setLayout(new BoxLayout(scroll_panel, BoxLayout.X_AXIS));
        scroll_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        prev_epoch = new JButton("<");
        prev_epoch.addActionListener(this);
        scroll_panel.add(prev_epoch);
        scroll_panel.add(new JLabel("   "));
        
        wtm_epoch = new JLabel("xx / xx");
        scroll_panel.add(wtm_epoch);
        scroll_panel.add(new JLabel("   "));
        
        next_epoch = new JButton(">");
        next_epoch.addActionListener(this);
        scroll_panel.add(next_epoch);

        JPanel explanation_panel = new JPanel();
        explanation_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        explanation_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        shown_explanation = new JLabel("");
        explanation_panel.add(shown_explanation);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setSize(new Dimension(700, 700));
        
        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.PAGE_AXIS));
        secondaryPanel.setSize(new Dimension(400, 400));
        
        mainPanel.add(graphPanel);
        mainPanel.add(scroll_panel);
        mainPanel.add(explanation_panel);
        
        init_scoreboard(path_text, path_cost_text, time1_txt, time2_txt, time3_txt, time4_txt, time5_txt);
        secondaryPanel.add(score_panel);
        
        add(mainPanel);
        add(secondaryPanel);

        set_WTM_panel(wtm_graphs, explanations, collection_size, is_final_path, final_path_list);
        
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prev_epoch) {
            this.current_epoch -= 1;
            if (this.current_epoch < 0) {
                this.current_epoch += this.collection_size;
            }
            wtm_epoch.setText(String.valueOf(this.current_epoch + 1)
                    + " / " + String.valueOf(this.collection_size));
            if(is_final_path.get(this.current_epoch) == true) {
                graph.add_answer(wtm_graphs.get(this.current_epoch),
                        this.final_path_list.get(this.current_epoch));
                shown_explanation.setText(explanations.get(this.current_epoch));
            } else {
                graph.repaint(wtm_graphs.get(this.current_epoch));
                shown_explanation.setText(explanations.get(this.current_epoch));
            }
        } else if (e.getSource() == next_epoch) {
            this.current_epoch += 1;
            if (this.current_epoch >= this.collection_size) {
                this.current_epoch -= this.collection_size;
            }
            wtm_epoch.setText(String.valueOf(this.current_epoch + 1)
                    + " / " + String.valueOf(this.collection_size));
            if(is_final_path.get(this.current_epoch) == true) {
                graph.add_answer(wtm_graphs.get(this.current_epoch),
                        this.final_path_list.get(this.current_epoch));
                shown_explanation.setText(explanations.get(this.current_epoch));
            } else {
                graph.repaint(wtm_graphs.get(this.current_epoch));
                shown_explanation.setText(explanations.get(this.current_epoch));
            }
        }
    }
    
    public void set_WTM_panel(
            List<Graph> wtm_graphs,
            List<String> explanations,
            int collection_size,
            List<Boolean> is_final_path,
            List<List<Integer>> final_path_list) {
        this.is_final_path = is_final_path;
        this.final_path_list = final_path_list;
        
        this.wtm_graphs = wtm_graphs;
        this.explanations = explanations;
        this.collection_size = collection_size;
        this.current_epoch = collection_size - 1;
        wtm_epoch.setText(String.valueOf(this.current_epoch + 1) +
                " / " + String.valueOf(this.collection_size));
        
        if (wtm_graphs != null) {
            if(is_final_path.get(this.current_epoch) == true) {
                graph.add_answer(wtm_graphs.get(this.current_epoch),
                        this.final_path_list.get(this.current_epoch));
            } else {
                graph.repaint(wtm_graphs.get(this.current_epoch));
            }
            
            shown_explanation.setText(explanations.get(this.current_epoch));
            if(this.collection_size > 1) {
                prev_epoch.setEnabled(true);
                next_epoch.setEnabled(true);
            } else {
                prev_epoch.setEnabled(false);
                next_epoch.setEnabled(false);
            }
        } else {
            prev_epoch.setEnabled(false);
            next_epoch.setEnabled(false);
        }
    }
    
    void init_scoreboard(String path_text,
            String path_cost_text,
            String time1_txt,
            String time2_txt,
            String time3_txt,
            String time4_txt,
            String time5_txt) {
        score_panel = new JPanel();
        score_panel.setLayout(new BoxLayout(score_panel, BoxLayout.PAGE_AXIS));
        score_panel.setSize(new Dimension(200, 400));

        header_panel = new JPanel();
        header_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        score_text = new JLabel("Wynik:");
        header_panel.add(score_text);

        track_panel = new JPanel();
        track_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        track_text = new JLabel("Najkrótsza trasa:");
        track_panel.add(track_text);

        print_track_panel = new JPanel();
        print_track_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        print_track_text = new JLabel(path_text);
        print_track_panel.add(print_track_text);

        cost_panel = new JPanel();
        cost_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));       
        cost_text = new JLabel("Koszt trasy:");
        cost_panel.add(cost_text);
        print_cost_text = new JLabel(path_cost_text);
        cost_panel.add(print_cost_text);

        time1_panel = new JPanel();
        time1_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));        
        time1_text = new JLabel("Czas wczytywania danych [s]:");
        time1_panel.add(time1_text);
        print_time1_text = new JLabel(time1_txt);
        time1_panel.add(print_time1_text);

        time2_panel = new JPanel();
        time2_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));        
        time2_text = new JLabel("Czas przygotowania do algorytmu [s]:");
        time2_panel.add(time2_text);
        print_time2_text = new JLabel(time2_txt);
        time2_panel.add(print_time2_text);
        
        time3_panel = new JPanel();
        time3_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));        
        time3_text = new JLabel("Czas realizacji algorytmu [s]:");
        time3_panel.add(time3_text);
        print_time3_text = new JLabel(time3_txt);
        time3_panel.add(print_time3_text);

        time4_panel = new JPanel();
        time4_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));        
        time4_text = new JLabel("Czas czynności końcowych [s]:");
        time4_panel.add(time4_text);
        print_time4_text = new JLabel(time4_txt);
        time4_panel.add(print_time4_text);
        
        time5_panel = new JPanel();
        time5_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));        
        time5_text = new JLabel("Łączny czas algorytmu [s]:");
        time5_panel.add(time5_text);
        print_time5_text = new JLabel(time5_txt);
        time5_panel.add(print_time5_text);

        score_panel.add(header_panel);
        score_panel.add(Box.createVerticalStrut(8));
        score_panel.add(track_panel);
        score_panel.add(Box.createVerticalStrut(4));
        score_panel.add(print_track_panel);
        score_panel.add(cost_panel);
        score_panel.add(time1_panel);
        score_panel.add(time2_panel);
        score_panel.add(time3_panel);
        score_panel.add(time4_panel);
        score_panel.add(time5_panel);
    }
}
