package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// MENÚ LATERAL (el que se abre con el botón MENU de la barra superior).
// EXPLICAR: está ordenado en grupos separados por una línea (HorizontalDivider):
//   1) conocer CORPALIV  2) lo mío (solicitudes y perfil)  3) acceso gestores  4) configuración (el engranaje, al final).
// Cada ítem es una llamada a 'ItemMenu'. Para agregar una pantalla: sumar un ItemMenu aquí (el texto debe ser igual
// al del 'when' de App.kt) y un caso en el 'when'.
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

                    // Grupo 1: conocer CORPALIV
                    ItemMenu("Quiénes somos", Icons.Filled.Info, pantallaActual, onPantallaSeleccionada, drawerState, scope)
                    ItemMenu("Programas", Icons.Filled.Groups, pantallaActual, onPantallaSeleccionada, drawerState, scope)
                    ItemMenu("Productos", Icons.Filled.ShoppingCart, pantallaActual, onPantallaSeleccionada, drawerState, scope)
                    ItemMenu("Impacto", Icons.Filled.Star, pantallaActual, onPantallaSeleccionada, drawerState, scope)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                    // Grupo 2: lo que es de la persona
                    ItemMenu("Mis solicitudes", Icons.Filled.CheckCircle, pantallaActual, onPantallaSeleccionada, drawerState, scope)
                    ItemMenu("Perfil", Icons.Filled.Person, pantallaActual, onPantallaSeleccionada, drawerState, scope)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                    // Grupo 3: entrada para el personal de CORPALIV (pide correo y clave)
                    ItemMenu("Acceso gestores", Icons.Filled.Lock, pantallaActual, onPantallaSeleccionada, drawerState, scope)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                    // Grupo 4: el engranaje va al final. Incluye accesibilidad y novedades
                    ItemMenu("Configuración", Icons.Filled.Settings, pantallaActual, onPantallaSeleccionada, drawerState, scope)
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

// Un ítem del menú: ícono + texto. Al tocarlo cambia la pantalla y cierra el menú.
// EXPLICAR: antes cada ítem repetía este bloque completo; al ser una función aparte, el menú queda mucho más corto.
@Composable
private fun ItemMenu(
    texto: String,
    icono: ImageVector,
    pantallaActual: String,
    onPantallaSeleccionada: (String) -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    NavigationDrawerItem(
        label = { Text(texto) },
        icon = { Icon(imageVector = icono, contentDescription = null) },
        selected = pantallaActual == texto,
        onClick = {
            onPantallaSeleccionada(texto)
            scope.launch {
                drawerState.close()
            }
        },
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}
