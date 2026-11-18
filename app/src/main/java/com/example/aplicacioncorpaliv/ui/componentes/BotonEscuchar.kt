package com.example.aplicacioncorpaliv.ui.componentes

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.viewmodel.AccesibilidadViewModel

// COMPONENTE: botón 'Escuchar' que lee en voz alta el texto que se le entrega.
// EXPLICAR: se usa así en cualquier pantalla:   BotonEscuchar(texto = "Lo que se quiere leer")
// - Solo se muestra si la opción 'Leer en voz alta' está activada en la pantalla Accesibilidad.
// - Si se está leyendo ese texto, el botón cambia a 'Detener'.
// - Si se cambia de pantalla mientras lee, se calla solo (DisposableEffect).
@Composable
fun BotonEscuchar(
    texto: String,
    modifier: Modifier = Modifier,
    accesibilidadViewModel: AccesibilidadViewModel = viewModel()
) {
    // Lee la opción guardada (true = botones visibles)
    val activado by accesibilidadViewModel.lecturaVoz.collectAsState()
    // El lector de voz compartido (viene de MainActivity)
    val lector = LocalLectorVoz.current
    val context = LocalContext.current

    if (activado && lector != null) {
        // Cuando este botón sale de la pantalla (se cambia de sección), se corta la lectura
        DisposableEffect(Unit) {
            onDispose { lector.detener() }
        }

        // true si el lector está leyendo justo ESTE texto
        val leyendo = lector.textoActual == texto

        OutlinedButton(
            onClick = {
                if (leyendo) {
                    lector.detener()
                } else if (!lector.leer(texto)) {
                    // leer() devuelve false si el teléfono no tiene voz en español instalada
                    Toast.makeText(
                        context,
                        "No se pudo activar la voz. Revisa que tu teléfono tenga una voz en español.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = modifier
        ) {
            Icon(
                imageVector = if (leyendo) Icons.Filled.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (leyendo) "Detener" else "Escuchar")
        }
    }
}
