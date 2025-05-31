package com.example.jetmovie.ui.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetmovie.ui.detail.MovieDetailScreen
import com.example.jetmovie.ui.home.HomeScreen
import com.example.jetmovie.ui.sign_in.GoogleAuthUiClient
import com.example.jetmovie.ui.sign_in.SignInScreen
import com.example.jetmovie.ui.sign_in.SignInViewModel
import com.example.jetmovie.utils.K
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

@Composable
fun MovieNavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    startDestination: String,
    showToast:  (String) -> Unit
){
    val coroutineScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination=startDestination){

        composable(Route.SignInScreen().route){

            val viewModel = viewModel<SignInViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == android.app.Activity.RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect1(key1 = state.value.isSignInSuccessful) {
                if(state.value.isSignInSuccessful) {
                    showToast("Sign-in successful")
                    navController.navigate(Route.HomeScreen().route){
                        popUpTo(Route.SignInScreen().route) { inclusive = true }
                    }
                    viewModel.resetState()
                }
            }

            SignInScreen(
                state = state.value,
                onSignInClick = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        composable(
            route = Route.HomeScreen().route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            HomeScreen(
                userData = googleAuthUiClient.getSignedUsers(),
                onSignOut =  {
                    navController.navigate(Route.SignInScreen().route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onMovieClick = {
                    navController.navigate(
                        Route.FilmScreen().getRouteWithArgs(id = it)
                    ) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                    }
                }
            )
        }
        composable(
            route = Route.FilmScreen().routeWithArgs,
            arguments = listOf(navArgument(name = K.MOVIE_ID) {
                type = NavType.IntType
            })
        ) {
            MovieDetailScreen(
                userId = googleAuthUiClient.getSignedUsers()?.userId?:"",
                onNavigateUp = {
                    navController.navigateUp()
                },
                onMovieClick = {
                    navController.navigate(
                        Route.FilmScreen().getRouteWithArgs(id = it)
                    ) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                    }
                },
                onActorClick = {}
            )
        }
    }
}