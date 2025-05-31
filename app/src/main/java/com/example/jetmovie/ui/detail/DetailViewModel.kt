package com.example.jetmovie.ui.detail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmovie.movie.Movie
import com.example.jetmovie.movie_detail.MovieDetail
import com.example.jetmovie.movie_detail.MovieDetailRepository
import com.example.jetmovie.utils.K
import com.example.jetmovie.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState = _detailState.asStateFlow()

    val id: Int = savedStateHandle.get<Int>(K.MOVIE_ID) ?: -1

    init {
        fetchMovieDetailById()
    }

    private fun fetchMovieDetailById() = viewModelScope.launch {
        Log.e("detail","beginning of detail in VM")
        if (id == -1) {
            _detailState.update {
                it.copy(isLoading = false, error = "Movie not found")
            }
        } else {
            repository.fetchMovieDetail(id).collectAndHandle(
                onError = { error ->
                    _detailState.update {
                        it.copy(isLoading = false, error = error?.message)
                    }
                },
                onLoading = {
                    _detailState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            ) { movieDetail ->
                Log.e("detail","detail in VM after calling")
                Log.e("detail",movieDetail.toString())
                _detailState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        movieDetail = movieDetail
                    )
                }
            }
        }
    }

    fun fetchRec(tmdbId: Int) = viewModelScope.launch {
        repository.fetchRecommendations(tmdbId).collectAndHandle(
            onError = { error ->
                _detailState.update {
                    it.copy(isMovieLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(isMovieLoading = true, error = null)
                }
            }
        ) { movies ->
            _detailState.update {
                it.copy(
                    isMovieLoading = false,
                    error = null,
                    recommendations = movies
                )
            }
        }
    }

    fun rateCurrentMovie(userId:String, movie: MovieDetail, rating: Int) = viewModelScope.launch {
        repository.rateMovie(userId=userId,movie=movie,rating=rating).collectAndHandle (
            onError = { error ->
                _detailState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ){success ->
            if (success){
                _detailState.update {
                    it.copy(rating = rating, isLoading = false, error = null)
                }
            }
        }
    }

    fun rateCurrentMovie(userId:String, movie: Movie, rating: Int) = viewModelScope.launch {
        repository.rateMovie2(userId=userId,movie=movie,rating=rating).collectAndHandle (
            onError = { error ->
                _detailState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ){success ->
            if (success){
                _detailState.update {
                    it.copy(rating = rating, isLoading = false, error = null)
                }
            }

        }
    }

    fun getRating(userId: String, tmdbId: Int) = viewModelScope.launch {
        repository.getRating(userId,tmdbId).collectAndHandle(
            onError = { error ->
                _detailState.update {
                    it.copy(isMovieLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(isMovieLoading = true, error = null)
                }
            }
        ) { rating ->
            Log.e("DetailViewModel", rating.toString())
            try {
                _detailState.update {
                    it.copy(rating = rating)
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Failed to load rating", e)
            }
        }
    }
}

data class DetailState(
    val rating: Int? = null,
    val movieDetail: MovieDetail? = null,
    val recommendations: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMovieLoading: Boolean = false
)