package com.example.aplicacioncorpaliv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

// EXPLICAR: un ColorScheme es la "tabla de colores" de Material 3. Cada componente (botón, tarjeta,
// barra) toma de aquí su color. Por eso, si se cambia un color en esta tabla, cambia en toda la app.
// La app tiene DOS tablas: la normal y la de alto contraste (negro, blanco y amarillo del logo).
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

// EXPLICAR: tabla de ALTO CONTRASTE. Fondo negro, texto blanco y el amarillo del logo como color de acento.
// El texto negro sobre amarillo y el blanco sobre negro superan por mucho el contraste mínimo recomendado (4,5 a 1).
private val EsquemaAltoContraste = lightColorScheme(
    primary = AmarilloAlto,
    onPrimary = Color.Black,
    primaryContainer = GrisOscuroAlto,
    onPrimaryContainer = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    secondaryContainer = GrisMedioAlto,
    onSecondaryContainer = Color.White,
    tertiary = AmarilloAlto,
    onTertiary = Color.Black,
    tertiaryContainer = GrisOscuroAlto,
    onTertiaryContainer = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = GrisOscuroAlto,
    onSurfaceVariant = Color(0xFFE6E6E6),
    outline = Color.White,
    error = Color(0xFFFF8A80),
    onError = Color.Black,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF0D0D0D),
    surfaceContainer = Color(0xFF141414),
    surfaceContainerHigh = GrisOscuroAlto,
    surfaceContainerHighest = GrisMedioAlto
)

// AVANZADO: CompositionLocal = un dato que cualquier componente de la app puede leer sin pasarlo
// de pantalla en pantalla. Aquí lo usamos para que la ola, la barra superior y el logo sepan
// si el alto contraste está activado:  val alto = LocalAltoContraste.current
val LocalAltoContraste = compositionLocalOf { false }

// Los 3 tamaños de texto: normal, grande y muy grande (se multiplica el tamaño de cada letra)
private val EscalasTexto = listOf(1.0f, 1.15f, 1.3f)

@Composable
fun AplicacionCORPALIVTheme(
    nivelTexto: Int = 0,
    altoContraste: Boolean = false,
    content: @Composable () -> Unit
) {
    // Se elige la tabla de colores según la opción guardada
    val colorScheme = if (altoContraste) EsquemaAltoContraste else EsquemaClaro

    // AVANZADO: así se agranda TODO el texto de la app a la vez. LocalDensity guarda cuánto se escalan
    // las letras (fontScale). Se multiplica por la escala elegida, respetando también el tamaño de letra
    // que el usuario ya tenga en los ajustes de su teléfono.
    val densidadActual = LocalDensity.current
    val densidad = Density(
        density = densidadActual.density,
        fontScale = densidadActual.fontScale * EscalasTexto[nivelTexto.coerceIn(0, 2)]
    )

    CompositionLocalProvider(
        LocalDensity provides densidad,
        LocalAltoContraste provides altoContraste
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
