package com.example.collaborativetexteditor.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    // This is your SINGLE source of truth
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    fun handleIntent(intent: HomeIntent) {
        viewModelScope.launch {
            // Get the state from the MVI state object
            val rich = _homeState.value.richTextState

            when (intent) {

                HomeIntent.ToggleBold -> rich.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                HomeIntent.ToggleItalic -> rich.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                HomeIntent.ToggleUnderline -> rich.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                HomeIntent.ToggleStrikethrough -> rich.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                HomeIntent.AlignLeft -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left))
                HomeIntent.AlignCenter -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                HomeIntent.AlignRight -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right))
                HomeIntent.ToggleBulletedList -> rich.toggleUnorderedList()
                HomeIntent.ToggleNumberedList -> rich.toggleOrderedList()
                is HomeIntent.SetTextColor -> rich.toggleSpanStyle(SpanStyle(color = intent.color))


                is HomeIntent.OnContentChange -> {
                    // repository.updateDocument(intent.content)
                    println("Content changed, would send to network: ${intent.content}")
                    return@launch
                }

                is HomeIntent.SetFontFamily -> rich.toggleSpanStyle(SpanStyle(fontFamily=intent.family))
                is HomeIntent.SetFontSize -> rich.toggleSpanStyle(SpanStyle(fontSize=intent.fontSize))
            }


        }
    }


}