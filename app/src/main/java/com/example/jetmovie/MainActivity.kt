package com.example.jetmovie

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import com.example.jetmovie.ui.home.HomeScreen
import com.example.jetmovie.ui.sign_in.GoogleAuthUiClient
import com.example.jetmovie.ui.sign_in.SignInViewModel
import com.example.jetmovie.ui.theme.JetMovieTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetmovie.ui.detail.MovieDetailScreen
import com.example.jetmovie.ui.navigation.Route
import com.example.jetmovie.ui.sign_in.SignInScreen
import com.example.jetmovie.utils.K


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetMovieTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination=Route.SignInScreen().route){

                    composable(Route.SignInScreen().route){

                        val viewModel = viewModel<SignInViewModel>()
                        val state = viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit) {
                            if(googleAuthUiClient.getSignedUsers() != null){
                                navController.navigate(Route.HomeScreen().route)
                            }
                        }
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )
                        LaunchedEffect(key1 = state.value.isSignInSuccessful) {
                            if(state.value.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign in successful",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Route.HomeScreen().route)
                                viewModel.resetState()
                            }
                        }
                        SignInScreen(
                            state = state.value,
                            onSignInClick = {
                                lifecycleScope.launch {
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
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed out",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.popBackStack()
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
        }
    }
}