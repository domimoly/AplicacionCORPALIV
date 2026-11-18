package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// COMPONENTE: tarjeta que se despliega al tocarla (la usa la pantalla Programas).
// EXPLICAR: 'abierto' guarda si la tarjeta está desplegada. Al tocarla cambia true/false y la pantalla se redibuja.
// AnimatedVisibility hace que los detalles aparezcan y desaparezcan con una animación (expandir + desvanecer).
// 'contenido' es un bloque de composables que cada pantalla pasa entre llaves { } con los detalles de la tarjeta.
@Composable
fun TarjetaExpandible(
    icono: ImageVector,
    colorFondoIcono: Color,
    colorIcono: Color,
    titulo: String,
    resumen: String,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    // rememberSaveable: si se gira el teléfono, la tarjeta sigue abierta o cerrada como estaba
    var abierto by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // Parte siempre visible: círculo con ícono + título + resumen + flecha. Toda la fila es tocable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { abierto = !abierto }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colorFondoIcono),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // weight(1f): el texto ocupa todo el espacio sobrante y la flecha queda a la derecha
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = resumen,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (abierto) "Ocultar detalles" else "Ver detalles"
                )
            }

            // Parte que se despliega
            AnimatedVisibility(
                visible = abierto,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = contenido
                )
            }
        }
    }
}

// Una línea de detalle con viñeta. Se usa dentro de TarjetaExpandible
@Composable
fun ItemDetalle(texto: String) {
    Row {
        Text(
            text = "•",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
