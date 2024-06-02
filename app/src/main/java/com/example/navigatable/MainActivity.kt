package com.example.navigatable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.navigatable.ui.theme.NavigatableTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigatableTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = FIRST_SCREEN,
                ) {
                    firstScreen(
                        onClick = { navController.navigateToSecondScreen(it) }
                    )
                    secondScreen(
                        onBackPress = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
