Suggestor
=========

Suggestor allows for visual exploration of movie recommendations and enables the user to 
go down different "recommendation paths" based on the users interest in the movie. 

There are several parts to the project:

1.  Engine
  a.	Suggestor – The recommender engine
2.	Domain-dependent SuggestorEngine implementations
  a.	MovieSuggestor – A project that connects to SQL server that contains the Movielens data and uses the Suggestor recommendations engine to provide recommendations of users/items based on query user/item. 
  b.	MovieSuggestorWS – A web service that encapsulates the functionality of the MovieSuggestor project and provides a communications platform to applications via XML. 
  c.	NAVSuggestor – Provides recommendations of items given user/invoice with data retrieved from an external web service that connects to a Dynamics NAV ERP system.
  d.	NAVSuggestorWS – A web service that encapsulates the functionality of the NAVSuggestor project and provides a communications platform to applications via XML.
3.	Utilities
  a.	ImportMoviePosters – Crawls IMDB and gets the poster and other information for a given movie.
  b.	SuggestorWeb – Web application that consumes the NAVSuggestorWS to display item recommendations from invoice data. This project is a byproduct of earlier system development. 
  c.	TestMovieSuggestor – Runs a 5-fold cross validation of recommendations. 
