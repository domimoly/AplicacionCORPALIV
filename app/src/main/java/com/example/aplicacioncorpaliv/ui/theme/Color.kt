package com.example.aplicacioncorpaliv.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------------------------
// COLORES DE LA APP
// EXPLICAR: la paleta sale de una franja de tonos (café-rosa-morado-azul-verde azulado).
// Regla que usamos: los tonos medios (los de la franja) van solo en decoración sin texto encima
// (la ola, los círculos). Todo lo que lleva texto usa la versión OSCURA del mismo tono,
// porque el texto blanco sobre un tono medio no se lee bien (contraste bajo).
// ---------------------------------------------------------------------------------------------

// --- Decoración (sin texto encima) ---
val MoradoOla = Color(0xFF6A60AA)      // la ola de arriba y la barra superior

// --- Versiones oscuras: para botones, textos e íconos ---
val MoradoOscuro = Color(0xFF4B4390)   // color principal (botones, títulos)
val AzulOscuro = Color(0xFF385A94)
val RosaOscuro = Color(0xFF8E3A55)
val VerdeAzuladoOscuro = Color(0xFF27766F)
val TerracotaOscuro = Color(0xFF8A4A3A)

// --- Versiones claras: fondos suaves de los círculos y las tarjetas ---
val MoradoClaro = Color(0xFFE4E1F3)
val AzulClaro = Color(0xFFDCE4F2)
val RosaClaro = Color(0xFFF1DCE3)
val VerdeAzuladoClaro = Color(0xFFD6EBE8)
val TerracotaClaro = Color(0xFFF0E1DC)

// --- Fondo y textos ---
val FondoApp = Color(0xFFFBFAFD)
val TextoPrincipal = Color(0xFF26233A)
val TextoSecundario = Color(0xFF4A4660)
