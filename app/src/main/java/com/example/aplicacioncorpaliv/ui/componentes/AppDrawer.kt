package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                // EXPLICAR: la Column con verticalScroll permite deslizar el menú si hay más ítems de los que caben
                // en la pantalla (por ejemplo con el teléfono girado o con el texto grande)
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // El logo es un componente aparte (LogoCorpaliv) que se reutiliza en varias pantallas
                    LogoCorpaliv(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(220.dp)
                    )
                    // al tocarlo cambia la pantalla y cierra el menú. Para agregar uno, copiar un bloque completo
                    // (el texto debe ser igual al del 'when' de App.kt)
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

                    // Historias e indicadores de impacto de CORPALIV
                    NavigationDrawerItem(
                        label = { Text("Impacto") },
                        selected = pantallaActual == "Impacto",
                        onClick = {
                            onPantallaSeleccionada("Impacto")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    // Antes se llamaba 'Registros'. Aquí la persona ve en qué estado está cada solicitud que envió
                    NavigationDrawerItem(
                        label = { Text("Mis solicitudes") },
                        selected = pantallaActual == "Mis solicitudes",
                        onClick = {
                            onPantallaSeleccionada("Mis solicitudes")
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

                    // Antes 'Accesibilidad'. Ahora incluye también las preferencias de novedades
                    NavigationDrawerItem(
                        label = { Text("Configuración") },
                        selected = pantallaActual == "Configuración",
                        onClick = {
                            onPantallaSeleccionada("Configuración")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )

                    // Entrada para el personal de CORPALIV (gestores de vinculación): pide correo y clave
                    NavigationDrawerItem(
                        label = { Text("Acceso gestores") },
                        selected = pantallaActual == "Acceso gestores",
                        onClick = {
                            onPantallaSeleccionada("Acceso gestores")
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
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
