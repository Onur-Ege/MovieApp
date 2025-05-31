package com.example.jetmovie.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("/api/tmdb/discover")
    suspend fun fetchDiscoverMovie(): List<Movie>

    @GET("/api/tmdb/popular")
    suspend fun fetchTrendingMovie():  List<Movie>

    @GET("/api/tmdb/upcoming")
    suspend fun fetchUpcomingMovie():  List<Movie>

    @GET("/api/recommendations/user/{userId}")
    suspend fun fetchRecommendations(@Path("userId") userId: String): List<Movie>

    @GET("/api/recommendations/movie/{imdbId}")
    suspend fun fetchRecommendations2(@Path("imdbId") imdbId: String): List<Movie>

    @GET("api/tmdb/search")
    suspend fun searchMovies(@Query("query") query: String): List<Movie>

    @GET("/api/movies/favorites")
    suspend fun fetchFavouritesMovie(
        @Query("userId") userId: String
    ): List<Movie>

    @POST("api/rating")
    suspend fun markAsFavorite(
        @Body request: FavoriteMovieRequest
    ): FavoriteMovieResponse
}

@Serializable
data class FavoriteMovieRequest(
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
data class FavoriteMovieResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val status_code: Int,
    @SerialName("status_message")
    val status_message: String?
)