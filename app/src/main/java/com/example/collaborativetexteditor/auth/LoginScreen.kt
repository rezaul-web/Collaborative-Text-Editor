package com.example.collaborativetexteditor.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.collaborativetexteditor.auth.mvi.AuthEffect
import com.example.collaborativetexteditor.auth.mvi.AuthEvent
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel
import com.example.collaborativetexteditor.utils.widgets.AuthHeader
import com.example.collaborativetexteditor.utils.widgets.ClickableTextRow
import com.example.collaborativetexteditor.utils.widgets.EmailTextField
import com.example.collaborativetexteditor.utils.widgets.PasswordTextField
import com.example.collaborativetexteditor.utils.widgets.PrimaryButton

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val state by authViewModel.state.collectAsState()
    val context = LocalContext.current

    // Handle one-time effects like navigation or toast
    LaunchedEffect(Unit) {
        authViewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToHome -> onNavigateHome()
                is AuthEffect.ShowToast -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader(title = "Welcome Back!", subtitle = "Login to continue")

            Spacer(Modifier.height(32.dp))

            EmailTextField(
                value = email,
                onValueChange = { email = it }
            )

            Spacer(Modifier.height(16.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it }
            )

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                text = if (state.isLoading) "Logging in..." else "Login",
                onClick = {
                    if (!state.isLoading) {
                        authViewModel.onEvent(AuthEvent.ClearError)
                        authViewModel.onEvent(AuthEvent.Login(email, password))
                    }
                }
            )

            ClickableTextRow(
                text = "Don't have an account?",
                clickableText = "Sign Up",
                onClick = onSignUpClick
            )

            // Optional: show error text
            state.error?.let { error ->
                Spacer(Modifier.height(16.dp))
                androidx.compose.material3.Text(
                    text = error,
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onNavigateHome = {},
        onSignUpClick = {}
    )
}
