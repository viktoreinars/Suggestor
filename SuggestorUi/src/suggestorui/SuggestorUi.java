/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

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
    protected Graph graph = null;
    
    public static void main(String[] args) 
    {
        SuggestorUi suggestorUi = new SuggestorUi();
    }
    
    public SuggestorUi()
    {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        ItemVizModel model = new ItemVizModel();
        model.buildGraph();
        graph = model.getGraph();
        Viewer viewer = graph.display();
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
    public void buttonPushed(String string) 
    {
        System.out.println("clicked");
        graph.getNode(string).addAttribute("ui.style", "shadow-mode: gradient-radial;\n" +
"    shadow-width: 3px;\n" +
"    shadow-color: #FC0; \n" +
"    shadow-offset: 0px;");
    }

    @Override
    public void buttonReleased(String string) 
    {
    }
}
