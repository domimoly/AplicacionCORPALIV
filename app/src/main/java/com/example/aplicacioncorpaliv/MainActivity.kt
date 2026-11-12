package com.example.aplicacioncorpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.ui.App
import com.example.aplicacioncorpaliv.ui.theme.AplicacionCORPALIVTheme
import com.example.aplicacioncorpaliv.viewmodel.AccesibilidadViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Punto de entrada de la app | carga el tema y muestra App(), que contiene toda la navegación
        setContent {
            // EXPLICAR: las opciones de accesibilidad se leen AQUÍ, antes del tema, porque cambian el aspecto de TODA la app.
            // Cuando la pantalla Accesibilidad cambia una opción, estos valores cambian y el tema se aplica de nuevo.
            val accesibilidadViewModel: AccesibilidadViewModel = viewModel()
            val nivelTexto by accesibilidadViewModel.nivelTexto.collectAsState()
            val altoContraste by accesibilidadViewModel.altoContraste.collectAsState()

            AplicacionCORPALIVTheme(
                nivelTexto = nivelTexto,
                altoContraste = altoContraste
            ) {
                App()
            }
        }
    }
}
