package com.example.jetmovie.movie.data.remote.api

import com.example.jetmovie.BuildConfig
import com.example.jetmovie.movie.data.remote.models.MovieDto
import com.example.jetmovie.utils.K
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET(K.MOVIE_ENDPOINT)
    suspend fun fetchDiscoverMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET(K.TRENDING_MOVIE_ENDPOINT)
    suspend fun fetchTrendingMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET("account/{account_id}/favorite/movies")
    suspend fun fetchFavouritesMovie(
        @Path("account_id") accountId:String = BuildConfig.account_id,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("session_id") sessionId: String = BuildConfig.session_id,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @POST("account/{account_id}/favorite")
    suspend fun markAsFavorite(
        @Path("account_id") accountId:String = BuildConfig.account_id,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("session_id") sessionId: String = BuildConfig.session_id,
        @Body request: FavoriteMovieRequest
    ): FavoriteMovieResponse
}

@Serializable
data class FavoriteMovieRequest(
    @SerialName("media_type")
    val media_type: String,
    @SerialName("media_id")
    val media_id: Int,
    @SerialName("favorite")
    val favorite: Boolean
)

@Serializable
data class FavoriteMovieResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val status_code: Int,
    @SerialName("status_message")
    val status_message: String
)