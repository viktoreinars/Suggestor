﻿using System;
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
        public XmlDocument SelectUser(int id)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            suggestorInstance.SelectUser(id);
            return GetUser(id);
        }

        [WebMethod]
        public XmlDocument SelectRandomUser()
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            User randomUser = suggestorInstance.SelectRandomUser();
            return MovieSerializer.ObjectToXml(randomUser, typeof(User));
        }

        [WebMethod]
        public XmlDocument GetRecommendedMovies(int userN, int n)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            if (suggestorInstance.GetCurrentUser() == null) return GetError("No user selected");
            List<Movie> movies = suggestorInstance.RecommendMovies(userN, n);
            return MovieSerializer.ObjectToXml(movies, typeof(List<Movie>));
        }

        /// <summary>
        /// Gets extended recommendation based on the movie presented. Looks at userN users similar to "us" (current user) and 
        /// finds secondaryUserN secondary similar users based on those. The top topSecondaryUsersN secondary users are then 
        /// used from the list. The movies with the top aggregate score of these top secondary users are then returned.
        /// </summary>
        /// <param name="movieId"></param>
        /// <param name="userN">How many users we want to consider that are similar to the current user</param>
        /// <param name="secondaryUserN">How many users we want to consider that are similar to our similar users (first ones)</param>
        /// <param name="topSecondaryUsersN">The number of the top secondary users to consider for final movie recommendation</param>
        /// <param name="n">How many movie recommendations to return</param>
        /// <returns></returns>
        [WebMethod]
        public XmlDocument GetExtendedMovieRecommendations(int movieId, int userN, int secondaryUserN, int topSecondaryUsersN, int n)
        {
            MovieSuggestor.MovieSuggestor suggestorInstance = MovieSuggestor.MovieSuggestor.GetInstance();
            if (suggestorInstance.GetCurrentUser() == null) return GetError("No user selected");
            List<Movie> movies = suggestorInstance.GetExtendedMovieRecommendations(movieId, userN, secondaryUserN, topSecondaryUsersN, n);
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