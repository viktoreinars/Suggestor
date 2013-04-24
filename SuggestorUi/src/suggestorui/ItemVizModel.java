/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import webclient.Item;

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
        graph = new ItemGraph<>();
        String graphInitStyle = String.format("url('%s')", Configuration.getValue("item.style"));
        graph.addAttribute("ui.stylesheet", graphInitStyle);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        graph.setAutoCreate(true);
        graph.setStrict(false);
        
        graph.buildFromItems();
    }
        
    public ItemGraph<T> getGraph()
    {
        return this.graph;
    }
}
