package com.example.collaborativetexteditor.auth.mvi

sealed interface AuthEvent {
    data class Login(val email: String, val password: String) : AuthEvent
    data class SignUp(val name: String, val email: String, val password: String) : AuthEvent
    data object Logout : AuthEvent
    data object ClearError: AuthEvent
}
