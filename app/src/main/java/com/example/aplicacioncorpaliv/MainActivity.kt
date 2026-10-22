package com.example.aplicacioncorpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aplicacioncorpaliv.ui.App
import com.example.aplicacioncorpaliv.ui.theme.AplicacionCORPALIVTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Punto de entrada de la app | carga el tema y muestra App(), que contiene toda la navegación
        setContent {
            AplicacionCORPALIVTheme {
                App()
            }
        }
    }
}