/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient;

import suggestorui.Displayable;
import suggestorui.Styleable;

/**
 *
 * @author El Zede
 * @ CSDL
 */
public class MovieItem extends Item implements Displayable, Styleable
{

    @Override
    public String getItemId() 
    {
        return "";
    }

    @Override
    public int getQuantity() 
    {
        return 0;
    }

    @Override
    public String getTitle() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
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
