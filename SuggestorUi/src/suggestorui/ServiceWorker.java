/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import webclient.messaging.ServiceEvent;
import webclient.messaging.ServiceEventListener;
import webclient.messaging.SuggestorClient.SuggestorResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class ServiceWorker extends SwingWorker<SuggestorResponse, SuggestorResponse> 
{
    private static ServiceWorker instance = null;
    private List<ServiceEventListener> listeners = new ArrayList<>();
    
    private ServiceWorker()
    {
        
    }
    
    public static ServiceWorker getInstance()
    {
        if(instance == null)
        {
            instance = new ServiceWorker();
        }
        return instance;
    }

    
    
    public void work()
    {
    }
    
    public void addServiceEventListener(ServiceEventListener listener)
    {
        this.listeners.add(listener);
    }
    
    public void removeServiceEventListener(ServiceEventListener listener)
    {
        this.listeners.remove(listener);
    }
    
    public synchronized void fireOnPreCommunicationEvents(ServiceEvent event)
    {
        for(ServiceEventListener listener : listeners)
        {
            listener.onPreCommunication(event);
        }
    }
    
    public synchronized void fireOnPostCommunicationEvents(ServiceEvent event)
    {
        for(ServiceEventListener listener : listeners)
        {
            listener.onPostCommunication(event);
        }
    }


    @Override
    protected SuggestorResponse doInBackground() throws Exception 
    {
        return (SuggestorResponse) new Object();
    }
    
}
