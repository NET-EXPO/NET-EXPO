/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netexpo;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mac
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class NetExpoBuilder implements StatisticsBuilder {

    @Override
    public String getName() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "Network Exposure Calculations";
    }

    @Override
    public Statistics getStatistics() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new NetExpo();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return NetExpo.class;
    }
    
}
