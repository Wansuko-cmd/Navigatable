package com.example.navigatable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import annotation.Navigatable

@Navigatable
@Composable
fun FirstScreen() {
    Scaffold { innerPadding ->
        Text(modifier = Modifier.padding(innerPadding), text = "First Screen")
    }
}
