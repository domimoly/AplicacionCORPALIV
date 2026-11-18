package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
import com.example.aplicacioncorpaliv.ui.componentes.EtiquetaEstado
import com.example.aplicacioncorpaliv.viewmodel.RegistrosViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// PANTALLA MIS SOLICITUDES (antes 'Registros de interés').
// EXPLICAR: muestra las colaboraciones que la persona envió desde 'Colaborar' y, para cada una, EN QUÉ ESTADO VA
// (Recibida, En revisión, Contactado, Concretado o Descartado) y el mensaje del equipo si lo hay.
// El estado lo cambia el gestor desde 'Acceso gestores'. También se pueden ver los mensajes enviados desde 'Contacto'.
@Composable
fun RegistrosScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrosViewModel = viewModel()
) {
    var message by remember { mutableStateOf("") }
    val registros by viewModel.registros.collectAsState(initial = emptyList())
    val contactos by viewModel.contactos.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Mis solicitudes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text("Revisa en qué estado va cada solicitud que enviaste a CORPALIV")

        if (message.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(message, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.height(20.dp))
        Text(
            text = "Colaboraciones (${registros.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        if (registros.isEmpty()) {
            Text("Todavía no hay colaboraciones. Envía la primera desde Colaborar.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(count = registros.size, key = { index -> registros[index].id }) { index ->
                    val registro = registros[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            // Nombre a la izquierda y etiqueta del estado a la derecha
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = registro.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                EtiquetaEstado(estado = registro.estado)
                            }
                            Spacer(Modifier.height(4.dp))
                            // Frase simple que explica qué significa el estado actual
                            Text(
                                text = EstadoSolicitud.descripcion(registro.estado),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            // Mensaje del gestor (solo aparece si escribió una observación)
                            if (registro.observacion.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Mensaje del equipo: ${registro.observacion}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("${registro.tipo} - ${registro.monto} CLP")
                            Spacer(Modifier.height(4.dp))
                            Text(registro.mensaje)
                            Spacer(Modifier.height(4.dp))
                            // Fecha en que se envió (createdAt se guarda como número de milisegundos; aquí se convierte a texto)
                            Text(
                                text = "Enviada el " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    .format(Date(registro.createdAt)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.eliminar(registro)
                                        message = "Solicitud eliminada."
                                    }
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Mensajes de contacto (${contactos.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        if (contactos.isEmpty()) {
            Text("Todavía no hay mensajes. Envía el primero desde Contacto.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(count = contactos.size, key = { index -> contactos[index].id }) { index ->
                    val contacto = contactos[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(contacto.nombre, style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(contacto.correo)
                            Spacer(Modifier.height(4.dp))
                            Text(contacto.mensaje)
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.eliminarContacto(contacto)
                                        message = "Mensaje eliminado."
                                    }
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
