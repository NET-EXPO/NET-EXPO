/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import javax.swing.JPanel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mac
 */
@ServiceProvider(service = StatisticsUI.class)
public class NetExpoUI implements StatisticsUI {
    
    private NetExpoPanel panel;
    private NetExpo statistics;
    
    @Override
    public JPanel getSettingsPanel() {
        panel = new NetExpoPanel();
        return panel;
    }

    @Override
    public void setup(Statistics ststcs) {
        this.statistics = (NetExpo)ststcs;
        if(panel !=null){
            panel.setAttributes(statistics.getNodeAttributes());
            panel.setGraph(statistics.getGraph());
            panel.setEdgeAttributes(statistics.getEdgeAttributes());
        }
        System.out.println("I am starting...");
    }

    @Override
    public void unsetup() {
        
        if(panel !=null){
            //transfer panel data to statistics instance
            statistics.setNetworkExposureAttribute(panel.getSelectedNetworkExposureAttribute());
            
            statistics.setTwoModeConfig(panel.getTwoModeColumn(), panel.getTwoModeValue());
            
            statistics.setAffiliationExposureAttribute(panel.getSelectedAffiliationExposureAttribute());
            
            statistics.setUserOffDiagonalChoice( panel.getUserOffDiagonalValuesChoice() );
            
            statistics.setUserDefinedColumns(
                    panel.getUserNetExpoColumn(),
                    panel.getUserAffExpoColumn(), 
                    panel.getUserDecompExpoColumn()
            );
            
            statistics.setDecomposedMatrices(panel.getCMatrixColumn(),panel.getC1Attribute(), panel.getC2Attribute());
            statistics.setDecompositionExposureAttribute(panel.getSelectedDecompositionExposureAttribute());
            
        }
        
        this.statistics = null;
        this.panel = null;
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return NetExpo.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "NET-EXPO";
    }

    @Override
    public String getShortDescription() {
        return "short description needed";
    }

    @Override
    public String getCategory() {
        return StatisticsUI.CATEGORY_NODE_OVERVIEW;
    }

    @Override
    public int getPosition() {
        return 10000;
    }
    
}
