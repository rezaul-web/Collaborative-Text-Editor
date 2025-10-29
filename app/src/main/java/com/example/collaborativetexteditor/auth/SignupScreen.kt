package com.example.collaborativetexteditor.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.collaborativetexteditor.auth.mvi.AuthEffect
import com.example.collaborativetexteditor.auth.mvi.AuthEvent
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel
import com.example.collaborativetexteditor.utils.widgets.AuthHeader
import com.example.collaborativetexteditor.utils.widgets.ClickableTextRow
import com.example.collaborativetexteditor.utils.widgets.EmailTextField
import com.example.collaborativetexteditor.utils.widgets.PasswordTextField
import com.example.collaborativetexteditor.utils.widgets.PrimaryButton
import com.google.rpc.context.AttributeContext

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context= LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observe state and effects from the ViewModel
    val state by authViewModel.state.collectAsState()
    val effect = authViewModel.effect

    // 🔹 React to one-time side effects like navigation
    LaunchedEffect(effect) {
        effect.collect { event ->
            when (event) {
                is AuthEffect.NavigateToHome -> onNavigateHome()
                is AuthEffect.ShowToast ->Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

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
            AuthHeader(
                title = "Create an Account",
                subtitle = "Get started with your new account"
            )

            Spacer(Modifier.height(32.dp))

            EmailTextField(
                label = "Name",
                value = name,
                onValueChange = { name = it }
            )

            EmailTextField(
                value = email,
                onValueChange = { email = it }
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it }
            )

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                PrimaryButton(
                    text = "Sign Up",
                    onClick = {
                        authViewModel.onEvent(AuthEvent.ClearError)
                        authViewModel.onEvent(AuthEvent.SignUp(name, email, password))
                    }
                )
            }

            ClickableTextRow(
                text = "Already have an account?",
                clickableText = "Login",
                onClick = onLoginClick
            )
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
