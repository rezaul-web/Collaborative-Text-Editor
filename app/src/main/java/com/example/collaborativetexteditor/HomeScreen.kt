package com.example.collaborativetexteditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel= hiltViewModel()) {
    Column(modifier= Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Home Screen")
        Text(text =authViewModel.currentUser.toString() )
    }
}