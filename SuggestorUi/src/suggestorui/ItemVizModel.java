/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.List;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import webclient.Item;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemVizModel 
{
    
    private Graph graph;
    public ItemVizModel()
    {
        String graphInitStyle = Configuration.getValue("item.style");
        graph = new SingleGraph("Movie Recommender");
        graph.addAttribute("ui.stylesheet", graphInitStyle);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        graph.setAutoCreate(true);
        graph.setStrict(false);
    }
    
    public boolean buildGraph()
    {
        List<Item> recommendedItems = User.getCurrent().getRecommendations();
        if(recommendedItems.isEmpty())
        {
            return false;
        }
        Item center = recommendedItems.get(0);
        for(int i = 1; i < recommendedItems.size(); i++)
        {
            //temporary code right here --- real one coming soon
            Item sibling = recommendedItems.get(i);
            this.createEdge(center, sibling);
        }
        if(recommendedItems.size() == 1)
        {
            graph.addNode(center.getItemId());
        }
        return true;
    }
    
    public void createEdge(Item item1, Item item2)
    {
        String edgeKey = item1.getItemId() + "-" + item2.getItemId();
        graph.addEdge(edgeKey, item1.getItemId(), item2.getItemId());
    }
    
    public Graph getGraph()
    {
        return this.graph;
    }
    
    
}
