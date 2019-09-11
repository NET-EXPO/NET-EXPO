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
public interface AffiliationExposureReport {
    
    public boolean isAffiliationExposureActive();
    public void setAffiliationExposureActive(boolean value);
    
    public String getAffiliationExposureAttribute();
    public void setAffiliationExposureAttribute(String attribute);
    public String getAffiliationExposureColumn();
    public void setAffiliationExposureColumn(String column);
    
    
    public boolean wantsOffDiagnonalValues();
    public void setWantsOffDiagnonalValues(boolean value);
    
    public String getTwoModeNodeAttribute();
    public void setTwoModeNodeAttribute(String attribute);
    public String getValueForTwoMode();
    public void setValueForTwoMode(String value);
    
    public String printAffiliationExposureParameters();
    
}
