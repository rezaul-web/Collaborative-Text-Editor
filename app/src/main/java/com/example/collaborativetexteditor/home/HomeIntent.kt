package com.example.collaborativetexteditor.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit

sealed class HomeIntent {
    data object ToggleBold : HomeIntent()
    data object ToggleItalic : HomeIntent()
    data object ToggleUnderline : HomeIntent()
    data object ToggleStrikethrough : HomeIntent()

    data object AlignLeft : HomeIntent()
    data object AlignCenter : HomeIntent()
    data object AlignRight : HomeIntent()

    data object ToggleBulletedList : HomeIntent()
    data object ToggleNumberedList : HomeIntent()

    data class SetTextColor(val color: Color) : HomeIntent()
    data class OnContentChange(val content: String) : HomeIntent()

    data class SetFontSize(val fontSize: TextUnit) : HomeIntent()
    data class SetFontFamily(val family: FontFamily) : HomeIntent()
}