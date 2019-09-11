/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;
import org.ejml.simple.SimpleMatrix;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

/**
 *
 * @author mac
 */
public class NetworkExposure extends ExposureComputation{
    
    

    public NetworkExposure() {

    }

    public void calculateNetworkExposure(Graph graph, Column attribute, String exposureColumn) {

        
    }

    @Override
    public void computeExposure(Graph graph, Column attribute, String exposureColumn) {
        System.out.println("Starting calculation for NE");

        //convert to matrix
        Node[] nodes = graph.getNodes().toArray();
        System.out.println("convert to array..");

        network_matrix_data = new double[nodes.length][nodes.length];
        attribute_vector = new double[nodes.length][1];
        nomination_vector = new double[nodes.length][1];

        for (int x = 0; x < nodes.length; x++) {

            //assign attribute value for vector
            if (nodes[x].getAttribute(attribute) != null && StringUtils.isNumeric(nodes[x].getAttribute(attribute).toString())) {
                attribute_vector[x][0] = Double.parseDouble(nodes[x].getAttribute(attribute).toString());
            } else {
                attribute_vector[x][0] = 0.00;
            }

            int num_nom = 0;
            for (int y = 0; y < nodes.length; y++) {
                Edge edge = graph.getEdge(nodes[x], nodes[y]);

                if (edge != null) {
                    network_matrix_data[x][y] += 1.00;
                    num_nom++;
                } else {
                    network_matrix_data[x][y] += 0.00;
                }
            }
            //System.out.println(x);
            nomination_vector[x][0] = num_nom;
        }

        System.out.println("starting simple matrix...");

        SimpleMatrix network_matrix = new SimpleMatrix(network_matrix_data);
        SimpleMatrix attribute_matrix = new SimpleMatrix(attribute_vector);
        SimpleMatrix nomination_matrix = new SimpleMatrix(nomination_vector);

        
        SimpleMatrix numerator = network_matrix.mult(attribute_matrix);
        //numerator.print();
        SimpleMatrix expo = numerator.elementDiv(nomination_matrix);
        //expo.print();

        DecimalFormat df = new DecimalFormat("#0.0000");
        for(int i=0; i<nodes.length; i++){
            System.out.println("" + i + " - " + expo.get(i, 0));
            if(Double.isNaN(expo.get(i, 0))){
                nodes[i].setAttribute(exposureColumn, 0.00);
            }
            else{
                nodes[i].setAttribute(exposureColumn, Double.parseDouble( df.format(expo.get(i,0))));
            }
            
        }
        
        System.out.println("Ending NE");
    }

}
