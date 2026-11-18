package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.aplicacioncorpaliv.ui.theme.AmarilloAlto
import com.example.aplicacioncorpaliv.ui.theme.LocalAltoContraste
import com.example.aplicacioncorpaliv.ui.theme.MoradoOla

// EXPLICAR: la barra solo tiene el botón MENU (abre el menú lateral) y el título de la pantalla.
// Antes tenía además un segundo menú desplegable a la derecha que no hacía nada; se quitó para no confundir.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onMenuClick: () -> Unit
) {
    // La barra usa el mismo color de la ola del Inicio, para que se vean como una sola pieza.
    // Con alto contraste pasa a amarillo con letras negras
    val altoContraste = LocalAltoContraste.current
    val colorBarra = if (altoContraste) AmarilloAlto else MoradoOla
    val colorTexto = if (altoContraste) Color.Black else Color.White

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorBarra,
            titleContentColor = colorTexto,
            actionIconContentColor = colorTexto
        ),
        navigationIcon = {
            TextButton(
                onClick = onMenuClick
            ) {
                Text("MENU", color = colorTexto)
            }
        },
        title = { Text(text = titulo) }
    )
}
