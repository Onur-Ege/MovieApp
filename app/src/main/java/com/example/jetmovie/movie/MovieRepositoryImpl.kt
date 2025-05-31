package com.example.jetmovie.movie

import android.util.Log
import com.example.jetmovie.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieRepositoryImpl(
    private val movieApiService: MovieApiService
) : MovieRepository {


    override fun fetchDiscoverMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.fetchDiscoverMovie()
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun fetchTrendingMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.fetchTrendingMovie()
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun fetchUpcomingMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.fetchUpcomingMovie()
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun fetchRecommendations(userId: String): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.fetchRecommendations(userId)
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun fetchFavouritesMovie(userId: String): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.fetchFavouritesMovie(userId)
        emit(Response.Success(movies))

    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun rateMovie(userId:String, movie: Movie, rating: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading())
        val body = FavoriteMovieRequest(userId=userId, tmdbId = movie.id, imdbId = movie.id.toString(), title = movie.title, rating = rating )
        val jsonbody= Json.encodeToString(body)
        Log.e("aaa",jsonbody)
        val response = movieApiService.markAsFavorite(request = body)
        Log.e("aaa",response.toString())
        if(response.success){
            emit(Response.Success(true))
        }else{
            emit(Response.Error(Exception(response.status_message)))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun searchMovies(query: String): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieApiService.searchMovies(query)
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }
}