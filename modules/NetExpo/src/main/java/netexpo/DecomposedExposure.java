/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.ejml.simple.SimpleMatrix;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author mac
 */
public class DecomposedExposure extends ExposureComputation {

    //private String primaryModeAttribute_1 = null; 
    private String c1_attribute_1 = null;
    private String c1_attribute_2 = null;
    
    //private String primaryModeAttribute_2 = null;
    private String c2_attribute_1 = null;
    private String c2_attribute_2 = null;
    
    private String c_column = null;
    
    private String primaryModeColumn_1 = null;
    private String primaryModeColumn_2 = null;

    private List<Node> primaryNodes_c1;
    private List<Node> secondaryNodes_c1;

    private List<Node> primaryNodes_c2;
    private List<Node> secondaryNodes_c2;
    
    private List<Node> c_nodes;
    
    public DecomposedExposure(String c_column, String c1_variable_attribute, String c2_variable_attribute){
        this.c_column = c_column;
        this.c1_attribute_1 = c1_variable_attribute;
        this.c2_attribute_1 = c2_variable_attribute;
    }

    public DecomposedExposure(String primaryModeAttribute_1, String primaryModeColumn_1,
            String primaryModeAttribute_2, String primaryModeColumn_2) {

    }

    private SimpleMatrix initTo2Mode(Graph graph, boolean isC2) {
        if (isC2) {
            return initToC2(graph);
        } else {
            return initToC1(graph);
        }
    }
    
    public SimpleMatrix initOneMode(Graph graph, String c_attribute){
        
        if (c_nodes == null) {
            Node[] nodes = graph.getNodes().toArray();
            c_nodes = new LinkedList<Node>();
            for (Node node : nodes) {
                c_nodes.add(node);
            }
        }

        double[][] c = new double[c_nodes.size()][c_nodes.size()];
        
        for(int x=0; x< c_nodes.size();x++){
            for(int y=0; y < c_nodes.size(); y++){
                
                
                Edge edge = graph.getEdge(c_nodes.get(x), c_nodes.get(y));

                
                if((edge !=null) && 
                        (edge.getAttribute(c_column).toString().equals(c_attribute))){
                    c[x][y] +=1;
                }
                else {
                    c[x][y] +=0;
                }
                
            }
        }
        
        SimpleMatrix c_matrix = new SimpleMatrix(c);
        
        c_matrix.print();
        
        return c_matrix;
    }
    

    @Override
    public void computeExposure(Graph graph, Column attribute, String exposureColumn) {
        //SimpleMatrix c1 = initTo2Mode(graph, false);
        //SimpleMatrix c2 = initTo2Mode(graph, true);
        
        SimpleMatrix c1 = initOneMode(graph, this.c1_attribute_1);
        SimpleMatrix c2 = initOneMode(graph, this.c2_attribute_1);

        //binarize c2Binary
        SimpleMatrix c2Binary = new SimpleMatrix(c2);

        for (int x = 0; x < c2Binary.numRows(); x++) {
            for (int y = 0; y < c2Binary.numCols(); y++) {

                if (c2Binary.get(x, y) > 0) {
                    c2Binary.set(x, y, 1);
                }

            }
        }

        //binarize and inverse c
        SimpleMatrix c2Inverse = new SimpleMatrix(c2Binary);
        for (int x = 0; x < c2Inverse.numRows(); x++) {
            for (int y = 0; y < c2Inverse.numCols(); y++) {

                if (x != y) {
                    if (c2Inverse.get(x, y) == 0) {
                        c2Inverse.set(x, y, 1);
                    } else {
                        c2Inverse.set(x, y, 0);
                    }
                }

            }
        }
        
        int size_c1 =c1.getNumElements();
        int size_c2 =c2.getNumElements();
        
        System.out.println("c1 size: " + size_c1 + " -  c2 size: " + size_c2);
        
        //calculate shared (with c2Binary)
         SimpleMatrix shared_matrix = c1.elementMult(c2Binary);
        
        
        //calculate not shared (with c2Inverse)
        SimpleMatrix not_shared_matrix = c1.elementMult(c2Inverse);
        
        try {
            c1.saveToFileCSV("c1.csv");
            c2Binary.saveToFileCSV("c2Binary.csv");
            shared_matrix.saveToFileCSV("shared.csv");
            not_shared_matrix.saveToFileCSV("not_shared.csv");
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        //shared exposure calculation
        this.computeExposure(shared_matrix, attribute, "shared_"+exposureColumn);
        
        //not shared exposure calculation
        this.computeExposure(not_shared_matrix, attribute, "unshared_" +exposureColumn);

        
    }
    
    private void computeExposure(SimpleMatrix sm, Column attribute, String exposureColumn){
        
        network_matrix_data = new double[c_nodes.size()][c_nodes.size()];
        attribute_vector = new double[c_nodes.size()][1];
        nomination_vector = new double[c_nodes.size()][1];
        
        System.out.println(attribute.toString());
        
        for(int x = 0; x < sm.numRows(); x++){
            
            //assign attribute value for vector
            if(c_nodes.get(x).getAttribute(attribute) != null && StringUtils.isNumeric(c_nodes.get(x).getAttribute(attribute).toString())) {
                attribute_vector[x][0] = Double.parseDouble(c_nodes.get(x).getAttribute(attribute).toString());
            }
            else{
                attribute_vector[x][0] = 0.00;
            }
            
            double num_nom = 0;
            for(int y = 0; y < sm.numCols(); y++){
                num_nom = num_nom + sm.get(x, y);
            }
            
            nomination_vector[x][0]= num_nom;
            
        }
        
        SimpleMatrix attribute_matrix = new SimpleMatrix(attribute_vector);
        SimpleMatrix nomination_matrix = new SimpleMatrix(nomination_vector);

       
        System.out.println("attribute matrix");
        attribute_matrix.print();
        
        System.out.println("nomination matrix");
        nomination_matrix.print();

        SimpleMatrix numerator = sm.mult(attribute_matrix);
        SimpleMatrix expo = numerator.elementDiv(nomination_matrix);
        
       
        
        DecimalFormat df = new DecimalFormat("#0.0000");
        
        for(int i =0; i< c_nodes.size(); i++){
            if(Double.isNaN(expo.get(i, 0))){
                c_nodes.get(i).setAttribute(exposureColumn, 0.00);
            }
            else{
                c_nodes.get(i).setAttribute(exposureColumn, Double.parseDouble( df.format(expo.get(i,0))));
            }
        }
        
    }

    private SimpleMatrix initToC2(Graph graph) {

        primaryNodes_c2 = new ArrayList<Node>();
        secondaryNodes_c2 = new ArrayList<Node>();

        Node[] nodes = graph.getNodes().toArray();

        for (Node node : nodes) {

            if (node.getAttribute(primaryModeColumn_2).toString().equals(c2_attribute_1)) {
                primaryNodes_c2.add(node);
            } else if (node.getAttribute(primaryModeColumn_2).toString().equals(c2_attribute_2))  {
                secondaryNodes_c2.add(node);
            }
        }

       

        double[][] a = new double[primaryNodes_c2.size()][secondaryNodes_c2.size()];

        for (int x = 0; x < primaryNodes_c2.size(); x++) {
            for (int y = 0; y < secondaryNodes_c2.size(); y++) {

                Edge edge = graph.getEdge(primaryNodes_c2.get(x), secondaryNodes_c2.get(y));
                if (edge != null) {
                    a[x][y] += 1;
                } else {
                    a[x][y] += 0;

                }

            }
        }

        SimpleMatrix c2_matrix = new SimpleMatrix(a);
        c2_matrix.print();

        return c2_matrix;
        
        
    }

        
    private SimpleMatrix initToC1(Graph graph) {

        primaryNodes_c1 = new ArrayList<Node>();
        secondaryNodes_c1 = new ArrayList<Node>();

        Node[] nodes = graph.getNodes().toArray();

        for (Node node : nodes) {

            if (node.getAttribute(primaryModeColumn_1).toString().equals(c1_attribute_1)) {
                primaryNodes_c1.add(node);
            } else if (node.getAttribute(primaryModeColumn_1).toString().equals(c1_attribute_2)){
                secondaryNodes_c1.add(node);
            }
        }

        System.out.println("primary size: " + primaryNodes_c1.size());
        System.out.println("secondary size: " + secondaryNodes_c1.size());

        double[][] a = new double[primaryNodes_c1.size()][secondaryNodes_c1.size()];

        for (int x = 0; x < primaryNodes_c1.size(); x++) {
            for (int y = 0; y < secondaryNodes_c1.size(); y++) {

                Edge edge = graph.getEdge(primaryNodes_c1.get(x), secondaryNodes_c1.get(y));
                if (edge != null) {
                    a[x][y] += 1;
                } else {
                    a[x][y] += 0;

                }

            }
        }

        SimpleMatrix c1_matrix = new SimpleMatrix(a);
        c1_matrix.print();

        
        return c1_matrix;
        
    }

}
