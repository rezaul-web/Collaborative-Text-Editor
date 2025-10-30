package com.example.collaborativetexteditor.home

import com.example.collaborativetexteditor.utils.helpers.UiError
import com.mohamedrejeb.richeditor.model.RichTextState

data class HomeState(
    val richTextState: RichTextState= RichTextState(),
    val error: UiError?=null,
    val isLoading: Boolean=false
)
