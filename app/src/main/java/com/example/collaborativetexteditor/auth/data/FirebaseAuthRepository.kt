package com.example.collaborativetexteditor.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
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


        firebaseAuth.currentUser?.let { user ->
            val userData = mapOf(
                "name" to name,
                "email" to email,
                "ownedFiles" to emptyMap<String, Boolean>(),
                "sharedFiles" to emptyMap<String, Boolean>()
            )

            firebaseDatabase.getReference("users")
                .child(user.uid)
                .setValue(userData)
                .await()
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getUserName(): String? {
        return firebaseAuth.currentUser?.displayName
    }
}
