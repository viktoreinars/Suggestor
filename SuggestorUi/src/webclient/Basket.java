/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.List;
import suggestorui.Configuration;
import webclient.SuggestorClient.SuggestorItemListResponse;
import webclient.SuggestorClient.SuggestorValueResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class Basket
{
    private static Basket instance = null;

    private Basket() { }
    
    public static Basket getIntance()
    {
        if(instance == null)
        {
            instance = new Basket();
        }
        return instance;
    }
    
    public void addItem(Item item)
    {
        item.addToBasket();
    }
    
    public void removeItem(Item item)
    {
        item.removeFromBasket();
    }
    
    public <T extends Item> List<T> getItems()
    {
        GetBasketMessage message = new GetBasketMessage();
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
    
    public <T extends Item> List<T> getRecommendations()
    {
        int k = Integer.parseInt(Configuration.getValue("sizeofrecommendeditems"));
        GetRecommendationMessage message = new GetRecommendationMessage(k);
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
    
    public void resetBasket()
    {
        ResetBasketMessage message = new ResetBasketMessage();
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
    }
}
