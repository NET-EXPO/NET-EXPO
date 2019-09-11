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
public interface NetworkExposureReport {
    
    public boolean isNetworkExposureActive();
    public void setNetworkExposureActive(boolean value);
    
    public void setNetworkExposureAttribute(String attribute);
    public String getNetworkExposureAttribute();
    
    public void setNetworkExposureColumn(String column);
    public String getNetworkExposureColumn();
    
    public String printNetworkExposureParameters();
    
}
