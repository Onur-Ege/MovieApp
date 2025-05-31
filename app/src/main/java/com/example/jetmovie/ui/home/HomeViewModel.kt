package com.example.jetmovie.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetmovie.movie.Movie
import com.example.jetmovie.movie.MovieRepository
import com.example.jetmovie.ui.sign_in.UserData
import com.example.jetmovie.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _userId = mutableStateOf<String?>(null)
    val userId: String? get() = _userId.value

    fun setUserData(userId: String) {
        _userId.value = userId
    }

    fun fetchFavsAndRecs(){
        fetchFavoriteMovies(userId?:"")
        fetchRecommendations(userId?:"")
    }

    init {
        fetchDiscoverMovie()
    }
    init {
        fetchTrendingMovie()
    }
    init {
        fetchUpcomingMovie()
    }

    fun clearSearchAndRestore(userId: String) {
        _homeState.update {
            it.copy(searchResults = emptyList())
        }
        fetchDiscoverMovie()
        fetchTrendingMovie()
        fetchFavoriteMovies(userId)
    }

    fun searchMovies(query: String) = viewModelScope.launch {
        repository.searchMovies(query).collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, searchResults = movie)
            }
        }
    }

    private fun fetchDiscoverMovie() = viewModelScope.launch {
        repository.fetchDiscoverMovie().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, discoverMovies = movie)
            }
        }
    }

    private fun fetchTrendingMovie() = viewModelScope.launch {
        repository.fetchTrendingMovie().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, trendingMovies = movie)
            }
        }
    }

    private fun fetchUpcomingMovie() = viewModelScope.launch {
        repository.fetchUpcomingMovie().collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, upcomingMovies = movie)
            }
        }
    }

    private fun fetchFavoriteMovies(userId: String) = viewModelScope.launch {
        repository.fetchFavouritesMovie(userId).collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ){ movie ->
            _homeState.update {
                it.copy(isLoading = false, error = null, favoriteMovies = movie)
            }
        }
    }

    private fun fetchRecommendations(userId: String) = viewModelScope.launch {
        repository.fetchRecommendations(userId).collectAndHandle(
            onError = { error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ){ movie ->
            Log.e("recommendations",movie.toString())
            _homeState.update {
                it.copy(isLoading = false, error = null, recommendations = movie)
            }
        }
    }

    fun rateCurrentMovie(userId:String, movie: Movie, rating: Int) = viewModelScope.launch {
        repository.rateMovie(userId=userId,movie=movie, rating = rating).collectAndHandle (
            onError = {error ->
                _homeState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _homeState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ){ success ->
            if (success){
                fetchFavoriteMovies(userId)
                _homeState.update {
                    it.copy(isLoading = false, error = null)
                }
            }
        }
    }
}

data class HomeState(
    val searchResults: List<Movie> = emptyList(),
    val discoverMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val favoriteMovies: List<Movie> = emptyList(),
    val recommendations: List<Movie> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)