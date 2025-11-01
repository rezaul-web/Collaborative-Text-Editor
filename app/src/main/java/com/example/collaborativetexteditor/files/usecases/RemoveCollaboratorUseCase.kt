package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class RemoveCollaboratorUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(fileId: String, userUid: String): Boolean {
        return repository.removeCollaborator(fileId, userUid)
    }
}
