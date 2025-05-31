package com.example.jetmovie.movie_detail

import android.util.Log
import com.example.jetmovie.movie.Movie
import com.example.jetmovie.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieDetailRepositoryImpl(
    private val movieDetailApiService: MovieDetailApiService
) : MovieDetailRepository {

    override fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>> = flow {
        emit(Response.Loading())
        Log.e("detail","before calling in repo")
        val details = movieDetailApiService.fetchMovieDetail(movieId)
        Log.e("detail","after calling in repo")
        Log.e("detail",details.toString())
        emit(Response.Success(details))
    }.catch { e ->
        e.printStackTrace()
        emit(Response.Error(e))
    }

    override fun fetchRecommendations(tmdbId: Int): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movies = movieDetailApiService.fetchRecommendations(tmdbId)
        emit(Response.Success(movies))
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun rateMovie(userId:String, movie: MovieDetail, rating: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading())
        val body = RatingRequest(userId=userId,tmdbId=movie.id, imdbId =movie.id.toString(), title = movie.title,rating=rating)
        val jsonbody= Json.encodeToString(body)
        Log.e("aaa",jsonbody)
        val response = movieDetailApiService.rateMovie(request = body)
        Log.e("aaa",response.success.toString())
        if(response.success){
            emit(Response.Success(true))
        }else{
            emit(Response.Error(Exception(response.status_message)))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }
    override fun rateMovie2(userId:String, movie: Movie, rating: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading())
        val body = RatingRequest(userId=userId,tmdbId=movie.id, imdbId =movie.id.toString(), title = movie.title,rating=rating)
        val jsonbody= Json.encodeToString(body)
        Log.e("aaa",jsonbody)
        val response = movieDetailApiService.rateMovie(request = body)
        Log.e("aaa",response.success.toString())
        if(response.success){
            emit(Response.Success(true))
        }else{
            emit(Response.Error(Exception(response.status_message)))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun getRating(userId: String, tmdbId: Int): Flow<Response<Int>> = flow {
        emit(Response.Loading())
        val response = movieDetailApiService.getRating(userId,tmdbId)
        emit(Response.Success(response))
    }.catch { e ->
        emit(Response.Error(e))
    }
}