/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mycompany.tsp_solver.algorithms.Brute_force;
import com.mycompany.tsp_solver.algorithms.Christofides;
import com.mycompany.tsp_solver.algorithms.Distance_data;
import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import com.mycompany.tsp_solver.algorithms.Nearest_neighbour;
import com.mycompany.tsp_solver.algorithms.Two_approximation;
import com.mycompany.tsp_solver.algorithms.Vertice;
import com.mycompany.tsp_solver.algorithms.WTM;
import com.mycompany.tsp_solver.algorithms.WTM_graph_object;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author mic45
 */
public class MenuView extends JPanel implements ActionListener{

    /**
     * @return the wtm_panel
     */
    public WTM_panel getWtm_panel() {
        return wtm_panel;
    }

    /**
     * @param path1 the path1 to set
     */
    public void setPath1(List<Integer> path1) {
        this.path1 = path1;
    }

    /**
     * @param path2 the path2 to set
     */
    public void setPath2(List<Integer> path2) {
        this.path2 = path2;
    }

    /**
     * @param is_path2 the is_path2 to set
     */
    public void setIs_path2(boolean is_path2) {
        this.is_path2 = is_path2;
    }

    /**
     * @return the tekstKonsoli
     */
    public JTextArea getTekstKonsoli() {
        return tekstKonsoli;
    }
    
    protected final Logger log = Logger.getLogger(getClass().getName());
    Graph g;
    boolean log_enabled;
    
    private List<Integer> path1;
    private List<Integer> path2;
    private boolean is_path2;
    
    JPanel options;
    
    JLabel text1;
    JButton load_vertices_n_edges_button;
    JButton load_vertices_button;
    JButton load_helper;
    JButton export_graph_button;
    
    JLabel text1b;
    JTextField vertices_amount;
    JTextField edges_amount;
    JButton generate_full_graph_button;
    JButton generate_graph_button;
    
    boolean first_vertice_mode;
    JCheckBox set_first_vertice;
    JComboBox vertices_array;
    
    JLabel text2;
    JCheckBox log_box;
    JButton clear_log_button;
    
    JLabel text3;
    JButton brute_force_button;
    JButton two_approximation_button;
    JButton christofides_button;
    JButton wtm_button;
    JButton nearest_neighbour_button;
    
    JTextField neurons_amount;
    JTextField epochs_amount;
    JTextField learning_rate_factor;
    JTextField decrease_learning_rate_factor;
    JTextField neighborhood_radius;
    JTextField decrease_neighborhood_radius;
    
    JLabel text4;
    JLabel text5;
    JLabel text6;
    JLabel text7;
    JLabel text8;
    JLabel text9;
    JLabel text11;
    
    JPanel contentPane;
    JScrollPane scrollPane;
    private JTextArea tekstKonsoli = new JTextArea("log here\n");
    
    protected GraphBuilder graph;
    private WTM_panel wtm_panel;
    
    public MenuView(GraphBuilder graph, WTM_panel wtm_panel){
        super();
        g = new Graph(new ArrayList<Integer>(), new ArrayList<Edge>());
        this.graph = graph;
        this.wtm_panel = wtm_panel;
        this.wtm_panel.lock_preview();
        
        log_enabled = true;
        first_vertice_mode = false;
        is_path2 = false;
        
        options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.PAGE_AXIS));
        options.setSize(new Dimension(350, 650));
        add(options);
        
        JPanel preload_panel = new JPanel(new GridLayout(1, 1));
        text1 = new JLabel("Wczytaj dane z pliku:");
        text1.setAlignmentX(Component.LEFT_ALIGNMENT);
        preload_panel.add(text1);
        options.add(preload_panel);
        
        load_vertices_n_edges_button = new JButton("wierzchołki + krawędzie");
        load_vertices_n_edges_button.addActionListener(this);
        
        load_vertices_button = new JButton("wierzchołki");
        load_vertices_button.addActionListener(this);
        
        JPanel load_panel = new JPanel();
        load_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        load_panel.add(load_vertices_n_edges_button);
        load_panel.add(load_vertices_button);
        options.add(load_panel);
        
        load_helper = new JButton("format danych pliku");
        load_helper.addActionListener(this);
        
        export_graph_button = new JButton("eksportuj graf");
        export_graph_button.addActionListener(this);
        export_graph_button.setEnabled(false);
        
        JPanel load_helper_panel = new JPanel();
        load_helper_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        load_helper_panel.add(load_helper);
        load_helper_panel.add(export_graph_button);
        options.add(load_helper_panel);
        
        options.add(Box.createVerticalStrut(8));
        text1b = new JLabel("Generuj dane:");
        text1b.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        generate_full_graph_button = new JButton("graf pełny");
        generate_full_graph_button.addActionListener(this);
        
        generate_graph_button = new JButton("graf");
        generate_graph_button.addActionListener(this);
        
        JPanel generate_panel = new JPanel(new GridLayout(1, 1));
        generate_panel.add(text1b);
        generate_panel.add(generate_full_graph_button);
        generate_panel.add(generate_graph_button);
        options.add(generate_panel);
        
        JLabel vertices_amount_text = new JLabel("Liczba wierzchołków:");
        vertices_amount = new JTextField("10", 4);
        JLabel edges_amount_text = new JLabel("Liczba krawędzi:");
        edges_amount = new JTextField("20", 4);
        
        JPanel generate_vertices_and_edges_panel = new JPanel();
        generate_vertices_and_edges_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        generate_vertices_and_edges_panel.add(vertices_amount_text);
        generate_vertices_and_edges_panel.add(vertices_amount);
        generate_vertices_and_edges_panel.add(edges_amount_text);
        generate_vertices_and_edges_panel.add(edges_amount);
        options.add(generate_vertices_and_edges_panel);
        
        JPanel options_1_panel = new JPanel();
        options_1_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        text2 = new JLabel("Opcje:");
        options_1_panel.add(text2);
        options.add(options_1_panel);
        
        JPanel first_vertice_option_panel = new JPanel();
        first_vertice_option_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        
        set_first_vertice = new JCheckBox("Ustaw pierwszy wierzchołek:  ");
        set_first_vertice.setMnemonic(KeyEvent.VK_F);
        set_first_vertice.setSelected(false);  
        set_first_vertice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                first_vertice_mode = e.getStateChange() == 1 ? true : false;
            }
        });
        String[] vertices_for_combobox = {"Najpierw wczytaj graf"};
        vertices_array = new JComboBox(vertices_for_combobox);
        vertices_array.setSelectedIndex(0);
        
        options.add(Box.createVerticalStrut(8));
        first_vertice_option_panel.add(set_first_vertice);
        first_vertice_option_panel.add(vertices_array);
        options.add(first_vertice_option_panel);
        options.add(Box.createVerticalStrut(8));
        
        JPanel log_panel = new JPanel();
        log_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        log_box = new JCheckBox("Włącz szczegółowy log");
        log_box.setMnemonic(KeyEvent.VK_L);
        log_box.setSelected(true);  
        log_box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                log_enabled = e.getStateChange() == 1 ? true : false;
            }
        });
        
        clear_log_button = new JButton("Wyczyść log");
        clear_log_button.addActionListener(this);
        
        log_panel.add(log_box);
        log_panel.add(clear_log_button);
        options.add(log_panel);
        
        JPanel WTM_settings = new JPanel();
        WTM_settings.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        
        JLabel WTM_text1 = new JLabel("Dla WTM:");
        JLabel WTM_text2 = new JLabel("Liczba neuronów:");
        neurons_amount = new JTextField("50", 5);
        JLabel WTM_text3 = new JLabel("Liczba epok:");
        epochs_amount = new JTextField("5", 5);
        JLabel WTM_text4 = new JLabel("Współczynnik szybkości uczenia");
        learning_rate_factor = new JTextField("0.6", 10);
        JLabel WTM_text5 = new JLabel("wartość pomniejszająca współczynnik szybkości uczenia");
        JLabel WTM_text5b = new JLabel("co każdą epokę");
        decrease_learning_rate_factor = new JTextField("0.1", 10);
        JLabel WTM_text6 = new JLabel("promień sąsiedztwa");
        neighborhood_radius = new JTextField("50.0", 10);
        JLabel WTM_text7 = new JLabel("wartość pomniejszająca promień sąsiedztwa");
        JLabel WTM_text7b = new JLabel("co każdą epokę");
        decrease_neighborhood_radius = new JTextField("7.5", 10);
        
        JPanel WTM_label1 = new JPanel();
        WTM_label1.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel WTM_label2 = new JPanel();
        WTM_label2.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        JPanel WTM_label4 = new JPanel();
        WTM_label4.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel WTM_label5 = new JPanel();
        WTM_label5.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel WTM_label5b = new JPanel();
        WTM_label5b.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel WTM_label6 = new JPanel();
        WTM_label6.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        JPanel WTM_label7 = new JPanel();
        WTM_label7.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel WTM_label7b = new JPanel();
        WTM_label7b.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        
        WTM_label1.add(WTM_text1);
        WTM_label2.add(WTM_text2);
        WTM_label2.add(neurons_amount);
        WTM_label2.add(WTM_text3);
        WTM_label2.add(epochs_amount);
        WTM_label4.add(WTM_text4);
        WTM_label4.add(learning_rate_factor);
        WTM_label5.add(WTM_text5);
        WTM_label5b.add(WTM_text5b);
        WTM_label5b.add(decrease_learning_rate_factor);
        WTM_label6.add(WTM_text6);
        WTM_label6.add(neighborhood_radius);
        WTM_label7.add(WTM_text7);
        WTM_label7b.add(WTM_text7b);
        WTM_label7b.add(decrease_neighborhood_radius);
        options.add(WTM_label1);
        options.add(WTM_label2);
        options.add(WTM_label4);
        options.add(Box.createVerticalStrut(8));
        options.add(WTM_label5);
        options.add(WTM_label5b);
        options.add(WTM_label6);
        options.add(WTM_label7);
        options.add(WTM_label7b);
        
        JPanel komi_panel = new JPanel();
        komi_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
        text3 = new JLabel("Rozwiąż problem komiwojażera");
        komi_panel.add(text3);
        options.add(Box.createVerticalStrut(8));
        options.add(komi_panel);
        
        JPanel mode_panel = new JPanel();
        mode_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
        mode_panel.setPreferredSize(new Dimension(options.getWidth(), 110));
        brute_force_button = new JButton("Brute Force");
        brute_force_button.addActionListener(this);
        brute_force_button.setEnabled(false);
        
        two_approximation_button = new JButton("Algorytm 2-aproksymacyjny");
        two_approximation_button.addActionListener(this);
        two_approximation_button.setEnabled(false);
        
        christofides_button = new JButton("Algorytm Christofidesa");
        christofides_button.addActionListener(this);
        christofides_button.setEnabled(false);
        
        nearest_neighbour_button = new JButton("Najbliższego sąsiada");
        nearest_neighbour_button.addActionListener(this);
        nearest_neighbour_button.setEnabled(false);
        
        wtm_button = new JButton("Sieć neuronowa WTM");
        wtm_button.addActionListener(this);
        wtm_button.setEnabled(false);
        
        mode_panel.add(brute_force_button);
        mode_panel.add(two_approximation_button);
        mode_panel.add(christofides_button);
        mode_panel.add(nearest_neighbour_button);
        mode_panel.add(wtm_button);
        options.add(mode_panel);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        add(contentPane);
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        tekstKonsoli.setEditable(false);
        tekstKonsoli.setRows(32);
        scrollPane.setViewportView(tekstKonsoli);
        tekstKonsoli.setColumns(32);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load_vertices_n_edges_button
                || e.getSource() == load_vertices_button) {
            
            lock_buttons();
            String path = null;

            final JFileChooser fc = new JFileChooser();

            int returnVal = fc.showOpenDialog(MenuView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path = file.getAbsolutePath();

                log.log(Level.INFO, "Otwieranie: {0}.\n", path);

                if (e.getSource() == load_vertices_n_edges_button) {
                    MenuView instance = this;
                    
                    List<String> list;
                    g = null;
                    
                    long start;
                    start = System.nanoTime();
                    
                    try {
                        list = Reader.read_lines(path);
                        g = Reader.read_edges_n_vertices(list);
                    } catch (IOException ex) {
                        Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (g != null) {
                                set_array_of_vertices_for_combobox();
                                Double[][] data = Reader.convert_to_adjacency_matrix(g);
                                graph.repaint(g);
                                addLog(getTekstKonsoli(), "Wczytano graf.\n");
                                if(Brute_force.is_graph_connected(data)) {
                                    unlock_buttons();
                                } else {
                                    load_vertices_n_edges_button.setEnabled(true);
                                    load_vertices_button.setEnabled(true);
                                    generate_full_graph_button.setEnabled(true);
                                    generate_graph_button.setEnabled(true);
                                    getWtm_panel().unlock_preview();
                                    addLog(getTekstKonsoli(), "Podany graf jest niespójny.\n");
                                }
                                reset_results();
                                
                                long stop = System.nanoTime();
                                set_time(getWtm_panel().get_time1_label(), start, stop, 0);
                                
                                create_single_preview("graf załadowany z pliku");

                                set_default_wtm_params(g);
                            }
                        }
                    });
                    thread.start();
                } else if (e.getSource() == load_vertices_button) {
                    MenuView instance = this;
                    
                    List<String> list;
                    g = null;
                    
                    long start;
                    start = System.nanoTime();
                    
                    try {
                        list = Reader.read_lines(path);
                        g = Reader.read_vertices(list);
                    } catch (IOException ex) {
                        Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (g != null) {
                                set_array_of_vertices_for_combobox();
                                Double[][] data = Reader.convert_to_adjacency_matrix(g);
                                graph.repaint(g);
                                addLog(getTekstKonsoli(), "Wczytano graf.\n");
                                if(Brute_force.is_graph_connected(data)) {
                                    unlock_buttons();
                                } else {
                                    load_vertices_n_edges_button.setEnabled(true);
                                    load_vertices_button.setEnabled(true);
                                    generate_full_graph_button.setEnabled(true);
                                    generate_graph_button.setEnabled(true);
                                    getWtm_panel().unlock_preview();
                                    addLog(getTekstKonsoli(), "Podany graf jest niespójny.\n");
                                }
                                reset_results();
                                
                                long stop = System.nanoTime();
                                set_time(getWtm_panel().get_time1_label(), start, stop, 0);
                                
                                create_single_preview("graf pełny załadowany z pliku");
                                
                                set_default_wtm_params(g);
                            }
                        }
                    });
                    thread.start();
                }
            } else {
                log.log(Level.INFO, "Otwieranie anulowane przez użytkownika.\n");
                load_vertices_n_edges_button.setEnabled(true);
                load_vertices_button.setEnabled(true);
                generate_full_graph_button.setEnabled(true);
                generate_graph_button.setEnabled(true);
                
            }
        } else if (e.getSource() == generate_full_graph_button) {
            MenuView instance = this;
            lock_buttons();
            
            long start;
            start = System.nanoTime();
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    g = GraphGenerator.generate_full_graph(
                            Integer.valueOf(vertices_amount.getText()));
                    set_array_of_vertices_for_combobox();
                    Double[][] data = Reader.convert_to_adjacency_matrix(g);
                    graph.repaint(g);
                    
                    addLog(getTekstKonsoli(), "Wygenerowano graf.\n");
                    if (Brute_force.is_graph_connected(data)) {
                        unlock_buttons();
                    } else {
                        load_vertices_n_edges_button.setEnabled(true);
                        load_vertices_button.setEnabled(true);
                        generate_full_graph_button.setEnabled(true);
                        generate_graph_button.setEnabled(true);
                        getWtm_panel().unlock_preview();
                        addLog(getTekstKonsoli(), "Podany graf jest niespójny.\n");
                    }
                    reset_results();

                    long stop = System.nanoTime();
                    set_time(getWtm_panel().get_time1_label(), start, stop, 0);

                    create_single_preview("wygenerowany graf pełny");
                    
                    set_default_wtm_params(g);
                }
            });
            thread.start();
            
        } else if (e.getSource() == generate_graph_button) {
            MenuView instance = this;
            lock_buttons();
            
            long start;
            start = System.nanoTime();
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    g = GraphGenerator.generate_graph(
                            Integer.valueOf(vertices_amount.getText()),
                            Integer.valueOf(edges_amount.getText()));
                    set_array_of_vertices_for_combobox();
                    Double[][] data = Reader.convert_to_adjacency_matrix(g);
                    graph.repaint(g);
                    
                    addLog(getTekstKonsoli(), "Wygenerowano graf.\n");
                    if (Brute_force.is_graph_connected(data)) {
                        unlock_buttons();
                    } else {
                        load_vertices_n_edges_button.setEnabled(true);
                        load_vertices_button.setEnabled(true);
                        generate_full_graph_button.setEnabled(true);
                        generate_graph_button.setEnabled(true);
                        getWtm_panel().unlock_preview();
                        addLog(getTekstKonsoli(), "Podany graf jest niespójny.\n");
                    }
                    reset_results();

                    long stop = System.nanoTime();
                    set_time(getWtm_panel().get_time1_label(), start, stop, 0);

                    create_single_preview("wygenerowany graf");
                    
                    set_default_wtm_params(g);
                }
            });
            thread.start();
            
        } else if (e.getSource() == load_helper) {
            new LoadDetailWindow().setVisible(true);
        } else if (e.getSource() == export_graph_button) {
            lock_buttons();
            String path = null;
            
            final JFileChooser fc = new JFileChooser();

            int returnVal = fc.showSaveDialog(MenuView.this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path = file.getAbsolutePath();
                final String path_final = path;

                log.log(Level.INFO, "Zapis: {0}.\n", path);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ExportGraph.export_graph(g, path_final);
                        addLog(getTekstKonsoli(), "Wyeksportowano graf:\n");
                        addLog(getTekstKonsoli(), path_final);
                        addLog(getTekstKonsoli(), ".txt\n");
                        unlock_buttons();
                    }
                });
                thread.start();
            } else {
                log.log(Level.INFO, "Zapis anulowany przez użytkownika.\n");
                load_vertices_n_edges_button.setEnabled(true);
                load_vertices_button.setEnabled(true);
                generate_full_graph_button.setEnabled(true);
                generate_graph_button.setEnabled(true);
                export_graph_button.setEnabled(true);
            }
            
        } else if (e.getSource() == brute_force_button) {

            MenuView instance = this;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lock_buttons();

                    reset_most_results();
                    WTM_graph_object wtm_graphs_object = Brute_force.algorithm(g, log_enabled, instance,
                            first_vertice_mode, get_id_of_combobox_vertice());
                    List<Graph> wtm_graphs = wtm_graphs_object.getWtm_graphs();
                    List<String> explanations = wtm_graphs_object.getExplanations();
                    List<Boolean> is_final_path = wtm_graphs_object.getIs_final_path();
                    List<List<Integer>> final_path_list = wtm_graphs_object.getFinal_path_list();
                    
                    String algorithm_name = "Brute Force";
                    if(first_vertice_mode) {
                        algorithm_name += " (wierzchołek początkowy: " +
                                g.getVertices_data().get(get_id_of_combobox_vertice()).getName() +
                                ")";
                    }
                    getWtm_panel().set_WTM_panel(algorithm_name,
                            wtm_graphs,
                            explanations,
                            wtm_graphs.size(),
                            is_final_path,
                            final_path_list);

                    unlock_buttons();
                }
            });
            
            thread.start();
            
        } else if (e.getSource() == two_approximation_button) {
            
            MenuView instance = this;
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lock_buttons();
                    
                    reset_most_results();
                    WTM_graph_object wtm_graphs_object = Two_approximation.algorithm(
                            g, log_enabled, instance,
                            first_vertice_mode, get_id_of_combobox_vertice());
                    List<Graph> wtm_graphs = wtm_graphs_object.getWtm_graphs();
                    List<String> explanations = wtm_graphs_object.getExplanations();
                    List<Boolean> is_final_path = wtm_graphs_object.getIs_final_path();
                    List<List<Integer>> final_path_list = wtm_graphs_object.getFinal_path_list();
                    
                    String algorithm_name = "Algorytm 2-aproksymacyjny";
                    if(first_vertice_mode) {
                        algorithm_name += " (wierzchołek początkowy: " +
                                g.getVertices_data().get(get_id_of_combobox_vertice()).getName() +
                                ")";
                    }
                    getWtm_panel().set_WTM_panel(algorithm_name,
                            wtm_graphs,
                            explanations,
                            wtm_graphs.size(),
                            is_final_path,
                            final_path_list);
                    
                    unlock_buttons();
                }
            });
            
            thread.start();
            
        } else if (e.getSource() == christofides_button) {
            
            MenuView instance = this;
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lock_buttons();
                    
                    reset_most_results();
                    WTM_graph_object wtm_graphs_object = Christofides.algorithm(
                            g, log_enabled, instance,
                            first_vertice_mode, get_id_of_combobox_vertice());
                    List<Graph> wtm_graphs = wtm_graphs_object.getWtm_graphs();
                    List<String> explanations = wtm_graphs_object.getExplanations();
                    List<Boolean> is_final_path = wtm_graphs_object.getIs_final_path();
                    List<List<Integer>> final_path_list = wtm_graphs_object.getFinal_path_list();
                    
                    String algorithm_name = "Algorytm Christofidesa";
                    if(first_vertice_mode) {
                        algorithm_name += " (wierzchołek początkowy: " +
                                g.getVertices_data().get(get_id_of_combobox_vertice()).getName() +
                                ")";
                    }
                    getWtm_panel().set_WTM_panel(algorithm_name,
                            wtm_graphs,
                            explanations,
                            wtm_graphs.size(),
                            is_final_path,
                            final_path_list);
                    
                    unlock_buttons();
                }
            });
            
            thread.start();
            
        } else if (e.getSource() == wtm_button) {
            
            MenuView instance = this;
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lock_buttons();
                    
                    reset_most_results();
                    WTM_graph_object wtm_graphs_object = WTM.algorithm(g, log_enabled, instance,
                            first_vertice_mode, get_id_of_combobox_vertice(),
                            Integer.valueOf(neurons_amount.getText()),
                            Integer.valueOf(epochs_amount.getText()),
                            Double.valueOf(learning_rate_factor.getText()),
                            Double.valueOf(decrease_learning_rate_factor.getText()),
                            Double.valueOf(neighborhood_radius.getText()),
                            Double.valueOf(decrease_neighborhood_radius.getText()));
                    List<Graph> wtm_graphs = wtm_graphs_object.getWtm_graphs();
                    List<String> explanations = wtm_graphs_object.getExplanations();
                    List<Boolean> is_final_path = wtm_graphs_object.getIs_final_path();
                    List<List<Integer>> final_path_list = wtm_graphs_object.getFinal_path_list();
                    
                    String algorithm_name = "Sieć neuronowa WTM";
                    if(first_vertice_mode) {
                        algorithm_name += " (wierzchołek początkowy: " +
                                g.getVertices_data().get(get_id_of_combobox_vertice()).getName() +
                                ")";
                    }
                    getWtm_panel().set_WTM_panel(algorithm_name,
                            wtm_graphs,
                            explanations,
                            wtm_graphs.size(),
                            is_final_path,
                            final_path_list);
                    
                    unlock_buttons();
                }
            });
            
            thread.start();
        } else if (e.getSource() == nearest_neighbour_button) {
            
            MenuView instance = this;
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    lock_buttons();
                    
                    reset_most_results();
                    WTM_graph_object wtm_graphs_object = Nearest_neighbour.algorithm(
                            g, log_enabled, instance,
                            first_vertice_mode, get_id_of_combobox_vertice());
                    List<Graph> wtm_graphs = wtm_graphs_object.getWtm_graphs();
                    List<String> explanations = wtm_graphs_object.getExplanations();
                    List<Boolean> is_final_path = wtm_graphs_object.getIs_final_path();
                    List<List<Integer>> final_path_list = wtm_graphs_object.getFinal_path_list();
                    
                    String algorithm_name = "Najbliższego sąsiada";
                    if(first_vertice_mode) {
                        algorithm_name += " (wierzchołek początkowy: " +
                                g.getVertices_data().get(get_id_of_combobox_vertice()).getName() +
                                ")";
                    }
                    getWtm_panel().set_WTM_panel(algorithm_name,
                            wtm_graphs,
                            explanations,
                            wtm_graphs.size(),
                            is_final_path,
                            final_path_list);
                    
                    unlock_buttons();
                }
            });
            
            thread.start();
            
        } else if (e.getSource() == clear_log_button) {
            clearLog(this.getTekstKonsoli());
        }
    }
    
    public static void addLog(JTextArea text_area, String text) {
        try {
            text_area.append(text);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public static void clearLog(JTextArea text_area) {
        try {
            text_area.selectAll();
            text_area.replaceSelection("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public static void set_time(JLabel time_label, long start, long stop, double bonus_time) {
        long nanoseconds_time = stop - start;
        
        DecimalFormatSymbols separator_symbol_change = new DecimalFormatSymbols(Locale.getDefault());
        separator_symbol_change.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.###", separator_symbol_change);
        df.setRoundingMode(RoundingMode.HALF_UP);
        Double seconds = nanoseconds_time * 0.000000001;
        seconds += bonus_time;
        try {
            time_label.setText(df.format(seconds));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public static void set_full_time(WTM_panel wtm_panel) {
        double time1 = Double.parseDouble(wtm_panel.get_time2_label().getText());
        double time2 = Double.parseDouble(wtm_panel.get_time3_label().getText());
        double time3 = Double.parseDouble(wtm_panel.get_time4_label().getText());
        double full_time = time1 + time2 + time3;
        
        DecimalFormatSymbols separator_symbol_change = new DecimalFormatSymbols(Locale.getDefault());
        separator_symbol_change.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.###", separator_symbol_change);
        df.setRoundingMode(RoundingMode.HALF_UP);
        try {
            wtm_panel.get_time5_label().setText(df.format(full_time));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public List<Integer> compare_two_path(Graph g, boolean time_measure) {
        long start_time, stop_time;
        start_time = System.nanoTime();
        
        MenuView instance = this;
        List<Integer> full_path = new ArrayList<Integer>();
        Distance_data distance_data = get_shortest_path_distance(g, path1, false, instance);
        double distance = distance_data.getDistance();
        if(is_path2) {
            Distance_data distance_data2 = get_shortest_path_distance(g, path2, false, instance);
            double distance2 = distance_data2.getDistance();
            if(distance2 < distance){
                distance = distance2;
                update_path(path2, g);
                full_path = distance_data2.getFull_path();
            } else {
                update_path(path1, g);
                full_path = distance_data.getFull_path();
            }
        } else {
            update_path(path1, g);
            full_path = distance_data.getFull_path();
        }
        wtm_panel.set_cost(distance);
        if(time_measure) {
            stop_time = System.nanoTime();
            set_time(wtm_panel.get_time4_label(), start_time, stop_time, 0);
            set_full_time(wtm_panel);
        }
        return full_path;
    }
    
    public void update_path(List<Integer> path, Graph g) {
        List<Vertice> vertices_data = g.getVertices_data();
        List<String> string_path = new ArrayList<String>();
        for(int i = 0; i < path.size() - 1; i++) {
            for(int j = 0; j < vertices_data.size(); j++) {
                if(vertices_data.get(j).getId() == path.get(i)) {
                    string_path.add(vertices_data.get(j).getName());
                }
            }
        }
        string_path.add(string_path.get(0));
        
        int counter = 0;
        String pth = "<html><p>";
        for(int i = 0; i < string_path.size(); i++) {
            pth += string_path.get(i);
            counter += string_path.get(i).length();
            if(counter < 40) {
                pth += "   ";
            } else if (i < string_path.size() - 1) {
                pth += "</p><p>";
                counter = 0;
            }
        }
        pth += "</p></html>";
        getWtm_panel().set_track(pth);
    }
    
    public static Distance_data get_shortest_path_distance(Graph g, List<Integer> path,
            boolean log_enabled, MenuView m) {
        int vertices_amount = g.getVertices().size();
        //informacja w jakiej kolejności są przechowywane wierzchołki w grafie
        int[] labels_for_graph = Brute_force.get_labels_for_graph(vertices_amount, g);
        //graf w postaci macierzy sąsiedztwa
        double[][] graph = Brute_force.get_a_graph_as_adjacency_matrix(vertices_amount, g, labels_for_graph);
        //informacja w jakiej kolejności są przechowywane wierzchołki w sciezce
        int[] labels_for_path = Brute_force.get_labels_for_path(path,
                vertices_amount, labels_for_graph);
        
        if(log_enabled) {
            Brute_force.print_some_data(path, vertices_amount, labels_for_graph,
                    labels_for_path, graph, m);
        }
        
        Distance_data distance_data = Brute_force.calculate_distance_n_full_path(path, labels_for_graph,
            labels_for_path, graph, log_enabled, m);
        
        return distance_data;
    }
    
    public void lock_buttons(){
        load_vertices_n_edges_button.setEnabled(false);
        load_vertices_button.setEnabled(false);
        export_graph_button.setEnabled(false);
        generate_full_graph_button.setEnabled(false);
        generate_graph_button.setEnabled(false);
        brute_force_button.setEnabled(false);
        two_approximation_button.setEnabled(false);
        christofides_button.setEnabled(false);
        wtm_button.setEnabled(false);
        nearest_neighbour_button.setEnabled(false);
        getWtm_panel().lock_preview();
    }
    
    public void unlock_buttons(){
        load_vertices_n_edges_button.setEnabled(true);
        load_vertices_button.setEnabled(true);
        export_graph_button.setEnabled(true);
        generate_full_graph_button.setEnabled(true);
        generate_graph_button.setEnabled(true);
        brute_force_button.setEnabled(true);
        two_approximation_button.setEnabled(true);
        christofides_button.setEnabled(true);
        wtm_button.setEnabled(true);
        nearest_neighbour_button.setEnabled(true);
        getWtm_panel().unlock_preview();
    }
    
    public void reset_results(){
        getWtm_panel().set_track("<html><p>-</p></html>");
        getWtm_panel().set_cost(0.0);
        getWtm_panel().set_time1(0.0);
        getWtm_panel().set_time2(0.0);
        getWtm_panel().set_time3(0.0);
        getWtm_panel().set_time4(0.0);
        getWtm_panel().set_time5(0.0);
    }
    
    public void reset_most_results(){
        getWtm_panel().set_track("<html><p>-</p></html>");
        getWtm_panel().set_cost(0.0);
        getWtm_panel().set_time2(0.0);
        getWtm_panel().set_time3(0.0);
        getWtm_panel().set_time4(0.0);
        getWtm_panel().set_time5(0.0);
    }
    
    public void set_default_wtm_params(Graph g) {
        int vertices = g.getVertices().size();
        neurons_amount.setText(String.valueOf(vertices * 10));
        int epochs = (int) Math.ceil(vertices / 2.0);
        epochs_amount.setText(String.valueOf(epochs));
        learning_rate_factor.setText(String.valueOf(0.3));
        decrease_learning_rate_factor.setText(String.valueOf(0.3 / epochs));
        
        double min_x = 1000000;
        double max_x = -1000000;
        double min_y = 1000000;
        double max_y = -1000000;
        for(int i = 0; i < vertices; i++) {
            double x = g.getVertices_data().get(i).getX();
            double y = g.getVertices_data().get(i).getY();
            if(x < min_x) {
                min_x = x;
            }
            else if(x > max_x) {
                max_x = x;
            }
            if(y < min_y) {
                min_y = y;
            }
            else if(y > max_y) {
                max_y = y;
            }
        }
        
        double range_x = 1.5 * Math.abs(max_x - min_x);
        double range_y = 1.5 * Math.abs(max_y - min_y);
        double radius = (range_x + range_y) / 8;
        
        neighborhood_radius.setText(String.valueOf(radius));
        decrease_neighborhood_radius.setText(String.valueOf(radius / epochs));
    }
    
    void set_array_of_vertices_for_combobox() {
        if (g.getVertices().size() > 0) {
            List<String> names = new ArrayList<String>();
            for (int i = 0; i < g.getVertices_data().size(); i++) {
                names.add(g.getVertices_data().get(i).getName());
            }
            String[] array = names.toArray(new String[0]);
            vertices_array.setModel(new DefaultComboBoxModel<String>(array));
            vertices_array.setSelectedIndex(0);
        } else {
            String[] empty_array = {"Brak wierzchołków"};
            vertices_array.setModel(new DefaultComboBoxModel<String>(empty_array));
            vertices_array.setSelectedIndex(0);
        }
    }
    
    int get_id_of_combobox_vertice() {
        String name = vertices_array.getSelectedItem().toString();
        for(int i = 0; i < g.getVertices_data().size(); i++) {
            if(g.getVertices_data().get(i).getName().equals(name)){
                return g.getVertices_data().get(i).getId();
            }
        }
        return (-1);
    }
    
    void create_single_preview(String algorithm_name) {
        List<Graph> wtm_graphs = new ArrayList<Graph>();
        wtm_graphs.add(g);
        List<String> explanations = new ArrayList<String>();
        explanations.add("Graf wejściowy");
        List<Boolean> is_final_path = new ArrayList<Boolean>();
        is_final_path.add(false);
        List<List<Integer>> final_path_list = new ArrayList<List<Integer>>();
        final_path_list.add(new ArrayList<Integer>());
        getWtm_panel().set_WTM_panel_params(algorithm_name, wtm_graphs, explanations, wtm_graphs.size(),
                is_final_path, final_path_list);
    }
}
