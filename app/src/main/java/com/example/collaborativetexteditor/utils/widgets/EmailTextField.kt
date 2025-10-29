package com.example.collaborativetexteditor.utils.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collaborativetexteditor.R

/**
 * A reusable, styled email text field.
 */
@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Email",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * A reusable, styled password text field with visibility toggle.
 */
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Password",
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Done
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        trailingIcon = {
            val image =
                if (passwordVisible) painterResource(R.drawable.visibility) else painterResource(R.drawable.visibility_off)
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, description)
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * A reusable, styled primary button that shows a loading indicator.
 */
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}

/**
 * A reusable header component for auth screens.
 */
@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * A row with plain text and clickable text.
 * Example: "Don't have an account? Sign Up"
 */
@Composable
fun ClickableTextRow(
    modifier: Modifier = Modifier,
    text: String,
    clickableText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        Spacer(Modifier.width(4.dp))
        TextButton(onClick = onClick) {
            Text(clickableText, fontWeight = FontWeight.Bold)
        }
    }
}
