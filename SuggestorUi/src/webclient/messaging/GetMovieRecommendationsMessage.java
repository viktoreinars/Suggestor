/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

/**
 *
 * @author El Zede
 * @ CSDL
 */
public class GetMovieRecommendationsMessage extends SuggestorMessage
{

    public GetMovieRecommendationsMessage(int k)
    {
        this.addParameter("n", new Integer(k).toString());
    }
    
    @Override
    public String getName() 
    {
        return "Get Recommended Movies";
    }

    @Override
    public String getOperation() 
    {
        return "GetRecommendedMovies";
    }

    @Override
    public int getResponseType() 
    {
        return SuggestorMessage.ENTITY_LIST_RESPONSE;
    }
}
