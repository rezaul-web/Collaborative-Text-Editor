package com.example.collaborativetexteditor.auth.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collaborativetexteditor.auth.mvi.AuthState
import com.example.collaborativetexteditor.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Login -> login(event.email, event.password)
            is AuthEvent.SignUp -> signup(event.name, event.email, event.password)
            AuthEvent.Logout -> logout()
            AuthEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = repository.login(email, password)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                    _effect.send(AuthEffect.NavigateToHome)
                },
                onFailure = { it ->
                    _state.update { it.copy(isLoading = false, error = it.error) }
                    _effect.send(AuthEffect.ShowToast(it.message ?: "Login failed"))
                }
            )
        }
    }

    private fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = repository.signup(name, email, password)
            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                    _effect.send(AuthEffect.NavigateToHome)

                },
                onFailure = { it ->
                    _state.update { it.copy(isLoading = false, error = it.error) }
                    _effect.send(AuthEffect.ShowToast(it.message ?: "Signup failed"))
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.update { AuthState(isAuthenticated = false) }
        }
    }

    val isAuthenticated: Boolean = repository.isUserLoggedIn()
    val currentUser = repository.getUserName()
}
