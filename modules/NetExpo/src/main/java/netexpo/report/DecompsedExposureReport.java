/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo.report;

/**
 *
 * @author mac
 */
public interface DecompsedExposureReport {
    public boolean isDecomposedExposedActive();
    public void setDecomposedExposedActive(boolean value);
    
    public void setDecomposedExposedAttribute(String attribute);
    public String getDecomposedExposedAttribute();
    public void setDecomposedExposedColumn(String column);
    public String getDecomposedExposedColumn();
    
    public String getCMatrixColumn();
    public void setCMatrixColumn(String attribute);
    
    public String getC1Value();
    public void setC1Value(String value);
    
    public String getC2Value();
    public void setC2Value(String value);
    
    public String printDecomposedExposureParameters();
    
}
