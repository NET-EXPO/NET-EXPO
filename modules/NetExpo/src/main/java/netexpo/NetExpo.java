package netexpo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mac
 */
import netexpo.report.Reporter;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Table;
import org.gephi.statistics.spi.Statistics;
import org.openide.util.Lookup;

public class NetExpo implements Statistics {

    public String NET_EXPOSURE = "net_exposure_score";
    public String AFF_EXPOSURE = "affilition_exposure_score";
    public String DECOMP_EXPOSURE = "decomposed_exposure_score";

    private Column network_exposure_attribute;
    private Column affiliation_exposure_attribute;
    private Column decomposition_exposure_attribute;
    
    private String twoModePrimaryValue;
    private String twoModePrimaryColumn;

    private String c_column;
    private String c1_one_mode_attribute;
    private String c2_one_mode_attribute;
    
    Column network_exposure_column;
    Column affiliation_exposure_column;
    Column decomposition_exposure_column;
    Column u_decomposition_exposure_column;
    
    private NetworkExposure ne;
    private AffiliationExposure ae;
    private DecomposedExposure de;
    
    private boolean calculateOffDiagonal = false;
    

    public void setNetworkExposureAttribute(String attribute) {

        //System.out.println("got network exposure attribute at netexp.java");

        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Table nodeTable = graphController.getGraphModel().getNodeTable();

        for (Column column : nodeTable.toList()) {
            if (column.getTitle().equals(attribute)) {
                this.network_exposure_attribute = column;
            }
        }

//this.network_exposure_attribute = attribute;
    }
    
    public void setAffiliationExposureAttribute(String attribute) {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Table nodeTable = graphController.getGraphModel().getNodeTable();
        
        for(Column column: nodeTable.toList()){
            if(column.getTitle().equals(attribute)){
                this.affiliation_exposure_attribute = column;
            }
        }
    }
    
    public void setDecompositionExposureAttribute(String attribute){
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Table nodeTable = graphController.getGraphModel().getNodeTable();
        
        for(Column column: nodeTable.toList()){
            if(column.getTitle().equals(attribute)){
                this.decomposition_exposure_attribute = column;
            }
        }
    }
    

    @Override
    public void execute(GraphModel gm) {
        
        if(decomposition_exposure_attribute != null){
            de = new DecomposedExposure(c_column, c1_one_mode_attribute, c2_one_mode_attribute);
        }

        if (network_exposure_attribute != null) {
            ne = new NetworkExposure();
        }

        if (twoModePrimaryValue != null) {
            ae = new AffiliationExposure(twoModePrimaryValue,twoModePrimaryColumn);
        }

        Graph graph = gm.getGraphVisible();

        Table nodeTable = gm.getNodeTable();

        if (ne != null) {
            network_exposure_column = nodeTable.getColumn(NET_EXPOSURE);
        }

        if (ae != null) {
            affiliation_exposure_column = nodeTable.getColumn(AFF_EXPOSURE);
        }
        
        if(de != null){
            decomposition_exposure_column = nodeTable.getColumn("shared_" +DECOMP_EXPOSURE);
            u_decomposition_exposure_column = nodeTable.getColumn("unshared_"+DECOMP_EXPOSURE);
        }

        if (network_exposure_column == null && ne != null) {
            nodeTable.addColumn(NET_EXPOSURE, Double.class);
            //nodeTable.addColumn(NET_EXPOSURE, "Network Exposure Value", Double.class, 0);
        }

        if (affiliation_exposure_column == null && ae != null) {

            nodeTable.addColumn(AFF_EXPOSURE, Double.class);
        }
        
        if(decomposition_exposure_column == null && de !=null){
            nodeTable.addColumn("shared_" + DECOMP_EXPOSURE, Double.class);
        }
        
        if(u_decomposition_exposure_column == null && de !=null){
            nodeTable.addColumn("unshared_" +DECOMP_EXPOSURE, Double.class);
        }

        graph.readLock();
        System.out.println("locking graph..");

        if (ne != null) {
            ne.computeExposure(graph, this.network_exposure_attribute, NET_EXPOSURE);
        }

        if (ae != null) {
            //calculate affiliation exposure
            //ae.initTo2Mode(graph);
            if(calculateOffDiagonal){
                ae.setupOffDiagonalColumn(gm.getEdgeTable());
            }
            
            ae.computeExposure(graph, this.affiliation_exposure_attribute, AFF_EXPOSURE);
        }
        
        if(de != null){
            de.computeExposure(graph, this.decomposition_exposure_attribute, DECOMP_EXPOSURE);
        }

        graph.readUnlock();
        System.out.println("unlocking graph...");
        
        if(calculateOffDiagonal==true) graph.addAllEdges(ae.getOffValueDiagonals());

    }

    @Override
    public String getReport() {
        
        
        String report = "<HTML> <BODY> <h1>NET-EXPO REPORT</h1> "
                + "<hr>"
                + "<br>"
                + "<h2>Network Exposure Model</h2>"
                + Reporter.getInstance().printNetworkExposureParameters()
                + "<h2>Affiliation Exposure Model</h2>"
                + Reporter.getInstance().printAffiliationExposureParameters()
                + "<h2>Decomposed Exposure Model</h2>"
                + Reporter.getInstance().printDecomposedExposureParameters()
                + "<h2>References</h2>"
                + "<h3>Network Exposure Model</h3>"
                + "<p>Valente, T.W.: Network models of the diffusion of innovations. Computational & Mathematical Organization Theory 2(2) (1996) 163–164</p>"
                + "<p>Valente, T.W.: Network models and methods for studying the diffusion of innovations. Models and methods in social network analysis 28 (2005) 98</p>"
                + "<h3>Affiliation Exposure Model</h3>"
                + "<p>Fujimoto, K., Chou, C.P., Valente, T.W.: The Network Autocorrelation Model using Two-mode Data: Affiliation Exposure and Potential Bias in the Autocorrelation Parameter. Social networks 33(3) (July 2011) 231–243</p>"
                + "<p>Fujimoto, K., Unger, J.B., Valente, T.W.: A network method of measuring affiliation-based peer influence: Assessing the influences of teammates’ smoking on adolescent smoking. Child Development 83(2) (2012) 442–451</p>"
                + "<h3>Decomposed Exposure Model</h3>"
                + "<p>Fujimoto,K.,Wang,P.,Valente,T.W.:The decomposed affiliation exposure model: A network approach to segregating peer influences from crowds and organized sports. Network Science 1(2) (2013) 154–169</p>"
                + "<br />"
                + "</BODY></HTML>";
        return report;
    }

    public Column[] getNodeAttributes() {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Table nodeTable = graphController.getGraphModel().getNodeTable();

        return nodeTable.toArray();
    }
    
    public Column[] getEdgeAttributes(){
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        Table edgeTable = graphController.getGraphModel().getEdgeTable();
        
        return edgeTable.toArray();
    }

    public Graph getGraph() {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        return graphController.getGraphModel().getGraphVisible();
    }


    void setTwoModeConfig(String twoModeColumn, String twoModeValue) {
        this.twoModePrimaryValue = twoModeValue;
        this.twoModePrimaryColumn = twoModeColumn;
    }
    
    void setDecomposedMatrices(String c_column, String c1_variable, String c2_variable){
        this.c_column = c_column;
        this.c1_one_mode_attribute = c1_variable;
        this.c2_one_mode_attribute = c2_variable;
    }

    public void setUserDefinedColumns(String userNetExpo, String userAffExpo, String userDecompExpo){
        
        if(userAffExpo != null) this.AFF_EXPOSURE = userAffExpo;
        
        if(userNetExpo !=null )this.NET_EXPOSURE = userNetExpo;
        
        if(userDecompExpo !=null)this.DECOMP_EXPOSURE = userDecompExpo;
    }
    
    public void setUserOffDiagonalChoice(boolean value){
        this.calculateOffDiagonal = value;
    }
    

}
