package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.ui.componentes.BotonEscuchar
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa
import com.example.aplicacioncorpaliv.ui.componentes.TarjetaExpandible
import com.example.aplicacioncorpaliv.ui.theme.AzulClaro
import com.example.aplicacioncorpaliv.ui.theme.AzulOscuro
import com.example.aplicacioncorpaliv.ui.theme.MoradoClaro
import com.example.aplicacioncorpaliv.ui.theme.MoradoOscuro
import com.example.aplicacioncorpaliv.ui.theme.RosaClaro
import com.example.aplicacioncorpaliv.ui.theme.RosaOscuro
import com.example.aplicacioncorpaliv.viewmodel.ImpactoViewModel

// PANTALLA IMPACTO: muestra los indicadores (números) y las historias de impacto de CORPALIV.
// EXPLICAR: la pantalla NO tiene los datos escritos: los pide a ImpactoViewModel con collectAsState.
// Así, cuando los datos vengan del backend, esta pantalla casi no cambia.
// Las historias usan TarjetaExpandible, el mismo componente de la pantalla Programas.
@Composable
fun ImpactoScreen(
    modifier: Modifier = Modifier,
    onPantallaSeleccionada: (String) -> Unit = {},
    viewModel: ImpactoViewModel = viewModel()
) {
    val indicadores by viewModel.indicadores.collectAsState()
    val historias by viewModel.historias.collectAsState()

    // Colores que se van repitiendo en las historias (fondo del círculo, color del ícono)
    val colores = listOf(
        RosaClaro to RosaOscuro,
        MoradoClaro to MoradoOscuro,
        AzulClaro to AzulOscuro
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // La misma ola de las demás pantallas
        OlaDecorativa(alto = 100.dp)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Nuestro impacto",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Lo que logramos gracias a quienes confían en CORPALIV.",
                style = MaterialTheme.typography.bodyMedium
            )
            BotonEscuchar(
                texto = "Nuestro impacto. Lo que logramos gracias a quienes confían en CORPALIV. " +
                        "Hace 32 años promovemos la inclusión social y laboral de personas con discapacidad múltiple."
            )

            // ---------- INDICADORES ----------
            Text(
                text = "Indicadores",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            // EXPLICAR: chunked(2) parte la lista en grupos de 2. Cada grupo es una fila con 2 tarjetas.
            // Si queda una sola tarjeta en la fila, se agrega un Spacer del mismo tamaño para que no se estire.
            indicadores.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fila.forEach { indicador ->
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = indicador.valor.toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = indicador.unidad,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = indicador.nombre,
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Período ${indicador.periodo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    if (fila.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // ---------- HISTORIAS ----------
            Text(
                text = "Historias de impacto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // forEachIndexed da la posición de cada historia; con '%' los colores se repiten cuando se acaban
            historias.forEachIndexed { indice, historia ->
                val (fondo, colorIcono) = colores[indice % colores.size]
                TarjetaExpandible(
                    icono = Icons.Filled.Favorite,
                    colorFondoIcono = fondo,
                    colorIcono = colorIcono,
                    titulo = historia.titulo,
                    resumen = historia.resumen
                ) {
                    Text(historia.contenido)
                    BotonEscuchar(texto = historia.titulo + ". " + historia.contenido)
                }
            }

            Text(
                text = "Cifras e historias de ejemplo para esta versión de la app. Las historias reales se publican solo con autorización y sin datos personales.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { onPantallaSeleccionada("Colaborar") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quiero colaborar")
            }
        }
    }
}
