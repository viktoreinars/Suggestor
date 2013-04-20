/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import suggestorui.Configuration;
import webclient.SuggestorClient.SuggestorItemListResponse;
import webclient.SuggestorClient.SuggestorValueResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */


public abstract class Item
{
    
    public Item( )
    {
    }
    
    public void removeFromBasket()
    {
        RemoveBasketItemMessage message = new RemoveBasketItemMessage(this.getItemId(), this.getQuantity());
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
    
    public void addToBasket()
    {
        AddBasketItemMessage message = new AddBasketItemMessage(this.getItemId(), this.getQuantity());
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
    
    public abstract String getItemId();
    public abstract int getQuantity();
    
    public static <T extends Item> List<T> getRandomItems()
    {
        int k = Integer.parseInt(Configuration.getValue("sizeofrandomitems"));
        GetRandomItemMessage message = new GetRandomItemMessage(k);
        SuggestorItemListResponse<T> response = (SuggestorItemListResponse<T>) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
            return new ArrayList<>();
        }
        else
        {
            return response.getItems();
        }
    }
    
    public static <T extends Item> T createFromXml(Class<T> classname, String xml)
    {
        Serializer serializer = new Persister();
        try 
        {
            return serializer.read(classname, xml);
        } catch (Exception ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
