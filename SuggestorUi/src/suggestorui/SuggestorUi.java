/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
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
        SuggestorUi suggestorUi = new SuggestorUi();
    }
    
    public SuggestorUi()
    {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        ItemVizModel model = new ItemVizModel();
        graph = model.getGraph();
        Viewer viewer = graph.display();
        //viewer.disableAutoLayout();
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);        

        while(loop) {
            fromViewer.pump();
        }
        
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
