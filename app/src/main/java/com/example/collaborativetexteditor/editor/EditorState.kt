package com.example.collaborativetexteditor.editor

import com.example.collaborativetexteditor.utils.helpers.UiError
import com.mohamedrejeb.richeditor.model.RichTextState

data class EditorState(
    val richTextState: RichTextState= RichTextState(),
    var title: String = "",
    val error: UiError?=null,
    val isLoading: Boolean=false
)
