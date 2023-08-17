package com.mready.myapplication.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun logout()

    suspend fun googleSignIn(credential: AuthCredential): Resource<FirebaseUser>
    //suspend fun googleSignUp(): Resource<FirebaseUser>
}