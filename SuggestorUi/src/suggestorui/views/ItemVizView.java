/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
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
import webclient.AttributeCollection;
import webclient.Item;
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
    private int nRecommendations = Integer.parseInt(Configuration.getValue("nRecommendedItems"));
    private Viewer viewer;
    private WebLabel info;
    private Map<String, String> movieStack = new HashMap<>();
    private WebButton clearBreadCrumbs;
    
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
        this.addBreadCrumb("Overall", "-1");
        middlePanel.add(breadcrumbs, BorderLayout.NORTH);
        
        WebPanel bottomPanel = new WebPanel();
        bottomPanel.setUndecorated(false);
        info = new WebLabel(String.format("<html><b>Total Recommended Movies: </b>%d &#08;          "
                                                 + "<b>Top Similar Users: </b>%s &#08;&#08;&#08;</html> ", 
                                                this.nRecommendations, Configuration.getValue("nFirstUsers")));
        
        clearBreadCrumbs = new WebButton("Clear Bread Crumbs");
        clearBreadCrumbs.setEnabled(false);
        clearBreadCrumbs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Component[] crumbs = breadcrumbs.getComponents();
                for(int i = 1; i < crumbs.length; i++)
                {
                    Component c = crumbs[i];
                    breadcrumbs.remove(c);
                }
                ((WebBreadcrumbToggleButton)crumbs[0]).setSelected(true);
                clearBreadCrumbs.setEnabled(false);
                Map<String, MovieItem> recommendations = User.getCurrent().getRecommendations();
                updateGraph(recommendations);
            }
        });
        
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(info, BorderLayout.WEST);
        bottomPanel.add(clearBreadCrumbs, BorderLayout.EAST);
        bottomPanel.setMargin(3);
        middlePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        this.getContentPane().add(middlePanel, BorderLayout.CENTER);
    }
    
    public void addBreadCrumb(String label, String movieId)
    {
        WebBreadcrumbToggleButton button = new WebBreadcrumbToggleButton(label);
        movieStack.put(label, movieId);
        breadcrumbs.add(button);
        if(clearBreadCrumbs != null)
        {
            clearBreadCrumbs.setEnabled(breadcrumbs.getComponentCount() > 1);
        }
        button.setSelected(true);
        for(Component c : breadcrumbs.getComponents())
        {
            if(c instanceof WebBreadcrumbToggleButton && !c.equals(button))
            {
                ((WebBreadcrumbToggleButton)c).setSelected(false);
            }
        }
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                for(Component c : breadcrumbs.getComponents())
                {
                    if(c instanceof WebBreadcrumbToggleButton && !c.equals(e.getSource()))
                    {
                        ((WebBreadcrumbToggleButton)c).setSelected(false);
                    }
                }
                Map<String, MovieItem> recommendations;
                if(e.getActionCommand().toLowerCase().equals("overall"))
                {
                    recommendations = User.getCurrent().getRecommendations();
                }
                else
                {
                    recommendations = User.getCurrent().getXRecommendations(movieStack.get(e.getActionCommand()));
                }
                updateGraph(recommendations);
            }
        });
    }
    
    private void updateBottom()
    {
        info.setText(String.format("<html><b>Total Recommended Movies: </b>%d &#08;          "
                                                 + "<b>Top Similar Users: </b>%s &#08;</html> ", 
                                                this.nRecommendations, Configuration.getValue("nFirstUsers")));
    }
    
    private void initGraphViz(JPanel middlePanel)
    {
        this.model.updateGraph(Configuration.getValue("defaultAttributeKey"));
        ItemGraph graph = model.getGraph();
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
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
        movie.printGenres();
        
        Date currentClickTime = new Date();
        long interval = currentClickTime.getTime() - this.lastClickTime.getTime();
        //System.out.println("interval: " + interval);
        if(interval <= 250)
        {
            System.out.println("Awesome!!! - Detected Double Click");
            Map<String, MovieItem> recommendations = User.getCurrent().getXRecommendations(movie.getItemId());
            
            
            this.updateGraph(recommendations);
            this.addBreadCrumb(movie.getTitle(), movie.getItemId());
        }
        this.lastClickTime = currentClickTime;
    }
    
    private void updateGraph(Map<String, MovieItem> recommendations)
    {
        nRecommendations = recommendations.size();
        viewer.disableAutoLayout();
        this.model.updateGraph(Configuration.getValue("defaultAttributeKey"), recommendations);
        viewer.enableAutoLayout();
        this.updateBottom();
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
