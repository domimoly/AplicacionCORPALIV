package com.example.aplicacioncorpaliv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.aplicacioncorpaliv.R

// EXPLICAR: los archivos de la tipografía Poppins están en res/font. Aquí se agrupan en una
// "familia" indicando qué archivo corresponde a cada grosor (normal, medio, seminegrita, negrita).
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// EXPLICAR: Typography() trae los 15 estilos de texto de Material 3 (titleLarge, bodyMedium...).
// Con .copy(fontFamily = Poppins) conservamos los tamaños de siempre y solo cambiamos la letra.
// Así TODOS los textos de la app (botones, barras, campos) pasan a Poppins sin tocar cada pantalla.
// Los títulos se dejan más gruesos (Bold / SemiBold) para que destaquen.
private val Base = Typography()

val Typography = Typography(
    displayLarge = Base.displayLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
    displayMedium = Base.displayMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
    displaySmall = Base.displaySmall.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
    headlineLarge = Base.headlineLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
    headlineMedium = Base.headlineMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
    headlineSmall = Base.headlineSmall.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
    titleLarge = Base.titleLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
    titleMedium = Base.titleMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
    titleSmall = Base.titleSmall.copy(fontFamily = Poppins, fontWeight = FontWeight.Medium),
    bodyLarge = Base.bodyLarge.copy(fontFamily = Poppins),
    bodyMedium = Base.bodyMedium.copy(fontFamily = Poppins),
    bodySmall = Base.bodySmall.copy(fontFamily = Poppins),
    labelLarge = Base.labelLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
    labelMedium = Base.labelMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.Medium),
    labelSmall = Base.labelSmall.copy(fontFamily = Poppins, fontWeight = FontWeight.Medium)
)
