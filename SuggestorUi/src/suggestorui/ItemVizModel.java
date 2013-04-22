/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.List;
import java.util.Map;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
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
        String graphInitStyle = String.format("url('%s')", Configuration.getValue("item.style"));
        graph = new SingleGraph("Movie Recommender");
        graph.addAttribute("ui.stylesheet", graphInitStyle);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        graph.setAutoCreate(true);
        graph.setStrict(false);
    }
    
    public boolean buildGraph()
    {
        Map<String, Item> recommendedItems = User.getCurrent().getRecommendations();
        if(recommendedItems.isEmpty())
        {
            return false;
        }
        Item center = recommendedItems.values().toArray(new Item[0])[0];
        for(String itemid : recommendedItems.keySet())
        {
            //temporary code right here --- real one coming soon
            Item sibling = recommendedItems.get(itemid);
            if(!sibling.getItemId().equals(center.getItemId()))
            {
                this.createEdge(center, sibling);
            }
        }
        if(recommendedItems.size() == 1)
        {
            graph.addNode(center.getItemId());
        }
        
        this.updateNodeStyles(recommendedItems);
        
        return true;
    }
    
    private void createEdge(Item item1, Item item2)
    {
        String edgeKey = item1.getItemId() + "-" + item2.getItemId();
        graph.addEdge(edgeKey, item1.getItemId(), item2.getItemId());
    }
    
    public Graph getGraph()
    {
        return this.graph;
    }

    private void updateNodeStyles(Map<String, Item> items) 
    {
        for(Node node : this.graph.getEachNode())
        {
            Styleable item = (Styleable)items.get(node.getId());
            node.addAttribute("ui.label", item.getUiLabel());
            node.addAttribute("ui.style", item.getStyle());
        }
    }    
}
