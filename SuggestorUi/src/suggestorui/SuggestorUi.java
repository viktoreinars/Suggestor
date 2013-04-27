/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import com.alee.laf.WebLookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.graphstream.ui.swingViewer.ViewerListener;
import suggestorui.views.ItemVizView;
import webclient.MovieItem;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class SuggestorUi implements ViewerListener
{
    /**
     * @param args the command line arguments
     */
    
    protected boolean loop = true;
    protected boolean highlighted = false;
    protected ItemGraph<MovieItem> graph = null;
    
    private String[] gender = { "M", "F" };
    private String[] colors = 
    {
        Highlightable.BLUE_HIGHLIGHT,
        Highlightable.RED_HIGHLIGHT,
        Highlightable.YELLOW_HIGHLIGHT,
        Highlightable.GREEN_HIGHLIGHT,
        Highlightable.ORANGE_HIGHLIGHT,
        Highlightable.VIOLET_HIGHLIGHT,
        Highlightable.PINK_HIGHLIGHT
    };
    private int aIter = 0;
    private int cIter = 0;
    
    public static void main(String[] args) 
    {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        try
        {
            UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
            WebLookAndFeel.initializeManagers();
            SuggestorUi suggestorUi = new SuggestorUi();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public SuggestorUi()
    {
        ItemVizModel model = new ItemVizModel();
        ItemVizView view = new ItemVizView(model);
        view.pack();
        view.setVisible(true);
        view.startPumping();
    }

    @Override
    public void viewClosed(String string) 
    {
        loop = false;
    }

    @Override
    public void buttonPushed(String nodeId) 
    {
        String attkey = "Gender";
        String attvalue = gender[aIter];
        String color = colors[cIter];
        if(highlighted)
        {
            graph.restore(attkey, attvalue);
        }
        else
        {
            graph.highlight(attkey, attvalue, color);
        }
        //highlighted = !highlighted;
        aIter = aIter >= gender.length - 1 ? 0 : aIter + 1;
        cIter = cIter >= colors.length - 1 ? 0 : cIter + 1;
        System.out.println(attvalue);
    }

    @Override
    public void buttonReleased(String string) 
    {
    }
}
