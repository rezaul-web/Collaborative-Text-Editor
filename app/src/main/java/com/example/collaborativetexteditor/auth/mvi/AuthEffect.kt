package com.example.collaborativetexteditor.auth.mvi

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data class ShowToast(val message: String) : AuthEffect
}
