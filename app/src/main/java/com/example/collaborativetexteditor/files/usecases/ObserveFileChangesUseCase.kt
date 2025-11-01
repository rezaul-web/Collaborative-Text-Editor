package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.data.repository.DocRepository
import javax.inject.Inject

class ObserveFileChangesUseCase @Inject constructor(
    private val repository: DocRepository
) {
    operator fun invoke(fileId: String, onChange: (DocFile?) -> Unit) {
        repository.observeFileChanges(fileId, onChange)
    }
}

class RemoveFileListenerUseCase @Inject constructor(
    private val repository: DocRepository
) {
    operator fun invoke(fileId: String) {
        repository.removeFileListener(fileId)
    }
}
