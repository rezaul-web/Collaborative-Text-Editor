package com.example.collaborativetexteditor.auth.mvi

data class AuthState(
    val isLoading: Boolean = false,
    val userEmail: String? = null,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)