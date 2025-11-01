package com.example.collaborativetexteditor.files.data.model

data class DocFile(
    val docId: String = "",
    val title: String = "",
    val content: String = "",
    val ownerId: String = "",
    val collaborators: Map<String, Boolean> = emptyMap(),
    val updatedAt: Long = System.currentTimeMillis()
)
