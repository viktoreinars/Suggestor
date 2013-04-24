/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public interface Highlightable 
{
    public String DEFAULT_HIGHLIGHTED = "default_highlighted";
    public void highlight(String highlightClass);
    public void restore();
}
