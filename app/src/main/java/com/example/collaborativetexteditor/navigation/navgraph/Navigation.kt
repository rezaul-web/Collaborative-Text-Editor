package com.example.collaborativetexteditor.navigation.navgraph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collaborativetexteditor.home.HomeScreen
import com.example.collaborativetexteditor.auth.LoginScreen
import com.example.collaborativetexteditor.auth.SignupScreen
import com.example.collaborativetexteditor.auth.mvi.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(authViewModel: AuthViewModel = hiltViewModel()) {
    val starDestination = if (authViewModel.isAuthenticated) "home" else "login"
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Collaborative Text Editor")
                    }

                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
                )

        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = starDestination
            ) {

                // 🔹 Login Screen
                composable("login") {
                    LoginScreen(
                        onNavigateHome = {
                            navController.navigate("home") {
                                popUpTo("login") {
                                    inclusive = true
                                } // remove login from back stack
                            }
                        },
                        onSignUpClick = { navController.navigate("signup") }
                    )
                }

                // 🔹 Sign Up Screen
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

                // 🔹 Home Screen
                composable("home") {
                    HomeScreen()
                }
            }
        }

    }


}
