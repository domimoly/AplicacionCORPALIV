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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import com.example.aplicacioncorpaliv.ui.componentes.EtiquetaEstado
import com.example.aplicacioncorpaliv.viewmodel.GestorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// PANTALLA ACCESO GESTORES: es para el personal de CORPALIV (gestores de vinculación).
// EXPLICAR: tiene DOS partes y la pantalla elige cuál mostrar según 'gestorActual' del ViewModel:
//  - Si nadie inició sesión (null): formulario de login (LoginGestor).
//  - Si ya inició sesión: panel con todas las solicitudes, donde puede cambiar su estado o eliminarlas (PanelGestor).
// Las solicitudes son las mismas de 'Mis solicitudes' (misma base de datos Room); cuando exista el backend,
// el panel las pedirá al microservicio ms-vinculacion con Retrofit.
@Composable
fun GestorScreen(
    modifier: Modifier = Modifier,
    viewModel: GestorViewModel = viewModel()
) {
    val gestorActual by viewModel.gestorActual.collectAsState()
    val errorLogin by viewModel.errorLogin.collectAsState()

    // Se copia a una variable normal para poder preguntar si es null y luego usar el texto sin problemas
    val nombreGestor = gestorActual

    if (nombreGestor == null) {
        LoginGestor(
            modifier = modifier,
            error = errorLogin,
            onIngresar = { correo, clave -> viewModel.iniciarSesion(correo, clave) }
        )
    } else {
        PanelGestor(
            modifier = modifier,
            nombreGestor = nombreGestor,
            viewModel = viewModel
        )
    }
}

// ============================== LOGIN ==============================
@Composable
private fun LoginGestor(
    modifier: Modifier,
    error: String?,
    onIngresar: (String, String) -> Unit
) {
    // remember: guarda lo que se escribe mientras la pantalla se redibuja. La clave NO se guarda con rememberSaveable a propósito
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mostrarClave by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Acceso para gestores",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Esta sección es para el equipo de CORPALIV que gestiona las solicitudes de colaboración.",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = error != null
        )

        // EXPLICAR: PasswordVisualTransformation dibuja puntos (•••) en vez de las letras. El ícono del ojo
        // cambia 'mostrarClave' para poder ver la clave escrita (ayuda a quien se equivoca al teclear)
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Clave") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (mostrarClave) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { mostrarClave = !mostrarClave }) {
                    Icon(
                        imageVector = if (mostrarClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (mostrarClave) "Ocultar clave" else "Mostrar clave"
                    )
                }
            },
            isError = error != null
        )

        // Mensaje de error del ViewModel (campos vacíos, correo inválido o datos incorrectos)
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = { onIngresar(correo, clave) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        // Ayuda solo para la versión de demostración (datos ficticios). Se quitará cuando haya login real con el backend
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Cuenta de demostración",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Correo: gestor@ejemplo.cl")
                Text("Clave: Gestor2026")
            }
        }
    }
}

// ============================== PANEL ==============================
@Composable
private fun PanelGestor(
    modifier: Modifier,
    nombreGestor: String,
    viewModel: GestorViewModel
) {
    val registros by viewModel.registros.collectAsState(initial = emptyList())

    // Solicitud que se está editando. Si es null, el cuadro de diálogo está cerrado
    var registroEditando by remember { mutableStateOf<Registro?>(null) }
    // Mensaje corto que confirma la última acción (cambió estado / eliminó)
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Panel de gestión",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Sesión iniciada: $nombreGestor",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = { viewModel.cerrarSesion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(Modifier.height(16.dp))
        // count { condición } cuenta cuántas solicitudes cumplen la condición (aquí, las que siguen en estado RECIBIDA)
        val porAtender = registros.count { it.estado == EstadoSolicitud.RECIBIDA }
        Text(
            text = "Solicitudes: ${registros.size} (por atender: $porAtender)",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        if (mensaje.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(8.dp))

        if (registros.isEmpty()) {
            Text("Todavía no hay solicitudes. Cuando alguien envíe el formulario Colaborar, aparecerá aquí.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // key = { it.id } ayuda a Compose a reconocer cada solicitud aunque cambie el orden o el contenido de la lista
                items(registros, key = { it.id }) { registro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
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
                            // El monto solo existe en las donaciones; el tipo se muestra con su nombre (no con el código)
                            val tipoTexto = TiposColaboracion.nombre(registro.tipo)
                            Text(if (registro.monto > 0) "$tipoTexto - ${registro.monto} CLP" else tipoTexto)
                            // El gestor necesita el medio de contacto para poder responder, y el código para identificar la solicitud
                            if (registro.medioContacto.isNotBlank()) {
                                Text("Contacto: ${registro.medioContacto}")
                            }
                            if (registro.codigoSeguimiento.isNotBlank()) {
                                Text("Código: ${registro.codigoSeguimiento}")
                            }
                            if (registro.prioritario) {
                                Text(
                                    text = "Contacto prioritario",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(registro.mensaje)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Enviada el " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    .format(Date(registro.createdAt)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (registro.observacion.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Observación: ${registro.observacion}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.eliminar(registro)
                                        mensaje = "Solicitud eliminada."
                                    }
                                ) {
                                    Text("Eliminar")
                                }
                                // Al tocar este botón se guarda la solicitud en 'registroEditando' y se abre el cuadro de diálogo
                                Button(onClick = { registroEditando = registro }) {
                                    Text("Cambiar estado")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ?.let { } ejecuta el bloque SOLO si registroEditando no es null, o sea, solo cuando hay un diálogo abierto
    registroEditando?.let { registro ->
        DialogCambiarEstado(
            registro = registro,
            onGuardar = { estado, observacion ->
                viewModel.cambiarEstado(registro, estado, observacion)
                mensaje = "Estado actualizado: ${EstadoSolicitud.texto(estado)}."
                registroEditando = null
            },
            onCancelar = { registroEditando = null }
        )
    }
}

// ============================== CUADRO DE DIÁLOGO ==============================
// EXPLICAR: ventana emergente (AlertDialog) para elegir el nuevo estado y, si se quiere, escribir un mensaje para la persona.
// Guarda lo elegido en variables propias y solo avisa a la pantalla cuando se toca 'Guardar' (onGuardar).
@Composable
private fun DialogCambiarEstado(
    registro: Registro,
    onGuardar: (String, String) -> Unit,
    onCancelar: () -> Unit
) {
    // remember(registro.id): si se abre el diálogo para otra solicitud, estas variables vuelven a empezar con sus datos
    var estadoElegido by remember(registro.id) { mutableStateOf(registro.estado) }
    var observacion by remember(registro.id) { mutableStateOf(registro.observacion) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Cambiar estado") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Solicitud de ${registro.nombre}")
                // Mismo patrón de opciones del tamaño de texto en Configuración: una fila tocable por cada opción
                Column(modifier = Modifier.selectableGroup()) {
                    EstadoSolicitud.todos.forEach { estado ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = estadoElegido == estado,
                                    onClick = { estadoElegido = estado },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = estadoElegido == estado,
                                onClick = null
                            )
                            Text(
                                text = EstadoSolicitud.texto(estado),
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = observacion,
                    // Se limita a 120 caracteres para que el mensaje sea corto y fácil de leer
                    onValueChange = { if (it.length <= 120) observacion = it },
                    label = { Text("Mensaje para la persona (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    supportingText = { Text("${observacion.length}/120") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(estadoElegido, observacion) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}
