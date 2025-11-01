package com.example.collaborativetexteditor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collaborativetexteditor.files.data.model.DocFile
import com.example.collaborativetexteditor.files.usecases.AddOrUpdateFileUseCase
import com.example.collaborativetexteditor.files.usecases.GetUserFilesUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserFilesUseCase: GetUserFilesUseCase,
    private val addFileUseCase: AddOrUpdateFileUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadFiles -> loadFiles()
            is HomeIntent.CreateNewFile -> createNewFile()
            is HomeIntent.OpenFile -> openFile(intent.fileId)
        }
    }

    private fun loadFiles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val uid = firebaseAuth.currentUser?.uid ?: return@launch
            try {
                val files = getUserFilesUseCase(uid)
                _state.update { it.copy(isLoading = false, files = files) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun createNewFile() {
        viewModelScope.launch {
            val uid = firebaseAuth.currentUser?.uid ?: return@launch
            val newId = UUID.randomUUID().toString()

            val newFile = DocFile(
              docId = newId,
                title = "Untitled Document",
                ownerId = uid,
                collaborators = mapOf(uid to true),
                content = ""
            )

            try {
                addFileUseCase(newId, uid, newFile)
                _effect.emit(HomeEffect.NavigateToEditor(newId))
            } catch (e: Exception) {
                _effect.emit(HomeEffect.ShowToast("Failed to create file"))
            }
        }
    }

    private fun openFile(fileId: String) {
        viewModelScope.launch {
            _effect.emit(HomeEffect.NavigateToEditor(fileId))
        }
    }
}
