package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.viewmodel.FeriadosViewModel

// COMPONENTE: tarjeta con los próximos feriados de Chile, que vienen de la API externa Nager.Date.
// EXPLICAR: sirve de ayuda para elegir la 'fecha preferida de contacto' del formulario (así se evitan días no hábiles).
// Si no hay internet, solo se muestra el aviso con el botón 'Reintentar'; el formulario sigue funcionando igual.
@Composable
fun TarjetaFeriados(
    modifier: Modifier = Modifier,
    viewModel: FeriadosViewModel = viewModel()
) {
    val feriados by viewModel.feriados.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        AvisoConexion(cargando = cargando, error = error, onReintentar = { viewModel.cargar() })

        // ?.let { } dibuja la tarjeta solo cuando ya llegaron los datos
        feriados?.let { lista ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Próximos feriados en Chile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (lista.isEmpty()) {
                        Text("No quedan feriados este año.")
                    }
                    lista.forEach { feriado ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(feriado.localName, modifier = Modifier.weight(1f).padding(end = 8.dp))
                            // La fecha llega como 'aaaa-mm-dd'; se muestra como 'dd/mm/aaaa'
                            Text(
                                text = textoFecha(feriado.date),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Text(
                        text = "Fuente: Nager.Date",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// '2026-12-08' -> '08/12/2026'
private fun textoFecha(fecha: String): String {
    val partes = fecha.split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else fecha
}