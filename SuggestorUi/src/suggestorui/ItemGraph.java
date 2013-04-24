/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.Map;
import org.graphstream.graph.EdgeFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.SingleGraph;
import webclient.Item;
import webclient.User;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemGraph<T extends Item> extends SingleGraph
{
    
    private ItemNode<T> selectedNode;
    
    public ItemGraph()
    {
        super("Suggestor - Recommendations");
        
        setNodeFactory(new NodeFactory<ItemNode<T>>() {
            @Override
            public ItemNode<T> newInstance(String id, Graph graph) 
            {
                T item = (T) User.getCurrent().getRecommendations().get(id);
                return new ItemNode<>((ItemGraph<T>)graph, item);
            }
        });
        
        setEdgeFactory(new EdgeFactory<ItemEdge<T>>(){
            @Override
            public ItemEdge<T> newInstance(String _id, Node _source, Node _target, boolean directed) 
            {
                ItemNode<T> sourceNode = (ItemNode<T>)_source;
                ItemNode<T> targetNode = (ItemNode<T>)_target;
                return new ItemEdge<>(sourceNode, targetNode);
            }
        });
    }
    
    public boolean buildFromItems()
    {
        Map<String, T> recommendedItems = User.getCurrent().getRecommendations();
        if(recommendedItems.isEmpty())
        {
            return false;
        }
        T center = (T)recommendedItems.values().toArray(new Item[0])[0];
        for(T sibling : recommendedItems.values())
        {
            //temporary code right here --- real one coming soon
            if(!sibling.getItemId().equals(center.getItemId()))
            {
                this.addEdge(center, sibling);
            }
        }
        if(recommendedItems.size() == 1)
        {
            this.addNode(center);
        }
        this.updateNodeStyles();
        return true;
    }
    
    public void highlight(ItemGraph<T> subGraph, String highlightClass)
    {
        for(ItemNode<T> node : subGraph.getEachNodeItem())
        {
            ((Highlightable)this.getNodeItem(node)).highlight(highlightClass);
        }
        for(ItemEdge<T> edge : subGraph.getEachEdgeItem())
        {
            ((Highlightable)this.getEdgeItem(edge)).highlight(highlightClass);
        }
    }
    
    public void restore(ItemGraph<T> subGraph)
    {
        for(ItemNode<T> node : subGraph.getEachNodeItem())
        {
            ((Highlightable)this.getNodeItem(node)).restore();
        }
        for(ItemEdge<T> edge : subGraph.getEachEdgeItem())
        {
            ((Highlightable)this.getEdgeItem(edge)).restore();
        }
    }
    
    public Iterable<ItemNode<T>> getEachNodeItem()
    {
        return (Iterable<ItemNode<T>>) this.getEachNode();
    }
    
    public Iterable<ItemEdge<T>> getEachEdgeItem()
    {
        return (Iterable<ItemEdge<T>>) this.getEachEdge();
    }
    
    public ItemNode<T> getNodeItem(T item)
    {
        return (ItemNode<T>)this.getNode(item.getItemId());
    }
    
    public ItemNode<T> getNodeItem(ItemNode<T> nodeItem)
    {
        return (ItemNode<T>)this.getNodeItem(nodeItem.getItem());
    }
    
    public ItemEdge<T> getEdgeItem(ItemEdge<T> edgeItem)
    {
        return (ItemEdge<T>)this.getEdge(edgeItem.getId());
    }
    
    private void updateNodeStyles() 
    {
        for (ItemNode<T> node : this.getEachNodeItem()) 
        {
            node.updateStyle();
        }
    }    
    
    public ItemNode<T> addNode(T item)
    {
        if(item == null)
        {
            return null;
        }
        
        ItemNode<T> node = new ItemNode<>(this, item);
        this.addNode(item.getItemId());
        return node;
    }

    public ItemEdge<T> addEdge(T source, T target)
    {
        ItemNode<T> sourceNode = this.getNodeItem(source); 
        if(sourceNode == null)
        {
            sourceNode = this.addNode(source);
        }        
        ItemNode<T> targetNode = this.getNodeItem(target);
        if(targetNode == null)
        {
            targetNode = this.addNode(target);
        }
        ItemEdge<T> edge = new ItemEdge<>(sourceNode, targetNode);
        this.addEdge(edge.getId(), sourceNode, targetNode);
        return edge;
    }
    
    
    public ItemNode<T> getSelectedNode()
    {
        return this.selectedNode;
    }
    
    public void setSelectedNode(String id)
    {
        if(this.selectedNode != null)
        {
            ((Highlightable)this.selectedNode).restore();
        }
        this.selectedNode = (ItemNode<T>)this.getNode(id);
        ((Highlightable)this.selectedNode).highlight(Highlightable.DEFAULT_HIGHLIGHTED);
    }
}
