package com.mready.myapplication.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthRepository {
    val currentUser: MutableStateFlow<FirebaseUser?>
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun logout()

    suspend fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: () -> Unit)

    suspend fun googleSignIn(credential: AuthCredential): Resource<FirebaseUser>
    //suspend fun googleSignUp(): Resource<FirebaseUser>
}