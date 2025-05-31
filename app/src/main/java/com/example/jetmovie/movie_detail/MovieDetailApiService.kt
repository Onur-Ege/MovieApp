package com.example.jetmovie.movie_detail


import com.example.jetmovie.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val MOVIE_ID = "movie_id"

interface MovieDetailApiService {

    @GET("api/movies/{movieId}")
    suspend fun fetchMovieDetail(
        @Path("movieId") movieId: Int
    ): MovieDetail

    @GET("/api/recommendations/movie/{tmdbId}")
    suspend fun fetchRecommendations(@Path("tmdbId") tmdbId: Int): List<Movie>

    @POST("api/rating")
    suspend fun rateMovie(
        @Body request: RatingRequest
    ): RatingResponse

    @GET("api/rating")
    suspend fun getRating(
        @Query ("userId") userId: String,
        @Query ("tmdbId") tmdbId: Int
    ): Int
}

@Serializable
data class RatingRequest(
    @SerialName("userId")
    val userId: String,
    @SerialName("tmdbId")
    val tmdbId: Int,
    @SerialName("imdbId")
    val imdbId: String,
    @SerialName("title")
    val title: String,
    @SerialName("rating")
    val rating: Int
)

@Serializable
data class RatingResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val status_code: Int,
    @SerialName("status_message")
    val status_message: String?
)