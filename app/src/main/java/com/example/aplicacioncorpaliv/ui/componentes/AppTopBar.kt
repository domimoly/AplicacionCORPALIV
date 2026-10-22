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
import com.example.aplicacioncorpaliv.ui.theme.MoradoCorpaliv

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onMenuClick: () -> Unit
) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MoradoCorpaliv,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White
    ),
        navigationIcon = {
            TextButton(
                onClick = onMenuClick
            ) {
                Text("MENU",color=Color.White)
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