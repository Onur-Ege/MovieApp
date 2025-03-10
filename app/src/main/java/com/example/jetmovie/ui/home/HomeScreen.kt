package com.example.jetmovie.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.jetmovie.R
import com.example.jetmovie.ui.components.LoadingView
import com.example.jetmovie.ui.home.components.BodyContent
import com.example.jetmovie.ui.home.components.TopContent
import com.example.jetmovie.ui.sign_in.UserData
import kotlinx.coroutines.delay

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userData: UserData?,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onMovieClick: (id: Int) -> Unit,
    onSignOut: () -> Unit
) {
    val aramaYapiliyorMu = remember { mutableStateOf(false) }
    val tf = remember { mutableStateOf("") }

    var isAutoScrolling by remember {
        mutableStateOf(true)
    }
    val state by homeViewModel.homeState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { state.discoverMovies.size }
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    fun addFavorite(movieId:Int){
        homeViewModel.addMovieToFavorites(movieId)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        if (isDragged) {
            isAutoScrolling = false
        } else {
            isAutoScrolling = true
            delay(5000)
            with(pagerState) {
                val target = if (currentPage < state.discoverMovies.size - 1) currentPage + 1 else 0
                scrollToPage(target)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if(aramaYapiliyorMu.value){
                        TextField(value = tf.value,
                            onValueChange = {tf.value = it},
                            label = { Text(text = "Ara")},
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                unfocusedIndicatorColor = Color.White
                            )
                        )
                    }else{
                        Row {
                            AsyncImage(
                                model = userData?.profilePictureUrl,
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Hi, ${userData?.userName}")
                        }
                    }
                },
                actions = {
                    if(aramaYapiliyorMu.value){
                        IconButton(onClick = {
                            aramaYapiliyorMu.value = false
                            tf.value = ""
                        }) {
                            Icon(painter = painterResource(R.drawable.kapat_resim), contentDescription = "")
                        }
                    } else {
                        Row {
                            IconButton(onClick = {
                                aramaYapiliyorMu.value = true
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.arama_resim),
                                    contentDescription = ""
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onSignOut) {
                                Icon(
                                    painter = painterResource(R.drawable.exit),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue, titleContentColor = Color.White))
        }
    ) { paddingValues ->
        Box(modifier = modifier.padding(paddingValues)) {
            AnimatedVisibility(visible = state.error != null) {
                Text(
                    text = state.error ?: "unknown error",
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2
                )
            }
            AnimatedVisibility(visible = !state.isLoading && state.error == null) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val boxHeight = maxHeight
                    val topItemHeight = boxHeight * .45f
                    val bodyItemHeight = boxHeight * .55f
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(defaultPadding),
                        pageSize = PageSize.Fill,
                        pageSpacing = itemSpacing
                    ) { page ->
                        if (isAutoScrolling) {
                            AnimatedContent(
                                targetState = page,
                                label = "",
                            ) { index ->
                                TopContent(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .heightIn(min = topItemHeight),
                                    movie = state.discoverMovies[index],
                                    onMovieClick = {
                                        onMovieClick(it)
                                    }
                                )
                            }
                        } else {
                            TopContent(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .heightIn(min = topItemHeight),
                                movie = state.discoverMovies[page],
                                onMovieClick = {
                                    onMovieClick(it)
                                }
                            )
                        }
                    }
                    BodyContent(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .heightIn(max = bodyItemHeight),
                        discoverMovies = state.discoverMovies,
                        trendingMovies = state.trendingMovies,
                        favoriteMovies = state.favoriteMovies,
                        onMovieClick = onMovieClick,
                        onFavClick = ::addFavorite
                    )
                }
            }
        }
    }
    LoadingView(isLoading = state.isLoading)
}