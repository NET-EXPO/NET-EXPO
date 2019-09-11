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
public class Reporter implements NetworkExposureReport,AffiliationExposureReport,DecompsedExposureReport {
    
    //NETWORK EXPOSURE VARIABLES
    boolean network_exposure_active = false;
    String ne_attribute = "";
    String ne_column = "";
    
    //AFFILIATION EXPOSURE 
    boolean affiliation_exposure_active = false;
    String ae_attribute = "";
    String ae_column = "";
    String two_mode_attribute = "";
    String two_mode_value = "";
    boolean wants_off_diagnonal = false;
    
    //DecomposedExposureReport
    boolean decomposed_exposure_active = false;
    String de_attribute = "";
    String de_column = "";
    String cMatrix_column = "";
    String c1_value = "";
    String c2_value = "";
    
    private Reporter() {
    }
    
    public static Reporter getInstance() {
        return ReporterHolder.INSTANCE;
    }

    @Override
    public boolean isNetworkExposureActive() {
         return network_exposure_active;

    }

    @Override
    public void setNetworkExposureActive(boolean value) {
        
        this.network_exposure_active = value;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNetworkExposureAttribute(String attribute) {
        this.ne_attribute = attribute;
    }

    @Override
    public String getNetworkExposureAttribute() {
        return this.ne_attribute;
    }

    @Override
    public void setNetworkExposureColumn(String column) {
        this.ne_column = column;
    }

    @Override
    public String getNetworkExposureColumn() {
       return this.ne_column;
    }

    @Override
    public String printNetworkExposureParameters() {
        
        StringBuilder content = new StringBuilder();
        
        
        if(this.network_exposure_active){
            
            //content.append("<p><b>Network Exposure Selected: </b> True </p>");
            content.append("<p><b>Selected exposure attribute:</b> ");
            content.append(this.ne_attribute);
            content.append("</p>");
            content.append("<p><b>Column for exposure attribute:</b> ");
            content.append(this.ne_column);
            content.append("</p>");
            return content.toString();
        }
        else{
            
            content.append("Not selected");
            
            return content.toString();
        }
    }

    @Override
    public boolean isAffiliationExposureActive() {
        
        return this.affiliation_exposure_active;
    }

    @Override
    public void setAffiliationExposureActive(boolean value) {
        this.affiliation_exposure_active = value;
    }

    @Override
    public String getAffiliationExposureAttribute() {
        return this.ae_attribute;
    }

    @Override
    public void setAffiliationExposureAttribute(String attribute) {
        
        this.ae_attribute = attribute;
    }

    @Override
    public boolean wantsOffDiagnonalValues() {
        return this.wants_off_diagnonal;
    }

    @Override
    public void setWantsOffDiagnonalValues(boolean value) {
        this.wants_off_diagnonal = value;
    }

    @Override
    public String getTwoModeNodeAttribute() {
        
        return this.two_mode_attribute;
    }

    @Override
    public void setTwoModeNodeAttribute(String attribute) {
        this.two_mode_attribute = attribute;
    }

    @Override
    public String getValueForTwoMode() {
        return this.two_mode_value;
    }

    @Override
    public void setValueForTwoMode(String value) {
        this.two_mode_value = value;
    }

    @Override
    public String printAffiliationExposureParameters() {
        
        StringBuilder content = new StringBuilder();
        
        if(this.affiliation_exposure_active){
            
            //content.append("<p><b>Affiliation Exposure Selected: </b> True </p>");
            content.append("<p><b>Selected exposure attribute:</b> ");
            content.append(this.ae_attribute);
            content.append("</p>");
            content.append("<p><b>Column for exposure attribute:</b> ");
            content.append(this.ae_column);
            content.append("</p>");
            content.append("<p><b>Selected two-mode:</b> ");
            content.append(this.two_mode_attribute);
            content.append("</p>");
            content.append("<p><b>First mode:</b> ");
            content.append(this.two_mode_value);
            content.append("</p>");
            content.append("<p><b>Produce off-diagnonals:</b> ");
            content.append(this.wants_off_diagnonal);
            content.append("</p>");
            return content.toString();
            
        }
        else{
            content.append("Not selected");
            
            return content.toString();
        }
        
        
        
        
    }

    @Override
    public String getAffiliationExposureColumn() {
        return this.ae_column;
    }

    @Override
    public void setAffiliationExposureColumn(String column) {
       this.ae_column = column;
    }

    @Override
    public boolean isDecomposedExposedActive() {
        return this.decomposed_exposure_active;
    }

    @Override
    public void setDecomposedExposedActive(boolean value) {
        this.decomposed_exposure_active = value;
    }

    @Override
    public void setDecomposedExposedAttribute(String attribute) {
        this.de_attribute = attribute;
    }

    @Override
    public String getDecomposedExposedAttribute() {
        return this.de_attribute;
    }

    @Override
    public void setDecomposedExposedColumn(String column) {
        this.de_column = column;
    }

    @Override
    public String getDecomposedExposedColumn() {
        return this.de_column;
    }

    @Override
    public String getCMatrixColumn() {
        return this.cMatrix_column;
    }

    @Override
    public void setCMatrixColumn(String attribute) {
        this.cMatrix_column = attribute;
    }

    @Override
    public String getC1Value() {
        return this.c1_value;
    }

    @Override
    public void setC1Value(String value) {
        this.c1_value = value;
    }

    @Override
    public String getC2Value() {
        return this.c2_value;
    }

    @Override
    public void setC2Value(String value) {
        this.c2_value = value;
    }

    @Override
    public String printDecomposedExposureParameters() {
        StringBuilder content = new StringBuilder();
        
        if(this.decomposed_exposure_active){
            
            
            content.append("<p><b>Selected exposure attribute:</b> ");
            content.append(this.de_attribute);
            content.append("</p>");
            content.append("<p><b>Column for exposure attribute:</b> ");
            content.append(this.de_column);
            content.append("</p>");
            content.append("<p><b>Column for matrix separation:</b> ");
            content.append(this.cMatrix_column);
            content.append("</p>");
            content.append("<p><b>C1:</b> ");
            content.append(this.c1_value);
            content.append("</p>");
            content.append("<p><b>C2:</b> ");
            content.append(this.c2_value);
            content.append("</p>");
            
            return content.toString();
        }
        else{
            content.append("Not selected");
            
            return content.toString();
        }
        
    }
    
    private static class ReporterHolder {

        private static final Reporter INSTANCE = new Reporter();
    }
}
