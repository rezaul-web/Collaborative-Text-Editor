package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class UpdateFileContentUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(docId: String, content: String) {
        repository.updateFileContent(docId, content)
    }
}