package com.example.collaborativetexteditor.utils.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopAppBar(
    onBack: () -> Unit,
    onAddCollaborator: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { /* Title is handled by the TextField in the screen */ },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        },
        actions = {
            // New "Add Collaborator" Button
            IconButton(onClick = onAddCollaborator) {
                Icon(Icons.Default.PersonAdd, "Add Collaborator")
            }
            
            // Existing "Delete" Button
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete File")
            }
        }
    )
}