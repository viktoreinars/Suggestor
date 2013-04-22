/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import suggestorui.Configuration;
import suggestorui.Displayable;
import webclient.messaging.GetMovieRecommendationsMessage;
import webclient.messaging.GetRecommendationMessage;
import webclient.messaging.SuggestorClient;
import webclient.messaging.SuggestorClient.SuggestorItemListResponse;
import webclient.messaging.SuggestorClient.SuggestorUserResponse;
import webclient.messaging.UserMessage;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

@Root(name="Item")
public class User extends Item implements Displayable
{
    private static User current = null;
    
    @Element(name="Id")
    private String itemId;

    @Element(name="Age")
    private int age;

    @Element(name="Occupation")
    private String occupation;
    
    @Element(name="Gender")
    private String gender;
    
    @Element(name="Zipcode")
    private String zipcode;
    
    
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
        return this.itemId;
    }

    @Override
    public int getQuantity() 
    {
        return 1;
    }
    
    public <T extends Item> Map<String, T> getRecommendations()
    {
        int k = Integer.parseInt(Configuration.getValue("sizeofrecommendeditems"));
        GetMovieRecommendationsMessage message = new GetMovieRecommendationsMessage(k);
        SuggestorItemListResponse<T> response = (SuggestorItemListResponse<T>) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
            return new HashMap<>();
        }
        else
        {
            return response.getItems();
        }
    }
    
    
    
    public static User selectUser()
    {
        return User.selectUser(Configuration.getValue("current.user.id"));
    }
    
    public static User selectUser(String userid)
    {
        return OperateOnUser(userid, UserMessage.SELECTUSER);
    }
    
    public static User selectRandomUser()
    {
        return OperateOnUser("", UserMessage.SELECTRANDOMUSER);
    }
    
    
    private static User OperateOnUser(String userid, String operation)
    {
        UserMessage message = new UserMessage(userid, operation);
        SuggestorUserResponse response = (SuggestorUserResponse)SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
            return null;
        }
        else
        {
            return response.getUser();
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
        return String.format("Age: %s\nOccupation: %s\nZipCode: %s", this.getAge(), this.getOccupation(), this.getZipcode());
    }

    @Override
    public String getIconPath() 
    {
        return "user_icon.png";
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }
}
