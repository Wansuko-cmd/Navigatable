package com.example.navigatable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import annotation.Navigatable

@OptIn(ExperimentalMaterial3Api::class)
@Navigatable
@Composable
fun FirstScreen(onClick: (String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "First Screen") }) }
    ) { innerPadding ->
        var value by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            TextField(value = value, onValueChange = { value = it })
            Button(onClick = { onClick(value) }) {
                Text(text = "Go to Second Screen")
            }
        }
    }
}
