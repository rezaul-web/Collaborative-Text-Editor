package com.example.collaborativetexteditor.editor

sealed interface EditorEffect {
    data object NavigateBack : EditorEffect
    data class ShowError(val message: String) : EditorEffect
}