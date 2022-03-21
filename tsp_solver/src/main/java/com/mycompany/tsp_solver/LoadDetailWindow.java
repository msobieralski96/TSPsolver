/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class LoadDetailWindow extends JFrame {

    public LoadDetailWindow() {
        super("Load data details");
        setResizable(false);
        setLocation(60, 60);
        setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);

        JLabel text_1 = new JLabel("1) wierzchołki:");
        panel.add(text_1);
        panel.add(Box.createVerticalStrut(8));

        JLabel text_2 = new JLabel("//x lines");
        JLabel text_3 = new JLabel("x    y    name [optional]");
        panel.add(text_2);
        panel.add(text_3);
        panel.add(Box.createVerticalStrut(15));

        JLabel text_4 = new JLabel("2) wierzchołki + krawędzie:          ");
        panel.add(text_4);
        panel.add(Box.createVerticalStrut(8));

        JLabel text_5 = new JLabel("n    m");
        JLabel text_6 = new JLabel("//next n lines");
        JLabel text_7 = new JLabel("id   x    y    name [optional]");
        JLabel text_8 = new JLabel("//next m lines");
        JLabel text_9 = new JLabel("id   id2  distance [optional]");
        panel.add(text_5);
        panel.add(text_6);
        panel.add(text_7);
        panel.add(text_8);
        panel.add(text_9);
        panel.add(Box.createVerticalStrut(8));

        JButton load_detail_button = new JButton("OK");
        panel.add(load_detail_button);
        load_detail_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
