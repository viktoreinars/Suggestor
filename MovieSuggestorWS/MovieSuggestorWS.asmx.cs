using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Xml;
using MovieSuggestor;

namespace MovieSuggestorWS
{
    /// <summary>
    /// Summary description for Service1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {

        [WebMethod]
        public XmlDocument GetUser(int id)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            User user = suggestorInstance.GetUser(id);
            // Every user has 20+ ratings associated with himself
            
            return MovieSerializer.ObjectToXml(user, typeof(User));
        }

        [WebMethod]
        public bool SelectUser(int id)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            suggestorInstance.SelectUser(id);
            return true;
        }

        [WebMethod]
        public XmlDocument GetRecommendedMovies(int n)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            List<Movie> movies = suggestorInstance.RecommendMovies(n);
            return MovieSerializer.ObjectToXml(movies, typeof(List<Movie>));
        }

        [WebMethod]
        public XmlDocument GetMovies()
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            List<Movie> movies = suggestorInstance.GetMovies();
            return MovieSerializer.ObjectToXml(movies, typeof(List<Movie>));
        }

        [WebMethod]
        public XmlDocument GetMovie(int id)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            Movie movies = suggestorInstance.GetMovie(id);
            return MovieSerializer.ObjectToXml(movies, typeof(Movie));
        }

        #region Helper Functions

        private XmlDocument GetError(string errorMessage)
        {
            XmlDocument errorDocument = new XmlDocument();
            errorDocument.LoadXml("<Error>" + errorMessage + "</Error>");
            return errorDocument;
        }

        #endregion
    }
}