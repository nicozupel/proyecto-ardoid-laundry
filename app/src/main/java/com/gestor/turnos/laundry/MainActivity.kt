package com.gestor.turnos.laundry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gestor.turnos.laundry.navigation.LaundryNavigation
import com.gestor.turnos.laundry.ui.theme.GestorTurnosLaundryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorTurnosLaundryTheme {
                LaundryApp()
            }
        }
    }
}

@Composable
fun LaundryApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LaundryNavigation()
    }
}

@Preview(showBackground = true)
@Composable
fun LaundryAppPreview() {
    GestorTurnosLaundryTheme {
        LaundryApp()
    }
}