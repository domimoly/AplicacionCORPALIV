package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.aplicacioncorpaliv.ui.theme.AmarilloAlto
import com.example.aplicacioncorpaliv.ui.theme.LocalAltoContraste
import com.example.aplicacioncorpaliv.ui.theme.MoradoOla

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onMenuClick: () -> Unit
) {
    // EXPLICAR: la barra usa el mismo color de la ola del Inicio, para que se vean como una sola pieza.
    // Con alto contraste pasa a amarillo con letras negras
    val altoContraste = LocalAltoContraste.current
    val colorBarra = if (altoContraste) AmarilloAlto else MoradoOla
    val colorTexto = if (altoContraste) Color.Black else Color.White

    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
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
        title = { Text(text = titulo) },
        actions = {
            // Guarda si el menú desplegable está abierto (true) o cerrado (false)
            var isMenuOpened by remember { mutableStateOf(false) }
            IconButton(onClick = { isMenuOpened = true }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Opciones Menu"
                )
                NuevoMenu(isExpanded = isMenuOpened, onItemClick = {}) {
                    isMenuOpened=false
                }
            }
        })
}
