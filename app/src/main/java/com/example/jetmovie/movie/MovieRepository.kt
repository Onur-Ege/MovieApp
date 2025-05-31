package com.example.jetmovie.movie

import com.example.jetmovie.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun fetchDiscoverMovie(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovie(): Flow<Response<List<Movie>>>
    fun fetchUpcomingMovie(): Flow<Response<List<Movie>>>
    fun fetchFavouritesMovie(userId: String): Flow<Response<List<Movie>>>
    fun rateMovie(userId: String, movie: Movie, rating: Int): Flow<Response<Boolean>>
    fun searchMovies(query:String): Flow<Response<List<Movie>>>
    fun fetchRecommendations(userId: String): Flow<Response<List<Movie>>>
}