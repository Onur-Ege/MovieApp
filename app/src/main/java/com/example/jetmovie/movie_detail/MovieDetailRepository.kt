package com.example.jetmovie.movie_detail

import com.example.jetmovie.movie.Movie
import com.example.jetmovie.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
    fun rateMovie(userId:String, movie: MovieDetail, rating: Int): Flow<Response<Boolean>>
    fun rateMovie2(userId:String, movie: Movie, rating: Int): Flow<Response<Boolean>>
    fun getRating(userId: String, tmdbId: Int): Flow<Response<Int>>
    fun fetchRecommendations(tmdbId: Int): Flow<Response<List<Movie>>>
}