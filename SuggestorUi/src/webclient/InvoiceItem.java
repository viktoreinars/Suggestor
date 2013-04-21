/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

@Root(name="Item")
public class InvoiceItem extends Item
{
    protected InvoiceItem() { }
    
    @Element(name="Id")
    protected String itemid;
    
    @Element(name="Description")
    protected String description;
    
    @Element(name="Quantity")
    protected int quantity;
    

    /**
     * @return the itemid
     */
    @Override
    public String getItemId() {
        return itemid;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the quantity
     */
    @Override
    public int getQuantity() {
        return quantity;
    }

}
