/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import org.graphstream.graph.implementations.SingleNode;
import webclient.Item;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemNode<T extends Item> extends SingleNode implements Highlightable
{
    private T item;
    
    public ItemNode(ItemGraph<T> graph, T item)
    {
        super(graph, item.getItemId());
        this.item = item;
    }

    public void updateStyle()
    {
        Styleable stylable = (Styleable)this.getItem();
        this.addAttribute("ui.label", stylable.getUiLabel());
        this.addAttribute("ui.style", stylable.getStyle());
    }
    
    /**
     * @return the item
     */
    public T getItem() 
    {
        return item;
    }

    @Override
    public void highlight(String highlightClass) 
    {
        String currentClass = this.getAttribute("ui.class") == null ? "" : this.getAttribute("ui.class").toString();
        Highlightable.previousClass.put(this.getId(), currentClass);
        this.addAttribute("ui.class", highlightClass);
    }

    @Override
    public void restore() 
    {
        this.removeAttribute("ui.class");
        String prevClass = Highlightable.previousClass.get(this.getId());
        prevClass = "".equals(prevClass) ? Configuration.getValue("nodeBaseClass") : prevClass;
        this.setAttribute("ui.class", prevClass);
        this.updateStyle();
    }
}
