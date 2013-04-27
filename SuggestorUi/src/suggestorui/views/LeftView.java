/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui.views;

import com.alee.extended.image.WebDecoratedImage;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.WebCollapsiblePane;
import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import suggestorui.Configuration;
import suggestorui.ServiceWorker;
import webclient.AttributeCollection;
import webclient.User;
import webclient.messaging.ServiceEvent;
import webclient.messaging.ServiceEventListener;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class LeftView extends WebComponentPanel implements ComponentListener
{
    public LeftView()
    {
        this.addElement(new UserView());
        this.addElement(new ItemAttributePanel());
        this.addComponentListener(this);
        this.setUndecorated(false);
        this.setDrawSides(false, false, false, false);
    }

    @Override
    public void componentResized(ComponentEvent e) 
    {
        setSize(new Dimension(190, getHeight())); 
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
    
    public class UserView extends WebPanel
    {
        public UserView()
        {
            User currentUser = User.getCurrent();
            WebImage profileImage = new WebImage(currentUser.getIconPath());
            TooltipManager.setTooltip(profileImage, "The Current Selected User", TooltipWay.up);
            
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            this.add(profileImage);
            this.add(new WebLabelPanel("ID: ", currentUser.getItemId()));
            this.add(new WebLabelPanel("Age: ", "" +currentUser.getAge()));
            this.add(new WebLabelPanel("Gender: ", currentUser.getGender()));
            this.add(new WebLabelPanel("Occupation: ", currentUser.getOccupation()));
            this.add(new WebLabelPanel("Zip Code: ", currentUser.getZipcode()));
        }
    }
    
    public class ItemAttributePanel extends WebPanel implements ServiceEventListener, MouseListener
    {
        public ItemAttributePanel()
        {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(Box.createVerticalGlue());
            
            WebLabel label = new WebLabel();
            label.setText(String.format("<html><div style='font-size: 1.2em; font-weight: bold; font-color: rgb(250,250,250);'>%s</div></html>", Configuration.getValue("itemAttributeLabel")));
            WebPanel titlePanel = new WebPanel();
            
            titlePanel.add(label);
            //titlePanel.setUndecorated ( false );
            titlePanel.setMargin ( 6 );
            //titlePanel.setDrawSides(true, true, true, true);
            titlePanel.setDrawBackground(false);
            titlePanel.setBackground(new Color(172,167,147));
            titlePanel.setForeground(new Color(250,250,250));
            
            this.add(titlePanel);
            this.setDrawSides(false, false, false, false);
            ServiceWorker.getInstance().addServiceEventListener(this);
            
            updateUi();
        }
        
        private void updateUi()
        {
            this.removeCollapsible();
            if(!AttributeCollection.isEmpty())
            {
                AttributeCollection.printToScreen();
                for(String attKey : AttributeCollection.getAttributeKeys())
                {
                    String keyName = Configuration.getValue(attKey);
                    int totalcount = 0;
                    WebPanel attValuePanes = new WebPanel();
                    attValuePanes.setLayout(new BoxLayout(attValuePanes, BoxLayout.Y_AXIS));
                    attValuePanes.add(Box.createVerticalGlue());
                    for(String attValue : AttributeCollection.getAttributeValues(attKey))
                    {
                        int count = AttributeCollection.getCluster(attKey, attValue).size();
                        totalcount++;
                        String valueLabel = String.format("%s (%d)", attValue, count);
                        WebAttributePanel panel = new WebAttributePanel(attKey, attValue);
                        panel.setDrawTop(true);
                        panel.setDrawBottom(true);
                        panel.add(new WebLabel(valueLabel));
                        panel.addMouseListener(this);
                        attValuePanes.add(panel);
                    }
                    String title = String.format("%s (%d)", keyName, totalcount);
                    WebCollapsiblePane attKeyPane = new WebCollapsiblePane(title, attValuePanes);
                    attKeyPane.setMargin(4);
                    attKeyPane.setExpanded(true);
                    //attKeyPane.addMouseListener(this);
                    this.add(attKeyPane);
                }
            }
        }
        
        private void removeCollapsible()
        {
            for(Component c : this.getComponents())
            {
                if(c instanceof WebCollapsiblePane)
                {
                   this.remove(c); 
                }
            }
        }

        @Override
        public void onPreCommunication(ServiceEvent event) 
        {
        }

        @Override
        public void onPostCommunication(ServiceEvent event) 
        {
            this.updateUi();
        }

        @Override
        public void mouseClicked(MouseEvent e) 
        {
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
        }

        @Override
        public void mouseReleased(MouseEvent e) 
        {
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
//            System.out.println("I am here");
            Component component = (Component)e.getSource();
            if(component instanceof WebAttributePanel)
            {
                ((WebAttributePanel)component).setBackground(new Color(153,153, 153));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            Component component = (Component)e.getSource();
            if(component instanceof WebAttributePanel)
            {
                ((WebAttributePanel)component).setBackground(Color.white);
            }
        }
        
        public class WebAttributePanel extends WebPanel
        {
            private String attKey;
            private String attValue;
            
            public WebAttributePanel(String attKey, String attValue)
            {
                this.attKey = attKey;
                this.attValue = attValue;
                this.setMargin(3);
            }

            /**
             * @return the attKey
             */
            public String getAttKey() {
                return attKey;
            }

            /**
             */
            public String getAttValue() {
                return attValue;
            }
        }
    }
    
}
