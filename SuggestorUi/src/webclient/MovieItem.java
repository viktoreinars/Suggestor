/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import java.util.List;
import java.util.Map;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import suggestorui.Displayable;
import suggestorui.Styleable;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */

@Root(name="Item")
public class MovieItem extends Item implements Displayable, Styleable
{
    @Element(name="Id")
    private String itemid;
    @Element(name="Title")
    private String title;
    @Element(name="Release_Date")
    private String releaseDate;
    @Element(name="Description")
    private String description;
    
    protected MovieItem()
    {
        
    }
    
    @ElementList(name="Attributes")
    private List<MovieAttribute> attributes;

    @Override
    public String getItemId() 
    {
        return this.itemid;
    }

    public String getYear()
    {
        return this.releaseDate.split("-")[2];
    }
    
    
    @Override
    public int getQuantity() 
    {
        return 0;
    }

    @Override
    public String getTitle() 
    {
        return this.title;
    }

    @Override
    public String getDescription() 
    {
        return this.description;
    }

    @Override
    public String getIconPath() 
    {
        return String.format("../Databases/ml-100k/PosterImages/%s.jpg", this.getItemId());
    }

    @Override
    public String getStyle() 
    {
        return String.format("fill-image: url('%s');", this.getIconPath());
    }

    @Override
    public String getUiLabel() 
    {
        return "";
    }
    
}
