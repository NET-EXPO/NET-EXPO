/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.ejml.simple.SimpleMatrix;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Table;

/**
 *
 * @author mac
 */
public class AffiliationExposure extends ExposureComputation {

    private String primaryModeAttribute = null;
    private String primaryModeColumn = null;
    private List<Node> primaryNodes;
    private List<Node> secondaryNodes;
    private List<Edge> offValueDiagonals;
    //private Table edgeTable = null;
    private boolean calculateOffDiagonal = false;
    
    
    public AffiliationExposure(String primaryModeAttribute, String primaryModeColumn) {
        this.primaryModeAttribute = primaryModeAttribute;
        this.primaryModeColumn = primaryModeColumn;
    }

    public SimpleMatrix initTo2Mode(Graph graph) {

        primaryNodes = new ArrayList<Node>();
        secondaryNodes = new ArrayList<Node>();

        Node[] nodes = graph.getNodes().toArray();

        for (Node node : nodes) {

            if (node.getAttribute(primaryModeColumn).toString().equals(primaryModeAttribute)) {
                primaryNodes.add(node);
            } else {
                secondaryNodes.add(node);
            }
        }

        System.out.println("primary size: " + primaryNodes.size());
        System.out.println("secondary size: " + secondaryNodes.size());

        double[][] a = new double[primaryNodes.size()][secondaryNodes.size()];
        
        for(int x=0; x < primaryNodes.size(); x++ ){
            for(int y = 0; y < secondaryNodes.size(); y++){
                
                Edge edge = graph.getEdge(primaryNodes.get(x), secondaryNodes.get(y));
                if(edge !=null){
                    a[x][y] += 1;
                }
                else{
                    a[x][y] += 0;
                    
                }
                
            }
        }
        
        SimpleMatrix a_matrix = new SimpleMatrix(a);
        a_matrix.print();
        
        SimpleMatrix a_prime_matrix = new SimpleMatrix(a_matrix.transpose());
        a_prime_matrix.print();
        
        SimpleMatrix c_matrix = a_matrix.mult(a_prime_matrix);
        c_matrix.print();
        
        //gut out that diagnol
        assert(c_matrix.numCols() == c_matrix.numRows());
        for(int i=0;i<c_matrix.numRows();i++){
            c_matrix.set(i, i, 0);
        }
        System.out.println("---- c matrix diagonal -----");
        c_matrix.print();
        
        //TODO: add edge.setLabel( [user_defined] ) to distinguish edges part of the affiliated matrix
        if(calculateOffDiagonal){
            
            GraphModel graph_model = graph.getModel();
            offValueDiagonals = new ArrayList<Edge>();
            
            for(int x = 0; x < primaryNodes.size(); x++){
                for(int y = 0 ; y < primaryNodes.size(); y++){
                    Edge edge = graph.getEdge(primaryNodes.get(x), primaryNodes.get(y));
                    if(edge != null){
                        
                        if(x != y){
                            Edge e = graph_model.factory().newEdge(primaryNodes.get(x), primaryNodes.get(y), graph_model.isDirected());
                            e.setAttribute("off_diagonal",c_matrix.get(x, y));
                            e.setLabel("off_diagonal");
                            offValueDiagonals.add(e);
                        }
                        
                    }
                    else{
                        
                        if( x != y){
                            
                            Edge e = graph_model.factory().newEdge(primaryNodes.get(x), primaryNodes.get(y), graph_model.isDirected());
                            
                            e.setAttribute("off_diagonal", c_matrix.get(x,y));
                            e.setLabel("off_diagonal");
                            offValueDiagonals.add(e);
                        }
                        
                        //graph.addEdge(e);
                        
                    }
                    
                }
            }
            
        }
        
        return c_matrix;
    }

    @Override
    public void computeExposure(Graph graph, Column attribute, String exposureColumn) {
        //Node[] nodes = graph.getNodes().toArray();
        
        

        SimpleMatrix c_matrix = initTo2Mode(graph);
        attribute_vector = new double[c_matrix.numRows()][1];
        nomination_vector = new double[c_matrix.numRows()][1];

        for(int x = 0; x < c_matrix.numRows(); x++){
            //for attribute vector
            if (primaryNodes.get(x).getAttribute(attribute) != null
                    && StringUtils.isNumeric(primaryNodes.get(x).getAttribute(attribute).toString())) {
                    
                attribute_vector[x][0] = Double.parseDouble(primaryNodes.get(x).getAttribute(attribute).toString());
            }
            else{
                attribute_vector[x][0] = Double.NaN;
            }
            
            double num_nom = 0;
            
            for(int y=0; y < c_matrix.numCols(); y++){
                num_nom = num_nom + c_matrix.get(x, y);
                
                
                
                
                
            }
            
            
            nomination_vector[x][0] = num_nom;
            
        }
        

        
        
        
        SimpleMatrix attribute_matrix = new SimpleMatrix(attribute_vector);
        SimpleMatrix nomination_matrix = new SimpleMatrix(nomination_vector);

       
        System.out.println("attribute matrix");
        attribute_matrix.print();
        
        System.out.println("nomination matrix");
        nomination_matrix.print();

        SimpleMatrix numerator = c_matrix.mult(attribute_matrix);
        SimpleMatrix expo = numerator.elementDiv(nomination_matrix);
        
        DecimalFormat df = new DecimalFormat("#0.0000");
        
        for(int i=0 ; i<primaryNodes.size();i++){
          System.out.println("" + i + " - " + expo.get(i, 0));
          if(Double.isNaN(expo.get(i, 0))){
              primaryNodes.get(i).setAttribute(exposureColumn, 0.00);
          }
          else{
              primaryNodes.get(i).setAttribute(exposureColumn, Double.parseDouble( df.format(expo.get(i,0))));
          }
        }

        //expo.print();
    }

    public void setupOffDiagonalColumn(Table edgeTable) {
        //this.edgeTable = edgeTable;
        edgeTable.addColumn("off_diagonal", Double.TYPE);
        calculateOffDiagonal = true;
    }
    
    public List<Edge> getOffValueDiagonals(){

        return this.offValueDiagonals;
        
    }
}
