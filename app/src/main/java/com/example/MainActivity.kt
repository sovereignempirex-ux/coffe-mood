package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroCinematicOverlay
import com.example.ui.screens.UserProfileScreen
import com.example.ui.theme.MoodDarkInk
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MoodCafeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MoodCafeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                // Arabic RTL Layout Provider
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val context = LocalContext.current
                    val introFinished by viewModel.introFinished.collectAsState()
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val toastMessage by viewModel.toastMessage.collectAsState()

                    // Toast alerts
                    LaunchedEffect(toastMessage) {
                        toastMessage?.let { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            viewModel.clearToast()
                        }
                    }

                    // Android System Back Handler
                    BackHandler(enabled = currentScreen != AppScreen.HOME) {
                        viewModel.navigateTo(AppScreen.HOME)
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MoodDarkInk
                    ) {
                        if (!introFinished) {
                            IntroCinematicOverlay(
                                onEnterCafe = { viewModel.finishIntro() }
                            )
                        } else {
                            AnimatedContent(
                                targetState = currentScreen,
                                transitionSpec = {
                                    fadeIn() togetherWith fadeOut()
                                },
                                label = "ScreenTransition"
                            ) { screen ->
                                when (screen) {
                                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                                    AppScreen.USER_PROFILE -> UserProfileScreen(viewModel = viewModel)
                                    AppScreen.ADMIN_DASHBOARD -> AdminDashboardScreen(viewModel = viewModel)
                                    AppScreen.SETTINGS -> UserProfileScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

