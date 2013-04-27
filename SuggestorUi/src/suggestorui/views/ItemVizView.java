/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
import suggestorui.Configuration;
import suggestorui.ItemGraph;
import suggestorui.ItemVizModel;
import webclient.MovieItem;
import webclient.User;


/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ItemVizView extends JFrame implements ViewerListener, ComponentListener
{
    private ItemVizModel model;
    private boolean loop = true;
    private RightView rightView;
    private WebBreadcrumb breadcrumbs;
    private ViewerPipe pipe;
    private View graphView;
    private Date lastClickTime = new Date();
    
    public ItemVizView(ItemVizModel model)
    {
        super(Configuration.getValue("application.title"));
        this.model = model;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUi();
    }

    private void initUi() 
    {
        this.rightView = new RightView();
        this.getContentPane().setLayout(new BorderLayout());
        this.initMiddleView();
        this.getContentPane().add(new LeftView(), BorderLayout.WEST);
        this.getContentPane().add(rightView, BorderLayout.EAST);
    }
    
    private void initMiddleView()
    {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        
        this.initGraphViz(middlePanel);
        
        breadcrumbs = new WebBreadcrumb ( true );
        breadcrumbs.add(new WebBreadcrumbToggleButton("Overall 0"));
        breadcrumbs.add(new WebBreadcrumbToggleButton("Overall 1"));
        breadcrumbs.add(new WebBreadcrumbToggleButton("Overall 2"));
        middlePanel.add(breadcrumbs, BorderLayout.NORTH);
        
        WebPanel bottomPanel = new WebPanel();
        bottomPanel.setUndecorated(false);
        WebLabel info = new WebLabel(String.format("<html><b>Total Recommended Movies: </b>%s &#08;          "
                                                 + "<b>Top Similar Users: </b>%s &#08;</html> ", 
                                                Configuration.getValue("nRecommendedItems"), Configuration.getValue("nFirstUsers")));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(info);
        bottomPanel.setMargin(3);
        middlePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        this.getContentPane().add(middlePanel, BorderLayout.CENTER);
    }
    
    private void initGraphViz(JPanel middlePanel)
    {
        this.model.updateGraph(Configuration.getValue("defaultAttributeKey"));
        ItemGraph graph = model.getGraph();
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewer.enableXYZfeedback(true);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);        
        
        graphView = viewer.addDefaultView(false);
        graphView.addComponentListener(this);
        middlePanel.add(graphView, BorderLayout.CENTER);
    }
    
    public void startPumping()
    {
        while(loop) 
        {
            pipe.pump();
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
        this.model.selectItem(nodeId);
        MovieItem movie = (MovieItem) this.model.getGraph().getSelectedNode().getItem();
        this.rightView.getMovieView().updateView(movie);
        
        Date currentClickTime = new Date();
        long interval = currentClickTime.getTime() - this.lastClickTime.getTime();
        //System.out.println("interval: " + interval);
        if(interval <= 200)
        {
            System.out.println("Awesome!!! - Detected Double Click");
            int nRecommendations = User.getCurrent().getXRecommendations(movie.getItemId()).size();
            this.model.updateGraph(Configuration.getValue("defaultAttributeKey"));
        }
        this.lastClickTime = currentClickTime;
    }

    @Override
    public void buttonReleased(String nodeId) 
    {
    }

    @Override
    public void componentResized(ComponentEvent e) 
    {
        graphView.getCamera().resetView();
    }

    @Override
    public void componentMoved(ComponentEvent e) 
    {
    }

    @Override
    public void componentShown(ComponentEvent e) 
    {
    }

    @Override
    public void componentHidden(ComponentEvent e) 
    {
    }

}
