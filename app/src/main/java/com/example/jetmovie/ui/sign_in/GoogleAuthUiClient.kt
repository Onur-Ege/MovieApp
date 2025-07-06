package com.example.jetmovie.ui.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.jetmovie.di.Token
import com.example.jetmovie.utils.K
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e: Exception) {

            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        Log.d("GoogleAuth", "Google ID Token: $googleIdToken")
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            Log.d("GoogleAuth", "User: ${user?.uid}")

            val firebaseIdToken = user?.getIdToken(true)?.await()?.token
            Log.d("FirebaseAuth", "Firebase ID Token: $firebaseIdToken")

            Token.token = firebaseIdToken

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = user.uid,
                        userName = user.displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }catch (e: Exception){
            Log.e("GoogleAuth", "Error signing in: ${e.message}")
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedUsers(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(K.client_id)
                    .build()
            ).setAutoSelectEnabled(true)
            .build()
    }
}