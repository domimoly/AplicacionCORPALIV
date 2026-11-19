package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// COMPONENTE: muestra el estado de la conexión con el servidor. Lo usan Programas, Productos e Impacto.
// EXPLICAR: si 'cargando' es true, aparece una barra de progreso. Si hay un mensaje de error, aparece una tarjeta
// con el mensaje y un botón 'Reintentar' que vuelve a pedir los datos. Si no pasa nada de eso, no se dibuja nada.
@Composable
fun AvisoConexion(
    cargando: Boolean,
    error: String?,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (cargando) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    TextButton(onClick = onReintentar) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}
