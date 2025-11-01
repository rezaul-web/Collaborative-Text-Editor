package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class UpdateFileTitleUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(docId: String, title: String) {
        repository.updateFileTitle(docId, title)
    }
}