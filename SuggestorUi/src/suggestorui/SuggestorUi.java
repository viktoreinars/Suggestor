/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suggestorui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import webclient.AddBasketItemMessage;
import webclient.GetBasketMessage;
import webclient.GetRandomItemMessage;
import webclient.GetRecommendationMessage;
import webclient.InvoiceItem;
import webclient.ResetBasketMessage;
import webclient.SuggestorClient;
import webclient.SuggestorClient.SuggestorItemListResponse;
import webclient.SuggestorClient.SuggestorValueResponse;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class SuggestorUi 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        
        
        AddBasketItemMessage message = new AddBasketItemMessage("1996-S", 3);
        //GetBasketMessage message = new GetBasketMessage();
        //GetRecommendationMessage message = new GetRecommendationMessage(5);
        //ResetBasketMessage message = new ResetBasketMessage();
        //GetRandomItemMessage message = new GetRandomItemMessage(6);
        //SuggestorItemListResponse<InvoiceItem> response = (SuggestorItemListResponse<InvoiceItem>) SuggestorClient.getCurent().sendMessage(message);
        SuggestorValueResponse response = (SuggestorValueResponse) SuggestorClient.getCurent().sendMessage(message);
        if(response.hasError())
        {
            System.out.println(response.getErrorMessage());
        }
        else
        {
            System.out.println("Response Message: " + response.getValue());
//            System.out.println("Items Retrieved: " + response.getItems().size());
//            for(InvoiceItem item : response.getItems())
//            {
//                System.out.println(item.getItemId());
//            }
        }
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost("http://localhost:59274/NAVSuggestorService.asmx/AddBasketItem");
//        try 
//        {
//          List<NameValuePair> nameValuePairs = new ArrayList<>();
//          nameValuePairs.add(new BasicNameValuePair("ItemId", "1996-S"));
//          nameValuePairs.add(new BasicNameValuePair("quantity", "1"));
//
//          post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//          HttpResponse response = client.execute(post);
//          BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//          String line = "";
//          while ((line = rd.readLine()) != null) 
//          {
//            System.out.println(line);
//          }
//        } catch (IOException e) {
//        }    
    }
}
