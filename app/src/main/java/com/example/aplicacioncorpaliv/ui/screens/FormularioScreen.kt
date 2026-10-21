package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.aplicacioncorpaliv.viewmodel.FormularioViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel = viewModel()
) {
    val formulario by
    viewModel.formulario.collectAsState()

    val errores by
    viewModel.errores.collectAsState()

    val guardadoCorrectamente by
    viewModel.guardadoCorrectamente.collectAsState()

    var mostrarDatePicker by
    remember {
        mutableStateOf(false)
    }
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val categorias = listOf(
        "Donación",
        "Voluntariado",
        "Alianza empresarial",
        "Compra de productos",
        "Capacitación"
    )
    var categoriaExpandida by remember { mutableStateOf(false) }

    LaunchedEffect(guardadoCorrectamente) {
        if (guardadoCorrectamente) {
            delay(2000)
            viewModel.ocultarMensajeGuardado()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Quiero colaborar",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        OutlinedTextField(
            value = formulario.nombre,
            onValueChange = { viewModel.actualizarNombre(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errores.nombre != null,
            supportingText = { errores.nombre?.let { Text(it) } }
        )
        OutlinedTextField(
            value = formulario.descripcion,
            onValueChange = { viewModel.actualizarDescripcion(it) },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = errores.descripcion != null,
            supportingText = { errores.descripcion?.let { Text(it) } }
        )

        OutlinedTextField(
            value = formulario.cantidad,
            onValueChange = { viewModel.actualizarCantidad(it) },
            label = { Text("Monto de aporte (CLP)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errores.cantidad != null,
            supportingText = {
                if (errores.cantidad != null) {
                    Text(errores.cantidad!!)
                } else {
                    Text("Ingrese un valor entre 1000 y 10000")
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formulario.aceptaTerminos,
                onCheckedChange = { viewModel.actualizarAceptaTerminos(it) })
            Text(
                text = "Acepto ser contactado por CORPALIV"
            )
        }

        if (errores.aceptaTerminos != null) {
            Text(
                text = errores.aceptaTerminos!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Contacto prioritario")
            Switch(
                checked = formulario.activo,
                onCheckedChange = { viewModel.actualizarActivo(it) }
            )
        }

        val fechaTexto = formulario.fecha?.let {
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(Date(it))
        } ?: "Seleccionar fecha"

        OutlinedTextField(
            value = fechaTexto,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha preferida de contacto") },
            modifier = Modifier.fillMaxWidth(),
            isError = errores.fecha != null
        )
        Button(
            onClick = { mostrarDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar fecha")
        }

        if (errores.fecha != null) {
            Text(
                text = errores.fecha!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        ExposedDropdownMenuBox(
            expanded = categoriaExpandida,
            onExpandedChange = { categoriaExpandida = !categoriaExpandida })
        {
            OutlinedTextField(
                value = formulario.categoria,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de colaboración") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpandida) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                isError = errores.categoria != null
            )

            ExposedDropdownMenu(
                expanded = categoriaExpandida,
                onDismissRequest = { categoriaExpandida = false })
            {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        onClick = {
                            viewModel.actualizarCategoria(categoria)
                            categoriaExpandida = false
                        })
                }
            }
        }

        if (errores.categoria != null) {
            Text(text = errores.categoria!!, color = MaterialTheme.colorScheme.error)
        }

        AnimatedVisibility(
            visible = guardadoCorrectamente,
            enter = fadeIn() +
                    expandVertically(),
            exit = fadeOut() +
                    shrinkVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Guardado correctamente",
                    tint = Color(0xFF2E7D32)
                )
                Text(
                    text = "¡Registro guardado correctamente!",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                scope.launch {
                    cargando = true
                    delay(1200)
                    viewModel.guardar()
                    cargando = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardando...")
            } else {
                Text("Guardar")
            }
        }

        if (mostrarDatePicker) {
            val datePickerState =
                rememberDatePickerState(initialSelectedDateMillis = formulario.fecha)
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.actualizarFecha(datePickerState.selectedDateMillis)
                        mostrarDatePicker = false
                    })
                    {
                        Text("Aceptar")
                    }
                },

                dismissButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) {
                        Text("Cancelar")
                    }
                }) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}
