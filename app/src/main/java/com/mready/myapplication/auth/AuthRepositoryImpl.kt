package com.mready.myapplication.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository, FirebaseAuth.AuthStateListener {

    init {
        firebaseAuth.addAuthStateListener(this)
    }

    override val currentUser: MutableStateFlow<FirebaseUser?> =
        MutableStateFlow(firebaseAuth.currentUser)

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()

            val storage = Firebase.storage
            val profilePictureIdx = Random.nextInt(from = 0, until = 6)
            val storageRef = storage.reference.child("profile/profile_$profilePictureIdx.png")

            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setPhotoUri(
                    storageRef.downloadUrl.await()
                ).build()
            )?.await()

            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun googleSignIn(credential: AuthCredential): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        currentUser.update { firebaseAuth.currentUser }
    }

}

suspend fun <T> Task<T>.await(): T {
    return suspendCoroutine { continuation ->
        addOnCompleteListener {
            if (isSuccessful) {
                continuation.resume(result)
            } else {
                continuation.resumeWithException(exception!!)
            }
        }
    }
}