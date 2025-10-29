
package com.example.collaborativetexteditor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // This will now include Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel() // Assuming you need this for auth state
) {
    // Collect the state from the ViewModel
    val richState by homeViewModel.richTextState.collectAsState()

    // This effect will be triggered whenever the content changes.
    // Use it to send updates to the backend via the ViewModel.
    LaunchedEffect(richState.toHtml()) {
        homeViewModel.onContentChange(richState.toHtml())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        // Header with Logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Collaborative Editor",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = {

            }) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toolbar
        Toolbar(
            state = richState,
            onBoldClick = homeViewModel::toggleBold,
            onItalicClick = homeViewModel::toggleItalic,
            onUnderlineClick = homeViewModel::toggleUnderline,
            onStrikethroughClick = homeViewModel::toggleStrikethrough,
            onAlignLeftClick = homeViewModel::setTextAlignLeft,
            onAlignCenterClick = homeViewModel::setTextAlignCenter,
            onAlignRightClick = homeViewModel::setTextAlignRight,
            onBulletedListClick = homeViewModel::toggleBulletedList,
            onNumberedListClick = homeViewModel::toggleNumberedList,
            onColorClick = { color -> homeViewModel.setTextColor(color) }
        )
        Divider(color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

        // Rich Text Editor
        RichTextEditor(
            state = richState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun Toolbar(
    state: RichTextState,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
    onStrikethroughClick: () -> Unit, // New
    onAlignLeftClick: () -> Unit,      // New
    onAlignCenterClick: () -> Unit,    // New
    onAlignRightClick: () -> Unit,     // New
    onBulletedListClick: () -> Unit,   // New
    onNumberedListClick: () -> Unit,   // New
    onColorClick: (Color) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .horizontalScroll(scrollState), // Makes it scrollable
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Existing Style Checks ---
        val isBold = state.currentSpanStyle.fontWeight == FontWeight.Bold
        val isItalic = state.currentSpanStyle.fontStyle == FontStyle.Italic
        val isUnderlined = state.currentSpanStyle.textDecoration == TextDecoration.Underline

        // --- New Style Checks ---
        val isStrikethrough = state.currentSpanStyle.textDecoration == TextDecoration.LineThrough
        val isAlignLeft = state.currentParagraphStyle.textAlign == TextAlign.Left || state.currentParagraphStyle.textAlign == TextAlign.Start
        val isAlignCenter = state.currentParagraphStyle.textAlign == TextAlign.Center
        val isAlignRight = state.currentParagraphStyle.textAlign == TextAlign.Right || state.currentParagraphStyle.textAlign == TextAlign.End
        val isBulletedList = state.isUnorderedList
        val isNumberedList = state.isOrderedList

        // --- Buttons ---

        // Bold
        IconButton(
            onClick = onBoldClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isBold) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatBold, contentDescription = "Bold")
        }

        // Italic
        IconButton(
            onClick = onItalicClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isItalic) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
        }

        // Underline
        IconButton(
            onClick = onUnderlineClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isUnderlined) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline")
        }

        // Strikethrough (New)
        IconButton(
            onClick = onStrikethroughClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isStrikethrough) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatStrikethrough, contentDescription = "Strikethrough")
        }

        // Divider
        VerticalDivider(modifier = Modifier.height(24.dp).padding(horizontal = 4.dp))

        // Align Left (New)
        IconButton(
            onClick = onAlignLeftClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isAlignLeft) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatAlignLeft, contentDescription = "Align Left")
        }

        // Align Center (New)
        IconButton(
            onClick = onAlignCenterClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isAlignCenter) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatAlignCenter, contentDescription = "Align Center")
        }

        // Align Right (New)
        IconButton(
            onClick = onAlignRightClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isAlignRight) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatAlignRight, contentDescription = "Align Right")
        }

        // Divider
        VerticalDivider(modifier = Modifier.height(24.dp).padding(horizontal = 4.dp))

        // Bulleted List (New)
        IconButton(
            onClick = onBulletedListClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isBulletedList) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatListBulleted, contentDescription = "Bulleted List")
        }

        // Numbered List (New)
        IconButton(
            onClick = onNumberedListClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (isNumberedList) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
        ) {
            Icon(Icons.Default.FormatListNumbered, contentDescription = "Numbered List")
        }

        // Divider
        VerticalDivider(modifier = Modifier.height(24.dp).padding(horizontal = 4.dp))

        // Color Dropdown
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.FormatColorText, contentDescription = "Text Color")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val colors = mapOf("Blue" to Color.Blue, "Red" to Color.Red, "Black" to Color.Black)
                colors.forEach { (name, color) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onColorClick(color)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}