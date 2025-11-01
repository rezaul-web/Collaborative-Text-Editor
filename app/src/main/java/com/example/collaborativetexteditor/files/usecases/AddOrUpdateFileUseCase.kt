package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class AddOrUpdateFileUseCase @Inject constructor(
    private val repository: DocRepository
) {
    suspend operator fun invoke(docId: String, ownerUid: String, file: DocFile): Boolean {
        return repository.addOrUpdateFile(docId, ownerUid, file)
    }
}
