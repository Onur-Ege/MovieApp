package com.example.jetmovie.ui.detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetmovie.movie.Movie
import com.example.jetmovie.ui.components.LoadingView
import com.example.jetmovie.ui.detail.components.DetailBodyContent
import com.example.jetmovie.ui.detail.components.DetailTopContent

@Composable
fun MovieDetailScreen(
    userId:String,
    modifier: Modifier = Modifier,
    movieDetailViewModel: DetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onActorClick: (Int) -> Unit
) {
    val state by movieDetailViewModel.detailState.collectAsStateWithLifecycle()
    val movieId = state.movieDetail?.id

    LaunchedEffect(movieId) {
        movieId?.let {
            movieDetailViewModel.getRating(userId, it)
        }
    }

    fun rateMovie(id: String, movie: Movie, rating: Int){
        movieDetailViewModel.rateCurrentMovie(id,movie,rating)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(
            state.error != null,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text(
                state.error ?: "unknown",
                color = MaterialTheme.colorScheme.error,
                maxLines = 2
            )
        }
        AnimatedVisibility(visible = !state.isLoading && state.error == null) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val boxHeight = maxHeight
                val topItemHeight = boxHeight * .5f
                val bodyItemHeight = boxHeight * .5f
                state.movieDetail?.let { movieDetail ->
                    Log.e("xxx","1")
                    DetailTopContent(
                        movieDetail = movieDetail,
                        modifier = Modifier
                            .height(topItemHeight)
                            .align(Alignment.TopCenter)
                    )
                    Log.e("xxx","2")
                    DetailBodyContent(
                        userId=userId,
                        rating = state.rating ?: 0,
                        onRatingSelected = { rating ->
                            movieDetailViewModel.rateCurrentMovie(userId, movieDetail, rating)
                        },
                        movieDetail = movieDetail,
                        recommendations = state.recommendations,
                        isMovieLoading = state.isMovieLoading,
                        fetchRecs = movieDetailViewModel::fetchRec,
                        onMovieClick = onMovieClick,
                        onActorClick = onActorClick,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(bodyItemHeight),
                        onFavClick=::rateMovie
                    )
                    Log.e("xxx","3")

                }
            }
        }
        IconButton(onClick = onNavigateUp, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
        }
    }
    LoadingView(isLoading = state.isLoading)
}





