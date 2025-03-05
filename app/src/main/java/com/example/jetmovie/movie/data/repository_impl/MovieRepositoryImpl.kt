package com.example.jetmovie.movie.data.repository_impl

import android.util.Log
import com.example.jetmovie.common.data.ApiMapper
import com.example.jetmovie.movie.data.remote.api.FavoriteMovieRequest
import com.example.jetmovie.movie.data.remote.api.MovieApiService
import com.example.jetmovie.movie.data.remote.models.MovieDto
import com.example.jetmovie.movie.domain.models.Movie
import com.example.jetmovie.movie.domain.repository.MovieRepository
import com.example.jetmovie.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MovieRepositoryImpl(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>
) : MovieRepository {


    override fun fetchDiscoverMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = movieApiService.fetchDiscoverMovie()
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }


    override fun fetchTrendingMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = movieApiService.fetchTrendingMovie()
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }


    override fun fetchFavouritesMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = movieApiService.fetchFavouritesMovie()
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }


    override fun markMovieFavorite(movieId: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading())
        val body = FavoriteMovieRequest(media_type = "movie",media_id = movieId, favorite = true)
        val jsonbody= Json.encodeToString(body)
        Log.e("aaa",jsonbody)
        val response = movieApiService.markAsFavorite(request = body)

        if(response.success){
            emit(Response.Success(true))
        }else{
            emit(Response.Error(Exception(response.status_message)))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }
}