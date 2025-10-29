package com.example.collaborativetexteditor.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collaborativetexteditor.HomeScreen
import com.example.collaborativetexteditor.auth.LoginScreen
import com.example.collaborativetexteditor.auth.SignupScreen
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel

@Composable
fun Navigation(authViewModel: AuthViewModel= hiltViewModel()) {
    val starDestination = if (authViewModel.isAuthenticated) "home" else "login"
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = starDestination
    ) {

        // 🔹 Login Screen
        composable("login") {
            LoginScreen(
                onNavigateHome = { navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // remove login from back stack
                }},
                onSignUpClick = { navController.navigate("signup") }
            )
        }

        // 🔹 Sign Up Screen
        composable("signup") {
            SignupScreen(
                onNavigateHome = { navController.navigate("home") {
                    popUpTo("signup") { inclusive = true }
                }},
                onLoginClick = { navController.navigate("login") }
            )
        }

        // 🔹 Home Screen
        composable("home") {
            HomeScreen()
        }
    }
}
