package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class GetSharedFilesUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(userUid: String): List<DocFile> {
        return repository.getSharedFiles(userUid)
    }
}
