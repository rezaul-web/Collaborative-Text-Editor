package com.example.collaborativetexteditor.utils.helpers

sealed class UiError {
    data class NetworkError(val message: String) : UiError()
    data class SaveError(val message: String) : UiError()
    data class ValidationError(val message: String) : UiError()
    data class UnknownError(val throwable: Throwable?) : UiError()
}
