package com.example.jetmovie

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.jetmovie.ui.sign_in.GoogleAuthUiClient
import com.example.jetmovie.ui.theme.JetMovieTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import com.example.jetmovie.ui.navigation.MovieNavGraph
import com.example.jetmovie.ui.navigation.Route

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
                val isUserSignedIn = remember { googleAuthUiClient.getSignedUsers() != null }
                val startDestination = if (isUserSignedIn) Route.HomeScreen().route else Route.SignInScreen().route

                MovieNavGraph(
                    navController = navController,
                    googleAuthUiClient = googleAuthUiClient,
                    startDestination=startDestination,
                    showToast = {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }
}