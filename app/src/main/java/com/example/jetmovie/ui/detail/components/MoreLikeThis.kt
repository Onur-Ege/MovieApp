package com.example.jetmovie.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.jetmovie.movie.Movie
import com.example.jetmovie.ui.home.components.MovieCoverImage
import com.example.jetmovie.ui.sign_in.UserData

@Composable
fun MoreLikeThis(
    userId:String,
    tmdbId:Int,
    modifier: Modifier = Modifier,
    fetchRecs: (Int) -> Unit,
    isMovieLoading: Boolean,
    recommendations: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavClick: (String, Movie, Int) -> Unit
) {
    LaunchedEffect(key1 = true) {
        fetchRecs(tmdbId)
    }
    Column(modifier) {
        Text(
            text = "More like this",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        LazyRow {
            item {
                AnimatedVisibility(visible = isMovieLoading) {
                    CircularProgressIndicator()
                }
            }
            items(recommendations) { rec ->
                MovieCoverImage(
                    userId=userId,
                    movie = rec,
                    onMovieClick = onMovieClick,
                    onFavClick = onFavClick)
            }
        }
    }
}
