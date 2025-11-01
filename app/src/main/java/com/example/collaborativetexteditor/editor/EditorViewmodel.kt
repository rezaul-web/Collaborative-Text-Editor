package com.example.collaborativetexteditor.editor

import android.util.Log
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.usecases.AddCollaboratorResult
import com.example.collaborativetexteditor.files.usecases.AddCollaboratorUseCase
import com.example.collaborativetexteditor.files.usecases.AddOrUpdateFileUseCase
import com.example.collaborativetexteditor.files.usecases.DeleteFileUseCase
import com.example.collaborativetexteditor.files.usecases.GetFileByIdUseCase
import com.example.collaborativetexteditor.files.usecases.ObserveFileChangesUseCase
import com.example.collaborativetexteditor.files.usecases.RemoveFileListenerUseCase
import com.example.collaborativetexteditor.files.usecases.UpdateFileContentUseCase
import com.example.collaborativetexteditor.files.usecases.UpdateFileTitleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi

@HiltViewModel
class EditorViewmodel @Inject constructor(
    private val deleteFileUseCase: DeleteFileUseCase,
    private val updateFileTitleUseCase: UpdateFileTitleUseCase,
    private val updateFileContentUseCase: UpdateFileContentUseCase,
    private val addOrUpdateFileUseCase: AddOrUpdateFileUseCase,
    private val getFileByIdUseCase: GetFileByIdUseCase,
    private val observeFileChangesUseCase: ObserveFileChangesUseCase,
    private val removeFileListenerUseCase: RemoveFileListenerUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val addCollaboratorUseCase: AddCollaboratorUseCase

) : ViewModel() {

    val currentUser = firebaseAuth.currentUser?.uid
    private val _editorState = MutableStateFlow(EditorState())
    val editorState = _editorState.asStateFlow()
    private var titleUpdateJob: Job? = null
    private val _editorEffect = MutableSharedFlow<EditorEffect>()
    val editorEffect = _editorEffect.asSharedFlow()
    private var autoSaveJob: Job? = null
    private var lastKnownContent: String = ""
    private var currentFileId: String? = null
    private var contentUpdateJob: Job? = null
    @OptIn(ExperimentalUuidApi::class)
    fun handleIntent(intent: EditorIntent) {
        viewModelScope.launch {
            val rich = _editorState.value.richTextState

            when (intent) {
                EditorIntent.ToggleBold -> rich.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                EditorIntent.ToggleItalic -> rich.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                EditorIntent.ToggleUnderline -> rich.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                EditorIntent.ToggleStrikethrough -> rich.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                EditorIntent.AlignLeft -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Left))
                EditorIntent.AlignCenter -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                EditorIntent.AlignRight -> rich.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right))
                EditorIntent.ToggleBulletedList -> rich.toggleUnorderedList()
                EditorIntent.ToggleNumberedList -> rich.toggleOrderedList()
                is EditorIntent.SetTextColor -> rich.toggleSpanStyle(SpanStyle(color = intent.color))
                is EditorIntent.OnTitleChange -> {
                    // Update UI state immediately
                    _editorState.value = _editorState.value.copy(title = intent.title)

                    // Debounce the save operation
                    titleUpdateJob?.cancel()
                    titleUpdateJob = viewModelScope.launch {
                        delay(1500L) // Wait 1.5s after typing stops
                        saveTitle(intent.fileId, intent.title)
                    }
                }
                is EditorIntent.OnContentChange -> {
                    // This is the correct "debounce" logic
                    Log.d("EditorViewmodel","On Content Change called")
                    contentUpdateJob?.cancel() // Cancel the previous save job
                    contentUpdateJob = viewModelScope.launch {
                        delay(1500L) // Wait for 1.5 seconds of inactivity
                        saveContent(intent.fileId, intent.content)
                    }
                }

                is EditorIntent.SetFontFamily -> rich.toggleSpanStyle(SpanStyle(fontFamily = intent.family))
                is EditorIntent.SetFontSize -> rich.toggleSpanStyle(SpanStyle(fontSize = intent.fontSize))
                EditorIntent.DeleteFile -> {
                    currentFileId?.let { fileId ->
                        val success = deleteFileUseCase(fileId)
                        if (success) {
                            // Tell the UI to navigate back
                            _editorEffect.emit(EditorEffect.NavigateBack)
                        } else {
                            // Tell the UI there was an error
                            _editorEffect.emit(EditorEffect.ShowError("Failed to delete file"))
                        }
                    }
                }
                is EditorIntent.AddCollaborator -> {
                    val fileId = currentFileId ?: return@launch
                    val result = addCollaboratorUseCase(fileId, intent.email)

                    // 3. Emit feedback to the UI
                    val message = when (result) {
                        is AddCollaboratorResult.Success -> "Collaborator added!"
                        is AddCollaboratorResult.UserNotFound -> "User not found."
                        is AddCollaboratorResult.IsOwner -> "You are already the owner."
                        is AddCollaboratorResult.Failure -> result.message
                    }
                    _editorEffect.emit(EditorEffect.ShowError(message)) // Re-using ShowError for snackbar
                }
            }
        }

    }
    private suspend fun saveTitle(fileId: String, title: String) {
        Log.d("EditorViewmodel", "Saving debounced title... $title")
        updateFileTitleUseCase(
            docId = fileId,
            title = title
        )
    }
    private suspend fun saveContent(fileId: String, content: String) {
        val userId = currentUser ?: return
        Log.d("EditorViewmodel", "Saving debounced content...${content}")

        // 2. Call the new, non-destructive UseCase
        updateFileContentUseCase(
            docId = fileId,
            content = content
        )
    }

    // Call this once when the screen loads
    fun loadAndObserveFile(fileId: String) {
        // Prevent re-registering listeners
        if (fileId == currentFileId) return

        // Stop listening to the old file, if any
        currentFileId?.let { removeFileListenerUseCase(it) }

        currentFileId = fileId

        viewModelScope.launch {
            // Load initial content
            val initialContent = getFileByIdUseCase(fileId)?.content ?: ""
            _editorState.value.title= getFileByIdUseCase(fileId)?.title.toString()
            _editorState.value.richTextState.setHtml(initialContent)
            Log.d("EditorViewmodel", "Initial content loaded.")

            // 3. Start listening for real-time collaborative updates
            observeFileChangesUseCase(fileId) { updatedFile ->
                updatedFile?.let {
                    val localHtml = _editorState.value.richTextState.toHtml()
                    // Only update if the remote content is different from local content
                    // This prevents an infinite loop
                    if (localHtml != it.content) {
                        Log.d("EditorViewmodel", "Remote change received. Updating UI.")
                        _editorState.value.richTextState.setHtml(it.content)
                    }
                }
            }
        }
    }

    // 4. Clean up the listener when the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        titleUpdateJob?.cancel()
        contentUpdateJob?.cancel()
        currentFileId?.let {
            Log.d("EditorViewmodel", "Removing file listener for $it")
            removeFileListenerUseCase(it)
        }
    }
}
