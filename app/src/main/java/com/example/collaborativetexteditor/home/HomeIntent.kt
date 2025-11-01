package com.example.collaborativetexteditor.home

sealed class HomeIntent {
    data object LoadFiles : HomeIntent()
    data object CreateNewFile : HomeIntent()
    data class OpenFile(val fileId: String) : HomeIntent()
    data object Logout : HomeIntent()
}
