package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class DeleteFileUseCase @Inject constructor(
    private val repository: DocRepository
) {
    /**
     * Deletes a file and all its associated references from the database.
     * @param fileId The ID of the file to delete.
     * @return True if deletion was successful, false otherwise.
     */
    suspend operator fun invoke(fileId: String): Boolean {
        return repository.deleteFile(fileId)
    }
}