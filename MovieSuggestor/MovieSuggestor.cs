using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace MovieSuggestor
{
    public class MovieSuggestor
    {
        private User currentUser;
        private SuggestorConnector suggestor;        
        
        private static MovieSuggestor singleton;

        private MovieSuggestor() { }

        public static MovieSuggestor GetInstance()
        {
            if (singleton == null)
            {
                singleton = new MovieSuggestor();
                singleton.Initialize();
            }
            return singleton;
        }

        private void Initialize()
        {            
            // Initialize a suggestor instance with TFIDF as the recommender engine.
            // Pre-Calculations are done in constructor
            MovieExtracter.GetInstance().Initialize();
            Dictionary<string, SuggestorItem> movies = MovieExtracter.GetInstance().GetMovies().ToDictionary(x => x.Id.ToString(), x => (SuggestorItem)x);
            //Dictionary<string, SuggestorCollection> ratings = .ToDictionary(x => (x.Key), x => (SuggestorItem)x.Value);
            Dictionary<string, SuggestorUser> users = MovieExtracter.GetInstance().GetUsers().ToDictionary(x => (x.Id.ToString()), x => (SuggestorUser)x);

            suggestor = new SuggestorConnector(null, movies, users, new Suggestor.Algorithms.CollaborativeFiltering(10));
            suggestor.Initialize();
        }

        public void SelectUser(int id)
        {
            currentUser = GetUser(id);
        }

        public User SelectRandomUser()
        {
            User randomUser = MovieExtracter.GetInstance().GetRandomUser();
            currentUser = randomUser;
            return currentUser;
        }

        public List<User> GetUsers()
        {
            return MovieExtracter.GetInstance().GetUsers();
        }

        public User GetUser(int id)
        {
            return MovieExtracter.GetInstance().GetUser(id);
        }

        public List<Movie> GetMovies()
        {
            return MovieExtracter.GetInstance().GetMovies();
        }

        public Movie GetMovie(int id)
        {
            return MovieExtracter.GetInstance().GetMovie(id);
        }

        public User GetCurrentUser()
        {
            return currentUser;
        }

        public List<Movie> RecommendMovies(int n)
        {
            // Get similar users
            Dictionary<SuggestorUser, double> recommendedUsers = suggestor.SuggestUsers(currentUser, n);
            // Aggregate movie score
            Dictionary<Movie, double> topMovies = new Dictionary<Movie, double>();
            foreach (User similarUser in recommendedUsers.Keys)
            {
                foreach (Rating rating in similarUser.Rating)
                {
                    if (!(topMovies.Keys.Contains(rating.Movie)))
                        topMovies.Add(rating.Movie, 0.0);
                    // Formula: R(u,i) = (1/N) * Sum(R(u,i) in SimilarUsers)
                    topMovies[rating.Movie] += ((double)rating.Rating1 / (double)recommendedUsers.Count); 
                }
            }

            Dictionary<Movie, double> sortedDict = (from entry in topMovies orderby entry.Value descending select entry)
                .ToDictionary(pair => pair.Key, pair => pair.Value);

            List<Movie> recommendedMovies = sortedDict.Keys.Take(n).ToList();

            return recommendedMovies;

            //return recommendedUsers.Values.ToList();
        }
    }
}
