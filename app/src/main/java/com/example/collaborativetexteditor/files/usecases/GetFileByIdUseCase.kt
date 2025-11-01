package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class GetFileByIdUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(fileId: String): DocFile? {
        return repository.getFileById(fileId)
    }
}
