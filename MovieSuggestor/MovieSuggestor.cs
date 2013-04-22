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

            // Find groups used for recommendations
            foreach (Movie recommendedMovie in recommendedMovies)
            {
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "AgeGroup");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Occupation");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Gender");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Zipcode");

                foreach (User similarUser in recommendedUsers.Keys)
                {
                    var ratingList = similarUser.Rating.Where(rating => rating.MovieId == recommendedMovie.Id);
                    if (ratingList.Count() == 0) continue; // User did not rate this movie
                    
                    recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("AgeGroup", similarUser.AgeGroup));
                    recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("Occupation", similarUser.Occupation));
                    recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("Gender", similarUser.Gender));
                    recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("Zipcode", similarUser.Zipcode));
                    /* Dont need to count
                    // Age groups                    
                    if (!(ageGroups.Keys.Contains(similarUser.AgeGroup))) ageGroups.Add(similarUser.AgeGroup, 1);
                    else ageGroups[similarUser.AgeGroup] += 1;

                    // Occupations
                    if (!(occupationGroups.Keys.Contains(similarUser.Occupation))) occupationGroups.Add(similarUser.Occupation, 1);
                    else occupationGroups[similarUser.Occupation] += 1;

                    if (!(genderGroups.Keys.Contains(similarUser.Gender))) genderGroups.Add(similarUser.Gender, 1);
                    else genderGroups[similarUser.Gender] += 1;
                    */
                }

                /* Dont need counting of groups - handled on server side
                foreach (string ageKey in ageGroups.Keys)
                {
                    recommendedMovie.Attributes.Add(new KeyValuePair<string,string>("AgeGroup", ageGroups[ageKey]
                }*/
            }

            return recommendedMovies;
        }

        public List<Movie> GetExtendedMovieRecommendations(int movieId)
        {

        }
    }
}
