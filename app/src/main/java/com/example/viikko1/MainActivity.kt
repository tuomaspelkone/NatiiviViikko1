package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viikko1.navigation.ROUTE_CALENDAR
import com.example.viikko1.navigation.ROUTE_HOME
import com.example.viikko1.navigation.ROUTE_SETTINGS
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.view.CalendarScreen
import com.example.viikko1.view.HomeScreen
import com.example.viikko1.view.SettingsScreen
import com.example.viikko1.viewModel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemInDarkTheme) }

            val navController = rememberNavController()
            val viewModel: TaskViewModel = viewModel()

            Viikko1Theme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_HOME
                    ) {
                        composable(ROUTE_HOME) {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateCalendar = {
                                    navController.navigate(ROUTE_CALENDAR)
                                },
                                onNavigateToSettings = {
                                    navController.navigate(ROUTE_SETTINGS)
                                }
                            )
                        }
                        composable(ROUTE_CALENDAR) {
                            CalendarScreen(
                                viewModel = viewModel,
                                onTaskClick = { task ->
                                    viewModel.selectTask(task)
                                },
                                onNavigateToHome = {
                                    navController.navigate(ROUTE_HOME)
                                }
                            )
                        }
                        composable(ROUTE_SETTINGS) {
                            SettingsScreen(
                                darkTheme = darkTheme,
                                onThemeToggle = { darkTheme = !darkTheme },
                                onNavigateToHome = {
                                    navController.navigate(ROUTE_HOME)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
