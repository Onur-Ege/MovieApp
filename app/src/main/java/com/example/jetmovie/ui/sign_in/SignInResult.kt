package com.example.jetmovie.ui.sign_in

data class UserData (
    val userId:String,
    val userName:String?,
    val profilePictureUrl:String?
    )

data class SignInResult (
    val data: UserData?,
    val errorMessage:String?
)
