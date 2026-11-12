package com.example.aplicacioncorpaliv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// EXPLICAR: un ColorScheme es la "tabla de colores" de Material 3. Cada componente (botón, tarjeta,
// barra) toma de aquí su color. Por eso, si se cambia un color en esta tabla, cambia en toda la app.
// La app usa SIEMPRE tema claro (sin modo oscuro automático). Más adelante el alto contraste
// se elegirá desde el menú de Accesibilidad.
private val EsquemaClaro = lightColorScheme(
    primary = MoradoOscuro,              // botones principales y títulos
    onPrimary = Color.White,             // texto sobre el color principal
    primaryContainer = MoradoClaro,      // tarjetas suaves
    onPrimaryContainer = TextoPrincipal,
    secondary = AzulOscuro,
    onSecondary = Color.White,
    secondaryContainer = AzulClaro,      // también pinta el "indicador" de la barra inferior
    onSecondaryContainer = TextoPrincipal,
    tertiary = RosaOscuro,
    onTertiary = Color.White,
    tertiaryContainer = RosaClaro,
    onTertiaryContainer = TextoPrincipal,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = FondoApp,
    onSurface = TextoPrincipal,
    surfaceVariant = Color(0xFFF1EFF8),
    onSurfaceVariant = TextoSecundario,
    outline = Color(0xFF7A7592),         // bordes de los campos de texto
    // Estos 5 son los fondos de la barra inferior, el menú lateral y las tarjetas
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7F5FB),
    surfaceContainer = Color(0xFFF3F1F8),
    surfaceContainerHigh = Color(0xFFEDEAF5),
    surfaceContainerHighest = Color(0xFFE7E4F0)
)

@Composable
fun AplicacionCORPALIVTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}
