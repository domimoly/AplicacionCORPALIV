package com.example.aplicacioncorpaliv.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.aplicacioncorpaliv.ui.componentes.AppDrawer
import com.example.aplicacioncorpaliv.ui.componentes.AppTopBar
import com.example.aplicacioncorpaliv.ui.screens.ContactoScreen
import com.example.aplicacioncorpaliv.ui.screens.FormularioScreen
import com.example.aplicacioncorpaliv.ui.screens.InicioScreen
import com.example.aplicacioncorpaliv.ui.screens.PantallaPrincipal
import com.example.aplicacioncorpaliv.ui.screens.PerfilScreen
import com.example.aplicacioncorpaliv.ui.screens.ProductosScreen
import com.example.aplicacioncorpaliv.ui.screens.ProgramasScreen
import com.example.aplicacioncorpaliv.ui.screens.QuienesSomosScreen
import com.example.aplicacioncorpaliv.ui.screens.RegistrosScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var pantallaActual by rememberSaveable {
        mutableStateOf("Inicio")
    }
    AppDrawer(
        pantallaActual = pantallaActual,
        onPantallaSeleccionada = {
            pantallaActual = it
        }
    ) { abrirMenu ->
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it.label == pantallaActual,
                        onClick = { pantallaActual = it.label }
                    )
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    AppTopBar(
                        onMenuClick = {
                            abrirMenu()
                        }
                    )
                }
            ) { innerPadding ->
                when (pantallaActual) {
                    "Inicio" -> {
                        InicioScreen(
                            modifier = Modifier.padding(innerPadding),
                            onPantallaSeleccionada = {
                                pantallaActual = it
                            }
                        )
                    }
                    "Quiénes somos" -> {
                        QuienesSomosScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Programas" -> {
                        ProgramasScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Productos" -> {
                        ProductosScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Novedades" -> {
                        PantallaPrincipal(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Registros" -> {
                        RegistrosScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Perfil" -> {
                        PerfilScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Colaborar" -> {
                        FormularioScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    "Contacto" -> {
                        ContactoScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Inicio", Icons.Filled.Home),
    FAVORITES("Colaborar", Icons.Filled.Favorite),
    PROFILE("Contacto", Icons.Filled.Person)
}