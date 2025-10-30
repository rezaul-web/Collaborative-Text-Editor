package com.example.collaborativetexteditor.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

private data class ToolbarButtonData(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val contentDescription: String,
    val intent: HomeIntent,
    val isSelected: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.homeState.collectAsState()

    LaunchedEffect(uiState.richTextState.toHtml()) {
        homeViewModel.handleIntent(HomeIntent.OnContentChange(uiState.richTextState.toHtml()))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Toolbar(
            state = uiState.richTextState,
            onIntent = { intent -> homeViewModel.handleIntent(intent) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = DividerDefaults.Thickness,
            color = Color.LightGray
        )

        RichTextEditor(
            state = uiState.richTextState,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
private fun Toolbar(
    state: RichTextState,
    onIntent: (HomeIntent) -> Unit
) {

    val isBold = state.currentSpanStyle.fontWeight == FontWeight.Bold
    val isItalic = state.currentSpanStyle.fontStyle == FontStyle.Italic
    val isUnderlined = state.currentSpanStyle.textDecoration == TextDecoration.Underline
    val isStrikethrough = state.currentSpanStyle.textDecoration == TextDecoration.LineThrough
    val isAlignLeft =
        state.currentParagraphStyle.textAlign == TextAlign.Left || state.currentParagraphStyle.textAlign == TextAlign.Start
    val isAlignCenter = state.currentParagraphStyle.textAlign == TextAlign.Center
    val isAlignRight =
        state.currentParagraphStyle.textAlign == TextAlign.Right || state.currentParagraphStyle.textAlign == TextAlign.End
    val isBulletedList = state.isUnorderedList
    val isNumberedList = state.isOrderedList
    val fontSizes = listOf(12.sp, 14.sp, 16.sp, 18.sp, 22.sp, 26.sp)
    val fontFamilies = mapOf(
        "Sans Serif" to FontFamily.SansSerif,
        "Serif" to FontFamily.Serif,
        "Cursive" to FontFamily.Cursive,
        "Monospace" to FontFamily.Monospace
    )


    val styleButtons = listOf(
        ToolbarButtonData(Icons.Default.FormatBold, "Bold", HomeIntent.ToggleBold, isBold),
        ToolbarButtonData(Icons.Default.FormatItalic, "Italic", HomeIntent.ToggleItalic, isItalic),
        ToolbarButtonData(
            Icons.Default.FormatUnderlined,
            "Underline",
            HomeIntent.ToggleUnderline,
            isUnderlined
        ),
        ToolbarButtonData(
            Icons.Default.FormatStrikethrough,
            "Strike",
            HomeIntent.ToggleStrikethrough,
            isStrikethrough
        )
    )

    val alignmentButtons = listOf(
        ToolbarButtonData(
            Icons.AutoMirrored.Filled.FormatAlignLeft,
            "Align Left",
            HomeIntent.AlignLeft,
            isAlignLeft
        ),
        ToolbarButtonData(
            Icons.Default.FormatAlignCenter,
            "Align Center",
            HomeIntent.AlignCenter,
            isAlignCenter
        ),
        ToolbarButtonData(
            Icons.AutoMirrored.Filled.FormatAlignRight,
            "Align Right",
            HomeIntent.AlignRight,
            isAlignRight
        )
    )

    val listButtons = listOf(
        ToolbarButtonData(
            Icons.AutoMirrored.Filled.FormatListBulleted,
            "Bulleted List",
            HomeIntent.ToggleBulletedList,
            isBulletedList
        ),
        ToolbarButtonData(
            Icons.Default.FormatListNumbered,
            "Numbered List",
            HomeIntent.ToggleNumberedList,
            isNumberedList
        )
    )


    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        styleButtons.forEach { button ->
            ToolbarButton(
                onClick = { onIntent(button.intent) },
                icon = button.icon,
                contentDescription = button.contentDescription,
                isSelected = button.isSelected
            )
        }

        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .align(Alignment.CenterVertically)
        )

        alignmentButtons.forEach { button ->
            ToolbarButton(
                onClick = { onIntent(button.intent) },
                icon = button.icon,
                contentDescription = button.contentDescription,
                isSelected = button.isSelected
            )
        }

        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .align(Alignment.CenterVertically)
        )


        listButtons.forEach { button ->
            ToolbarButton(
                onClick = { onIntent(button.intent) },
                icon = button.icon,
                contentDescription = button.contentDescription,
                isSelected = button.isSelected
            )
        }

        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .align(Alignment.CenterVertically)
        )

        var sizeExpanded by remember { mutableStateOf(false) }
        Box {

            TextButton(
                onClick = { sizeExpanded = true },
                modifier = Modifier.width(64.dp) // Give it a fixed width
            ) {

                val sizeText = if (state.currentSpanStyle.fontSize.isUnspecified) "Size"
                else state.currentSpanStyle.fontSize.value.toInt().toString()
                Text(sizeText)
            }

            DropdownMenu(expanded = sizeExpanded, onDismissRequest = { sizeExpanded = false }) {
                fontSizes.forEach { size ->
                    DropdownMenuItem(
                        text = { Text("${size.value.toInt()} sp") },
                        onClick = {
                            onIntent(HomeIntent.SetFontSize(size))
                            sizeExpanded = false
                        }
                    )
                }
            }
        }
        var familyExpanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { familyExpanded = true }) {
                Icon(Icons.Default.FontDownload, contentDescription = "Font Family")
            }

            DropdownMenu(expanded = familyExpanded, onDismissRequest = { familyExpanded = false }) {
                fontFamilies.forEach { (name, family) ->
                    DropdownMenuItem(
                        text = { Text(name, fontFamily = family) },
                        onClick = {
                            onIntent(HomeIntent.SetFontFamily(family))
                            familyExpanded = false
                        }
                    )
                }

            }
        }


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
                            onIntent(HomeIntent.SetTextColor(color))
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun ToolbarButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isSelected: Boolean = false
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            }
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}
