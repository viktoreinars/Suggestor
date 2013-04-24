/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webclient.messaging;

/**
 *
 * @author Gabriel Dzodom
 * @ CSDL
 */
public class GetExtendedMovieRecommendationsMessage extends GetMovieRecommendationsMessage
{
    public GetExtendedMovieRecommendationsMessage(String movieId, int nFirstUsers, int nSecondUsers, int kTopSecondUsers, int k)
    {
        super(k, nFirstUsers);
        this.addParameter("movieId", movieId);
        this.addParameter("secondaryUserN", new Integer(nSecondUsers).toString());
        this.addParameter("topSecondaryUsersN", new Integer(kTopSecondUsers).toString());
    }

    @Override
    public String getOperation() 
    {
        return "GetExtendedMovieRecommendations";
    }
    
}
