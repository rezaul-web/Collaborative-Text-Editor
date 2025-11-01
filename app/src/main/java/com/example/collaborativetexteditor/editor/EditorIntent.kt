package com.example.collaborativetexteditor.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit

sealed class EditorIntent {
    data class OnTitleChange(val title: String, val fileId: String) : EditorIntent()
    data object ToggleBold : EditorIntent()
    data object ToggleItalic : EditorIntent()
    data object ToggleUnderline : EditorIntent()
    data object ToggleStrikethrough : EditorIntent()

    data object AlignLeft : EditorIntent()
    data object AlignCenter : EditorIntent()
    data object AlignRight : EditorIntent()

    data object ToggleBulletedList : EditorIntent()
    data object ToggleNumberedList : EditorIntent()

    data class SetTextColor(val color: Color) : EditorIntent()
    data class OnContentChange(val content: String,val fileId:String) : EditorIntent()

    data class SetFontSize(val fontSize: TextUnit) : EditorIntent()
    data class SetFontFamily(val family: FontFamily) : EditorIntent()
    data object DeleteFile : EditorIntent()
    data class AddCollaborator(val email: String) : EditorIntent()
}