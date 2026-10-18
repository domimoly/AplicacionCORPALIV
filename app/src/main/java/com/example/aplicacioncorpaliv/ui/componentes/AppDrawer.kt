package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.R
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    contenido: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                NavigationDrawerItem(
                    label = { Text("Quiénes somos") },
                    selected = pantallaActual == "Quiénes somos",
                    onClick = {
                        onPantallaSeleccionada("Quiénes somos")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Programas") },
                    selected = pantallaActual == "Programas",
                    onClick = {
                        onPantallaSeleccionada("Programas")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Productos") },
                    selected = pantallaActual == "Productos",
                    onClick = {
                        onPantallaSeleccionada("Productos")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Novedades") },
                    selected = pantallaActual == "Novedades",
                    onClick = {
                        onPantallaSeleccionada("Novedades")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Registros") },
                    selected = pantallaActual == "Registros",
                    onClick = {
                        onPantallaSeleccionada("Registros")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = pantallaActual == "Perfil",
                    onClick = {
                        onPantallaSeleccionada("Perfil")
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        contenido {
            scope.launch {
                drawerState.open()
            }
        }
    }
}