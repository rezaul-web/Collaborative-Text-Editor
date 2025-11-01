package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class AddCollaboratorUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(fileId: String, userUid: String): Boolean {
        return repository.addCollaborator(fileId, userUid)
    }
}
