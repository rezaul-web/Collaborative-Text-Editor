package com.example.collaborativetexteditor.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = try {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signup(name: String, email: String, password: String): Result<Unit> = try {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        )?.await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getUserName(): String? {
       return firebaseAuth.currentUser?.displayName.toString()
    }
}