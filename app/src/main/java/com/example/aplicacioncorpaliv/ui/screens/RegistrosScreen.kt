package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.model.CodigoSeguimiento
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import com.example.aplicacioncorpaliv.ui.componentes.EtiquetaEstado
import com.example.aplicacioncorpaliv.viewmodel.RegistrosViewModel
import com.example.aplicacioncorpaliv.viewmodel.ResultadoConsulta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// PANTALLA MIS SOLICITUDES (antes 'Registros de interés').
// EXPLICAR: muestra las colaboraciones que la persona envió desde 'Colaborar' y, para cada una, EN QUÉ ESTADO VA
// (Recibida, En revisión, Contactado, Concretado o Descartado) y el mensaje del equipo si lo hay.
// Arriba hay una caja para consultar una solicitud con su CÓDIGO DE SEGUIMIENTO (sin necesitar cuenta).
// El estado lo cambia el gestor desde 'Acceso gestores'. También se pueden ver los mensajes enviados desde 'Contacto'.
// AVANZADO: toda la pantalla es UNA sola LazyColumn con secciones (item { }) para que se pueda desplazar completa.
@Composable
fun RegistrosScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrosViewModel = viewModel()
) {
    var message by remember { mutableStateOf("") }
    var codigoEscrito by remember { mutableStateOf("") }
    val registros by viewModel.registros.collectAsState(initial = emptyList())
    val contactos by viewModel.contactos.collectAsState(initial = emptyList())
    val consulta by viewModel.consulta.collectAsState()
    val actualizando by viewModel.actualizando.collectAsState()
    val aviso by viewModel.aviso.collectAsState()

    // EXPLICAR: LaunchedEffect(Unit) se ejecuta UNA vez al abrir la pantalla: pide al servidor los estados al día
    LaunchedEffect(Unit) { viewModel.actualizar() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
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
            aviso?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        // ---- Consulta con código de seguimiento ----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Consultar con mi código",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = codigoEscrito,
                        // Se normaliza (mayúsculas y sin espacios) para que 'col-7kq4m2xa ' también funcione
                        onValueChange = { codigoEscrito = CodigoSeguimiento.normalizar(it) },
                        label = { Text("Código (por ejemplo COL-7KQ4M2XA)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.consultarPorCodigo(codigoEscrito) }) {
                            Text("Consultar")
                        }
                        if (consulta != ResultadoConsulta.SinConsultar) {
                            TextButton(onClick = {
                                viewModel.limpiarConsulta()
                                codigoEscrito = ""
                            }) {
                                Text("Limpiar")
                            }
                        }
                    }
                    // EXPLICAR: 'when' revisa cuál de los resultados posibles hay y muestra algo distinto en cada caso
                    when (val resultado = consulta) {
                        ResultadoConsulta.SinConsultar -> {}
                        ResultadoConsulta.CodigoInvalido ->
                            Text(
                                "El código no tiene el formato correcto (COL- y 8 caracteres).",
                                color = MaterialTheme.colorScheme.error
                            )
                        ResultadoConsulta.NoEncontrada ->
                            Text(
                                "No encontramos una solicitud con ese código.",
                                color = MaterialTheme.colorScheme.error
                            )
                        ResultadoConsulta.Consultando -> CircularProgressIndicator()
                        is ResultadoConsulta.Error ->
                            Text(resultado.mensaje, color = MaterialTheme.colorScheme.error)
                        is ResultadoConsulta.Encontrada ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    DetalleSeguimiento(resultado.seguimiento)
                                }
                            }
                    }
                }
            }
        }

        // ---- Lista de solicitudes ----
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Colaboraciones (${registros.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (registros.isEmpty()) {
                Text("Todavía no hay colaboraciones. Envía la primera desde Colaborar.")
            } else {
                // Vuelve a preguntar al servidor por el estado de cada solicitud
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { viewModel.actualizar() }, enabled = !actualizando) {
                        Text("Actualizar estados")
                    }
                    if (actualizando) {
                        Spacer(Modifier.width(12.dp))
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    }
                }
            }
        }
        items(count = registros.size, key = { index -> "r" + registros[index].id }) { index ->
            val registro = registros[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(12.dp)) {
                    DetalleSolicitud(registro)
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

        // ---- Mensajes enviados desde Contacto ----
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Mensajes de contacto (${contactos.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (contactos.isEmpty()) {
                Text("Todavía no hay mensajes. Envía el primero desde Contacto.")
            }
        }
        items(count = contactos.size, key = { index -> "c" + contactos[index].id }) { index ->
            val contacto = contactos[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        contacto.nombre, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
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

// Datos de una solicitud (se usa en la lista y en el resultado de la consulta con código).
// EXPLICAR: es una función aparte para no repetir el mismo bloque dos veces.
@Composable
private fun DetalleSolicitud(registro: Registro) {
    Column {
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
        // Código de seguimiento (se puede copiar manteniéndolo presionado). Las solicitudes antiguas no tienen código
        if (registro.codigoSeguimiento.isNotBlank()) {
            SelectionContainer {
                Text(
                    text = "Código: ${registro.codigoSeguimiento}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
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
        // El monto solo se muestra si hay uno (solo las donaciones lo tienen)
        val tipoTexto = TiposColaboracion.nombre(registro.tipo)
        Text(if (registro.monto > 0) "$tipoTexto - ${registro.monto} CLP" else tipoTexto)
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
    }
}

// Resultado de consultar con el código: el servidor solo entrega el estado (sin datos personales)
@Composable
private fun DetalleSeguimiento(seguimiento: Seguimiento) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = TiposColaboracion.nombre(seguimiento.tipo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            EtiquetaEstado(estado = seguimiento.estado)
        }
        SelectionContainer {
            Text(
                text = "Código: ${seguimiento.codigoSeguimiento}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = EstadoSolicitud.descripcion(seguimiento.estado),
            style = MaterialTheme.typography.bodyMedium
        )
        if (!seguimiento.observacion.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Mensaje del equipo: ${seguimiento.observacion}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
