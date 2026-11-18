package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.ui.theme.AmarilloAlto
import com.example.aplicacioncorpaliv.ui.theme.LocalAltoContraste
import com.example.aplicacioncorpaliv.ui.theme.MoradoOla

// COMPONENTE DECORATIVO: la ola de color con líneas blancas que va arriba en Bienvenida e Inicio.
// EXPLICAR: Canvas es un "lienzo" donde se dibuja con código (formas, líneas, curvas).
// No es una imagen: se dibuja sola y se ajusta al ancho de cualquier pantalla.
@Composable
fun OlaDecorativa(
    modifier: Modifier = Modifier,
    alto: Dp = 180.dp
) {
    // EXPLICAR: los colores dependen de si el alto contraste está activado (amarillo con líneas negras)
    // o no (morado con líneas blancas)
    val altoContraste = LocalAltoContraste.current
    val color = if (altoContraste) AmarilloAlto else MoradoOla
    val colorLineas = if (altoContraste) Color.Black else Color.White

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(alto)
    ) {
        // AVANZADO: el dibujo se diseñó en una cuadrícula imaginaria de 300 x 190.
        // ax y ay convierten esas medidas al tamaño real del lienzo (así se estira sin deformarse mal).
        val ax = size.width / 300f
        val ay = size.height / 190f

        // 1) El relleno: se parte en la esquina de arriba a la izquierda, se baja por el borde derecho
        // y se vuelve por el borde de abajo con dos curvas (cubicTo = curva con 2 puntos de control).
        val relleno = Path().apply {
            moveTo(0f, 0f)
            lineTo(300f * ax, 0f)
            lineTo(300f * ax, 120f * ay)
            cubicTo(262f * ax, 168f * ay, 214f * ax, 112f * ay, 152f * ax, 142f * ay)
            cubicTo(98f * ax, 168f * ay, 48f * ax, 186f * ay, 0f, 148f * ay)
            close()
        }
        drawPath(path = relleno, color = color)

        // 2) Las líneas blancas: la MISMA curva del borde, repetida más arriba cada vez.
        // Esta lista dice cuánto sube cada línea (la separación crece, como en un mapa topográfico).
        val subidas = listOf(14f, 30f, 49f, 71f, 96f)
        subidas.forEach { subida ->
            val linea = Path().apply {
                moveTo(300f * ax, (120f - subida) * ay)
                cubicTo(
                    262f * ax, (168f - subida) * ay,
                    214f * ax, (112f - subida) * ay,
                    152f * ax, (142f - subida) * ay
                )
                cubicTo(
                    98f * ax, (168f - subida) * ay,
                    48f * ax, (186f - subida) * ay,
                    0f, (148f - subida) * ay
                )
            }
            drawPath(
                path = linea,
                color = colorLineas.copy(alpha = 0.6f),   // algo transparente para que sea suave
                style = Stroke(width = 1.5.dp.toPx())      // Stroke = solo el contorno, sin rellenar
            )
        }
    }
}
