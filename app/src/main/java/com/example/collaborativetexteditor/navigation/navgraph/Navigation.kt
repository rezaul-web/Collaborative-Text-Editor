package com.example.collaborativetexteditor.navigation.navgraph

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collaborativetexteditor.editor.EditorScreen
import com.example.collaborativetexteditor.auth.LoginScreen
import com.example.collaborativetexteditor.auth.SignupScreen
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel
import com.example.collaborativetexteditor.home.HomeScreen

@Composable
fun Navigation(authViewModel: AuthViewModel = hiltViewModel()) {
    val starDestination = if (authViewModel.isAuthenticated) "home" else "login"
    val navController = rememberNavController()



    NavHost(
        navController = navController,
        startDestination = starDestination,
    ) {

        // 🔹 Login Screen
        composable("login") {
            LoginScreen(
                onNavigateHome = {

                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = { navController.navigate("signup") }
            )
        }


        composable("signup") {
            SignupScreen(
                onNavigateHome = {
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate("login") }
            )
        }


        composable("home") {
            HomeScreen(onNavigateToEditor = { fileId ->
                navController.navigate("editor/$fileId")
            })
        }

        composable(
            route = "editor/{fileId}"
        ) { backStackEntry ->
            val fileId = backStackEntry.arguments?.getString("fileId") ?: return@composable
            EditorScreen(fileId = fileId, onBack = { navController.popBackStack() })
        }
    }
}