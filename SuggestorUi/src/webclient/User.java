/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.List;
import suggestorui.Configuration;
import suggestorui.Displayable;
import webclient.SuggestorClient.SuggestorItemListResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class User extends Item implements Displayable
{

    private static User current = null;
    
    private User()
    {
    }
    
    public static User getCurrent()
    {
        if(current == null)
        {
            current = new User();
        }
        return current;
    }
    
    @Override
    public String getItemId() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getQuantity() 
    {
        return 1;
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

    @Override
    public String getTitle() 
    {
        return this.getItemId();
    }

    @Override
    public String getDescription() 
    {
        return "The current user";
    }

    @Override
    public String getIconPath() 
    {
        return "user_icon.png";
    }
}
