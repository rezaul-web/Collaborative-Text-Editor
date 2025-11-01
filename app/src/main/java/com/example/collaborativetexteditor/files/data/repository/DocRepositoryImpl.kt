package com.example.collaborativetexteditor.files.data.repository

import android.util.Log
import com.example.collaborativetexteditor.files.data.model.DocFile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : DocRepository {

    private val usersRef = firebaseDatabase.getReference("users")
    private val filesRef = firebaseDatabase.getReference("files")

    override suspend fun addOrUpdateFile(
        docId: String,
        ownerUid: String,
        file: DocFile
    ): Boolean {
        return try {
            val updatedFile = file.copy(
                docId = docId,
                ownerId = ownerUid,
                updatedAt = System.currentTimeMillis()
            )


            filesRef.child(docId).setValue(updatedFile).await()


            usersRef.child(ownerUid)
                .child("ownedFiles")
                .child(docId)
                .setValue(true)
                .await()

            Log.d("Firebase", "File $docId written successfully by $ownerUid")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error writing file: ${e.message}", e)
            false
        }
    }

    override suspend fun addCollaborator(fileId: String, userUid: String): Boolean {
        return try {
            val updates = mutableListOf(
                filesRef.child(fileId)
                    .child("collaborators")
                    .child(userUid)
                    .setValue(true),
                usersRef.child(userUid)
                    .child("sharedFiles")
                    .child(fileId)
                    .setValue(true)
            )

            updates.forEach { it.await() }
            Log.d("Firebase", "Added collaborator $userUid to file $fileId")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error adding collaborator: ${e.message}", e)
            false
        }
    }

    override suspend fun getUserFiles(userUid: String): List<DocFile> {
        val result = mutableListOf<DocFile>()
        try {
            val ownedFilesSnap =
                usersRef.child(userUid).child("ownedFiles").get().await()

            for (fileNode in ownedFilesSnap.children) {
                val fileId = fileNode.key ?: continue
                val fileSnap = filesRef.child(fileId).get().await()
                val file = fileSnap.getValue(DocFile::class.java)
                if (file != null) result.add(file)
            }

            Log.d("Firebase", "Fetched ${result.size} files for user $userUid")
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error fetching user files: ${e.message}", e)
        }
        return result
    }

    override suspend fun getSharedFiles(userUid: String): List<DocFile> {
        val result = mutableListOf<DocFile>()
        try {
            val sharedSnap = usersRef.child(userUid).child("sharedFiles").get().await()
            for (node in sharedSnap.children) {
                val fileId = node.key ?: continue
                val fileSnap = filesRef.child(fileId).get().await()
                val file = fileSnap.getValue(DocFile::class.java)
                if (file != null) result.add(file)
            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error fetching shared files: ${e.message}", e)
        }
        return result
    }

    override suspend fun getFileById(fileId: String): DocFile? {
        return try {
            val snap = filesRef.child(fileId).get().await()
            snap.getValue(DocFile::class.java)
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error fetching file: ${e.message}", e)
            null
        }
    }

    override suspend fun removeCollaborator(fileId: String, userUid: String): Boolean {
        return try {
            filesRef.child(fileId)
                .child("collaborators")
                .child(userUid)
                .removeValue()
                .await()

            usersRef.child(userUid)
                .child("sharedFiles")
                .child(fileId)
                .removeValue()
                .await()

            Log.d("Firebase", "Removed collaborator $userUid from file $fileId")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error removing collaborator: ${e.message}", e)
            false
        }
    }
    private val listeners = mutableMapOf<String, ValueEventListener>()

    override fun observeFileChanges(fileId: String, onChange: (DocFile?) -> Unit) {
        val ref = filesRef.child(fileId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val file = snapshot.getValue(DocFile::class.java)
                onChange(file)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "File listener cancelled: ${error.message}")
            }
        }
        ref.addValueEventListener(listener)
        listeners[fileId] = listener
    }

    override fun removeFileListener(fileId: String) {
        listeners[fileId]?.let {
            filesRef.child(fileId).removeEventListener(it)
            listeners.remove(fileId)
        }
    }
    override suspend fun updateFileContent(docId: String, content: String): Boolean {
        return try {
            // This is the non-destructive part.
            // It updates only the fields in the map, leaving all other data (title, etc.) intact.
            val updates = mapOf(
                "content" to content,
                "updatedAt" to System.currentTimeMillis()
            )

            filesRef.child(docId).updateChildren(updates).await()
            Log.d("Firebase", "File content for $docId updated.")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error updating content: ${e.message}", e)
            false
        }
    }
    override suspend fun updateFileTitle(docId: String, title: String): Boolean {
        return try {
            val updates = mapOf(
                "title" to title,
                "updatedAt" to System.currentTimeMillis()
            )
            // updateChildren is non-destructive
            filesRef.child(docId).updateChildren(updates).await()
            Log.d("Firebase", "File title for $docId updated.")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error updating title: ${e.message}", e)
            false
        }
    }
    override suspend fun deleteFile(fileId: String): Boolean {
        return try {
            // 1. Get the file snapshot to find owner and collaborators
            val fileSnapshot = filesRef.child(fileId).get().await()
            val file = fileSnapshot.getValue(DocFile::class.java)

            if (file == null) {
                Log.w("Firebase", "File $fileId does not exist. Cannot delete.")
                return false
            }

            // 2. Create a map of all paths to delete
            val updates = mutableMapOf<String, Any?>()

            // Path to the main file
            updates["/files/$fileId"] = null

            // Path to the owner's reference
            updates["/users/${file.ownerId}/ownedFiles/$fileId"] = null

            // Paths for all collaborators' references
            file.collaborators.keys.forEach { collaboratorId ->
                updates["/users/$collaboratorId/sharedFiles/$fileId"] = null
            }

            // 3. Execute all deletes as a single atomic operation
            firebaseDatabase.reference.updateChildren(updates).await()

            Log.d("Firebase", "Successfully deleted file $fileId and all references.")
            true
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error deleting file $fileId: ${e.message}", e)
            false
        }
    }
    override suspend fun findUserByEmail(email: String): String? {
        return try {
            // This assumes you store user email in /users/{uid}/email
            // You must have an index on "email" in your Firebase Realtime Database rules for this to be efficient
            val snapshot = usersRef.orderByChild("email").equalTo(email).get().await()

            if (!snapshot.exists() || snapshot.childrenCount.toInt() == 0) {
                Log.w("Firebase", "No user found with email: $email")
                return null
            }

            // Get the first match (emails should be unique)
            val userSnapshot = snapshot.children.first()
            Log.d("Firebase", "Found user ${userSnapshot.key} for email $email")
            userSnapshot.key // This is the user's UID
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error finding user by email: ${e.message}", e)
            null
        }
    }
}
