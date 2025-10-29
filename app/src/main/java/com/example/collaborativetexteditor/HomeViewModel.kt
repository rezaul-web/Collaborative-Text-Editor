
package com.example.collaborativetexteditor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {


    private val _richTextState = MutableStateFlow(RichTextState())
    val richTextState = _richTextState.asStateFlow()

    fun toggleBold() {
        _richTextState.value.toggleSpanStyle(
            SpanStyle(fontWeight = FontWeight.Bold)
        )
    }

    fun toggleItalic() {
        _richTextState.value.toggleSpanStyle(
            SpanStyle(fontStyle = FontStyle.Italic)
        )
    }

    fun toggleUnderline() {
        _richTextState.value.toggleSpanStyle(
            SpanStyle(textDecoration = TextDecoration.Underline)
        )
    }

    fun setTextColor(color: Color) {

        _richTextState.value.toggleSpanStyle(
            SpanStyle(color = color)
        )
    }


    fun onContentChange(newHtml: String) {
        println("Content updated: $newHtml")
    }
    fun toggleStrikethrough() {
        _richTextState.value.toggleSpanStyle(
            SpanStyle(textDecoration = TextDecoration.LineThrough)
        )
    }

// --- Alignment Functions ---

    fun setTextAlignLeft() {
        _richTextState.value.toggleParagraphStyle(
            ParagraphStyle(textAlign = TextAlign.Left)
        )
    }

    fun setTextAlignCenter() {
        _richTextState.value.toggleParagraphStyle(
            ParagraphStyle(textAlign = TextAlign.Center)
        )
    }

    fun setTextAlignRight() {
        _richTextState.value.toggleParagraphStyle(
            ParagraphStyle(textAlign = TextAlign.Right)
        )
    }

// --- List Functions ---

    fun toggleBulletedList() {
        _richTextState.value.toggleUnorderedList()
    }

    fun toggleNumberedList() {
        _richTextState.value.toggleOrderedList()
    }
}