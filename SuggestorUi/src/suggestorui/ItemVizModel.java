/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.Map;
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
        
    public ItemVizModel()
    {
        Map<String, T> recommendations = User.getCurrent().getRecommendations();
        graph = new ItemGraph<>(recommendations);
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
        graph.buildFromItems(attKey);
    }
    
    public void selectItem(String itemId)
    {
        this.graph.setSelectedNode(itemId);
    }
    
    
}
