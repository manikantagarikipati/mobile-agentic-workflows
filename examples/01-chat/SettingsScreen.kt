// What got pasted. Do not ship this.
package com.example.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    Column(
        Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
    ) {
        Text("Settings", fontSize = 24.sp)
        Button(onClick = { /* TODO */ }) { Text("Save") }
    }
}
