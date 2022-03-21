/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tsp_solver;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mycompany.tsp_solver.algorithms.Edge;
import com.mycompany.tsp_solver.algorithms.Graph;
import com.mycompany.tsp_solver.algorithms.Vertice;
import java.awt.Dimension;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import javax.swing.JApplet;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 *
 * @author mic45
 */
public class GraphBuilder extends JApplet {

    private JGraphXAdapter<String, GraphEdge> jgxAdapter;
    mxGraphModel graphModel;
    mxGraphLayout graph_layout;
    
    int width;
    int height;
    
    public GraphBuilder(Graph graph, int width, int height) {
        super();

        this.width = width;
        this.height = height;
        graph_init(graph, this.width, this.height);
    }

    public void graph_init(Graph graph, int width, int height) {
        Dimension size = new Dimension(width, height);
        
        ListenableGraph<String, GraphEdge> g =
                new DefaultListenableGraph<>(new DefaultUndirectedWeightedGraph<>(GraphEdge.class));

        jgxAdapter = new JGraphXAdapter<>(g);

        setPreferredSize(size);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setEnabled(false);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        
        getContentPane().add(component);
        resize(size);

        List<Vertice> vertices = graph.getVertices_data();
        List<Edge> edges = graph.getEdges();
        
        double minX = 1000000;
        double minY = 1000000;
        double maxX = -1000000;
        double maxY = -1000000;
        for(int i = 0; i < vertices.size(); i++) {
            g.addVertex(vertices.get(i).getName());
            double X = vertices.get(i).getX();
            double Y = vertices.get(i).getY();
            if(X < minX) {
                minX = X;
            } else if (X > maxX) {
                maxX = X;
            }
            if(Y < minY) {
                minY = Y;
            } else if (Y > maxY) {
                maxY = Y;
            }
        }
                       
        graphModel = (mxGraphModel) component.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        
        graph_layout = new graphLayout(jgxAdapter);
        
        double Xrange = Math.abs(maxX - minX);
        double Xmargin = 0.2 * Xrange;
        Xrange += (2 * Xmargin);
        double Yrange = Math.abs(maxY - minY);
        double Ymargin = 0.2 * Yrange;
        Yrange += (2 * Ymargin);
        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if (c.isVertex()) {
                for (int i = 0; i < vertices.size(); i++) {
                    String vertice_name = vertices.get(i).getName();
                    if (c.getValue().equals(vertice_name)) {
                        double X = vertices.get(i).getX();
                        double Y = vertices.get(i).getY();
                        double scaledX = ((X - minX + Xmargin) / Xrange) * width;
                        double scaledY = ((Y - minY + Ymargin) / Yrange) * height;
                        double invertedY = height - scaledY;
                        c.getGeometry().setX(scaledX);
                        c.getGeometry().setY(invertedY);
                    }
                }
            }
        }

        for(int i = 0; i < edges.size(); i++) {
            Integer beginning = edges.get(i).getBeginning();
            Integer end = edges.get(i).getEnd();
            Integer b_id = (-1);
            Integer e_id = (-1);
            Double weight = edges.get(i).getWeight();
            for(int j = 0; j < vertices.size(); j++) {
                if(vertices.get(j).getId() == beginning) {
                    b_id = j;
                }
                if(vertices.get(j).getId() == end) {
                    e_id = j;
                }
                if(b_id != (-1) && e_id != (-1)){
                    break;
                }
            }
            g.addEdge(vertices.get(b_id).getName(), vertices.get(e_id).getName());
            GraphEdge e = g.getEdge(vertices.get(b_id).getName(), vertices.get(e_id).getName());
            g.setEdgeWeight(e, weight);
        }
        
        graphModel = (mxGraphModel) component.getGraph().getModel();
        cells = graphModel.getCells().values();
        mxUtils.setCellStyles(component.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);

        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if(c.isEdge()) {
                if (c.getValue().toString().equals("-1")) {
                    mxCell array[] = new mxCell[]{c};
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "brown", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKEWIDTH, "2", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_NOLABEL, "true", array);
                }
            }
        }
        
        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if (c.isVertex()) {
                String vr = (String) c.getValue();
                if (vr.length() >= 6) {
                    if (vr.substring(0, 6).equals("Neuron")) {
                        mxCell array[] = new mxCell[]{c};
                        component.getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "red", array);
                        c.getGeometry().setWidth(10);
                        c.setValue("n" + vr.substring(7));
                    }
                }
            }
        }
        
        component.refresh();
        graph_layout.execute(jgxAdapter.getDefaultParent());
    }
    
    public class graphLayout extends mxGraphLayout{
        public graphLayout(mxGraph graph) {
            super(graph);
        }
    }
    
    public static class GraphEdge extends DefaultWeightedEdge {
        private boolean isAnswer = false;

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return String.valueOf(df.format(getWeight()));
        }
    }
    
    public void repaint(Graph graph)
    {
        getContentPane().removeAll();
        graph_init(graph, this.width, this.height);
    }
    
    public void add_answer(Graph graph, List<Integer> path) {
        getContentPane().removeAll();
        
        int width = this.width;
        int height = this.height;
        
        ListenableGraph<String, GraphEdge> g =
                new DefaultListenableGraph<>(new DefaultUndirectedWeightedGraph<>(GraphEdge.class));

        jgxAdapter = new JGraphXAdapter<>(g);

        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setEnabled(false);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        
        getContentPane().add(component);

        List<Vertice> vertices = graph.getVertices_data();
        List<Edge> edges = graph.getEdges();
        
        double minX = 1000000;
        double minY = 1000000;
        double maxX = -1000000;
        double maxY = -1000000;
        for(int i = 0; i < vertices.size(); i++) {
            g.addVertex(vertices.get(i).getName());
            double X = vertices.get(i).getX();
            double Y = vertices.get(i).getY();
            if(X < minX) {
                minX = X;
            } else if (X > maxX) {
                maxX = X;
            }
            if(Y < minY) {
                minY = Y;
            } else if (Y > maxY) {
                maxY = Y;
            }
        }
        
        graphModel = (mxGraphModel) component.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        
        graph_layout = new graphLayout(jgxAdapter);
        
        double Xrange = Math.abs(maxX - minX);
        double Xmargin = 0.2 * Xrange;
        Xrange += (2 * Xmargin);
        double Yrange = Math.abs(maxY - minY);
        double Ymargin = 0.2 * Yrange;
        Yrange += (2 * Ymargin);
        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if (c.isVertex()) {
                for (int i = 0; i < vertices.size(); i++) {
                    String vertice_name = vertices.get(i).getName();
                    if (c.getValue().equals(vertice_name)) {
                        double X = vertices.get(i).getX();
                        double Y = vertices.get(i).getY();
                        double scaledX = ((X - minX + Xmargin) / Xrange) * width;
                        double scaledY = ((Y - minY + Ymargin) / Yrange) * height;
                        double invertedY = height - scaledY;
                        c.getGeometry().setX(scaledX);
                        c.getGeometry().setY(invertedY);
                    }
                }
            }
        }

        for(int i = 0; i < edges.size(); i++) {
            Integer beginning = edges.get(i).getBeginning();
            Integer end = edges.get(i).getEnd();
            Integer b_id = (-1);
            Integer e_id = (-1);
            Double weight = edges.get(i).getWeight();
            for(int j = 0; j < vertices.size(); j++) {
                if(vertices.get(j).getId() == beginning) {
                    b_id = j;
                }
                if(vertices.get(j).getId() == end) {
                    e_id = j;
                }
                if(b_id != (-1) && e_id != (-1)){
                    break;
                }
            }
            
            boolean is_in_answer = false;
            for(int j = 0; j < path.size() - 1; j++) {
                int point_a = path.get(j);
                int point_b = path.get(j + 1);
                if((beginning == point_a && end == point_b) ||
                        (beginning == point_b && end == point_a)) {
                    is_in_answer = true;
                    break;
                }
            }
            
            g.addEdge(vertices.get(b_id).getName(), vertices.get(e_id).getName());
            GraphEdge e = g.getEdge(vertices.get(b_id).getName(), vertices.get(e_id).getName());
            if(is_in_answer) {
                e.isAnswer = true;
            }
            g.setEdgeWeight(e, weight);
        }
        
        graphModel = (mxGraphModel) component.getGraph().getModel();
        cells = graphModel.getCells().values();
        mxUtils.setCellStyles(component.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        
        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if(c.isEdge()) {
                GraphEdge e = (GraphEdge) c.getValue();
                if(e.isAnswer) {
                    mxCell array[] = new mxCell[]{c};
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "green", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKEWIDTH, "2", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_FONTCOLOR, "green", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_FONTSTYLE, String.valueOf(mxConstants.FONT_BOLD), array);
                } else if (c.getValue().toString().equals("-1")) {
                    mxCell array[] = new mxCell[]{c};
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "brown", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_STROKEWIDTH, "2", array);
                    component.getGraph().setCellStyles(mxConstants.STYLE_NOLABEL, "true", array);
                }
            }
        }
        
        for (Object cell : cells) {
            mxCell c = (mxCell) cell;
            if (c.isVertex()) {
                String vr = (String) c.getValue();
                if (vr.length() >= 6) {
                    if (vr.substring(0, 6).equals("Neuron")) {
                        mxCell array[] = new mxCell[]{c};
                        component.getGraph().setCellStyles(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "red", array);
                        c.getGeometry().setWidth(10);
                        c.setValue("n" + vr.substring(7));
                    }
                }
            }
        }
        component.refresh();
        graph_layout.execute(jgxAdapter.getDefaultParent());
    }
}
