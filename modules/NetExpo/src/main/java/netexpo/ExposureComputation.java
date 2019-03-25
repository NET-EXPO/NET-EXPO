/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import org.gephi.graph.api.Column;
import org.gephi.graph.api.Graph;

/**
 *
 * @author mac
 */
abstract class ExposureComputation {
    
    double [][] network_matrix_data;
    double [][] attribute_vector;
    double [][] nomination_vector;
    
    abstract public void computeExposure(Graph graph, Column attribute, String exposureColumn);
    
}
