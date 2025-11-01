package com.example.collaborativetexteditor.files.usecases

import com.example.collaborativetexteditor.files.data.repository.DocRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

// A result wrapper for better feedback
sealed class AddCollaboratorResult {
    data object Success : AddCollaboratorResult()
    data object UserNotFound : AddCollaboratorResult()
    data object IsOwner : AddCollaboratorResult()
    data class Failure(val message: String) : AddCollaboratorResult()
}

class AddCollaboratorUseCase @Inject constructor(
    private val repository: DocRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(fileId: String, email: String): AddCollaboratorResult {
        try {
            val currentUser = firebaseAuth.currentUser ?: return AddCollaboratorResult.Failure("Not logged in")
            
            val targetUserUid = repository.findUserByEmail(email)
                ?: return AddCollaboratorResult.UserNotFound

            if (targetUserUid == currentUser.uid) {
                return AddCollaboratorResult.IsOwner
            }

            // You already have this method in your repository
            val success = repository.addCollaborator(fileId = fileId, userUid = targetUserUid)
            
            return if (success) {
                AddCollaboratorResult.Success
            } else {
                AddCollaboratorResult.Failure("Could not add collaborator")
            }
            
        } catch (e: Exception) {
            return AddCollaboratorResult.Failure(e.message ?: "Unknown error")
        }
    }
}