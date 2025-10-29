package com.example.collaborativetexteditor.auth.data



interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signup(name: String, email: String, password: String): Result<Unit>
    suspend fun logout()
    fun isUserLoggedIn(): Boolean
    fun getUserName(): String?
}


