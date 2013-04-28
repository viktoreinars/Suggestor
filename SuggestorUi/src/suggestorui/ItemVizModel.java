/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import webclient.Item;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemVizModel<T extends Item>
{
    private ItemGraph<T> graph;
    private Map<String, T> recommendations; 
        
    public ItemVizModel()
    {
        recommendations = User.getCurrent().getRecommendations();
        graph = new ItemGraph<>(recommendations);
        this.initGraph();
    }
    
    private void initGraph()
    {
        String graphInitStyle = String.format("url('%s')", Configuration.getValue("item.style"));
        graph.addAttribute("ui.stylesheet", graphInitStyle);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        graph.setAutoCreate(true);
        graph.setStrict(false);
    }
        
    public ItemGraph<T> getGraph()
    {
        return this.graph;
    }
    
    public void updateGraph(String attKey)
    {
        this.updateGraph(attKey, false);
    }
    
    public void updateGraph(String attKey, boolean force)
    {
        if(force)
        {
            graph.clear();
            this.initGraph();
            this.graph.setRecommendations(recommendations);
        }
        graph.buildFromItems(attKey);
    }
    
    public void updateGraph(String attKey, Map<String, T> recommendations)
    {
        this.recommendations = recommendations;
        try {
            graph.clear();
            //System.gc();
            //Thread.sleep(1000);
            this.initGraph();
            this.graph.setRecommendations(recommendations);
            graph.buildFromItems(attKey);
        } catch (Exception ex) {
            Logger.getLogger(ItemVizModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectItem(String itemId)
    {
        this.graph.setSelectedNode(itemId);
    }
    
}
