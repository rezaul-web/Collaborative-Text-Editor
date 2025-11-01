package com.example.collaborativetexteditor.files.data.repository

import com.example.collaborativetexteditor.files.data.model.DocFile


    interface DocRepository {
        suspend fun addOrUpdateFile(docId: String, ownerUid: String, file: DocFile): Boolean
        suspend fun addCollaborator(fileId: String, userUid: String): Boolean
        suspend fun getUserFiles(userUid: String): List<DocFile>
        suspend fun getSharedFiles(userUid: String): List<DocFile>
        suspend fun getFileById(fileId: String): DocFile?
        suspend fun removeCollaborator(fileId: String, userUid: String): Boolean
        fun observeFileChanges(fileId: String, onChange: (DocFile?) -> Unit)
        fun removeFileListener(fileId: String)
        suspend fun updateFileContent(docId: String, content: String): Boolean
        suspend fun updateFileTitle(docId: String, title: String): Boolean
        suspend fun deleteFile(fileId: String): Boolean
    }


