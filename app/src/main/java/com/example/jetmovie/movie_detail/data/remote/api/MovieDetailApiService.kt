package com.example.jetmovie.movie_detail.data.remote.api

import com.example.jetmovie.BuildConfig
import com.example.jetmovie.movie.data.remote.models.MovieDto
import com.example.jetmovie.movie_detail.data.remote.models.MovieDetailDto
import com.example.jetmovie.utils.K
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val MOVIE_ID = "movie_id"

interface MovieDetailApiService {

    @GET("${K.MOVIE_DETAIL_ENDPOINT}/{$MOVIE_ID}")
    suspend fun fetchMovieDetail(
        @Path(MOVIE_ID) movieId:Int,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("append_to_response") appendToResponse: String = "credits,reviews"
    ):MovieDetailDto

    @GET(K.MOVIE_ENDPOINT)
    suspend fun fetchMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @POST("account/{account_id}/favorite")
    suspend fun markAsFavorite(
        @Path("account_id") accountId:String = BuildConfig.account_id,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("session_id") sessionId: String = BuildConfig.session_id,
        @Body request: FavoriteMovieDetailRequest
    ): FavoriteMovieDetailResponse
}

@Serializable
data class FavoriteMovieDetailRequest(
    @SerialName("media_type")
    val media_type: String,
    @SerialName("media_id")
    val media_id: Int,
    @SerialName("favorite")
    val favorite: Boolean
)
@Serializable
data class FavoriteMovieDetailResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val status_code: Int,
    @SerialName("status_message")
    val status_message: String
)