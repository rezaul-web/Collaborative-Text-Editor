package com.example.collaborativetexteditor.home

sealed class HomeEffect {
    data class NavigateToEditor(val fileId: String?) : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
    object NavigateToAuth : HomeEffect()
}
