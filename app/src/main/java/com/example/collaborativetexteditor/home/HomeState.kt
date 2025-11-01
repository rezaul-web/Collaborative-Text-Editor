package com.example.collaborativetexteditor.home

import com.example.collaborativetexteditor.files.data.model.DocFile

data class HomeState(
    val isLoading: Boolean = false,
    val ownedFiles: List<DocFile> = emptyList(),
    val sharedFiles: List<DocFile> = emptyList(),
    val error: String? = null
)
