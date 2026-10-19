package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.viewmodel.ContactoViewModel
import kotlinx.coroutines.delay

@Composable
fun ContactoScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactoViewModel = viewModel()
) {
    val formulario by
    viewModel.formulario.collectAsState()

    val errores by
    viewModel.errores.collectAsState()

    val guardadoCorrectamente by
    viewModel.guardadoCorrectamente.collectAsState()

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
            text = "Contacto",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Déjanos tus datos para comunicarnos contigo"
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
            trailingIcon = {
                if (errores.nombre != null) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error"
                    )
                }
            },
            supportingText = { errores.nombre?.let { Text(it) } }
        )
        OutlinedTextField(
            value = formulario.correo,
            onValueChange = { viewModel.actualizarCorreo(it) },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email ),
            isError = errores.correo != null,
            trailingIcon = {
                if (errores.correo != null) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error"
                    )
                }
            },
            supportingText = { errores.correo?.let { Text(it) } }
        )
        OutlinedTextField(
            value = formulario.mensaje,
            onValueChange = { viewModel.actualizarMensaje(it) },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            isError = errores.mensaje != null,
            trailingIcon = {
                if (errores.mensaje != null) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error"
                    )
                }
            },
            supportingText = { errores.mensaje?.let { Text(it) }}
        )

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
                    contentDescription = "Enviado correctamente",
                    tint = Color(0xFF2E7D32)
                )
                Text(
                    text = "¡Mensaje enviado correctamente!",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer( modifier = Modifier.height(10.dp) )

        Button( onClick = { viewModel.guardar() },
            modifier = Modifier.fillMaxWidth() )
        {
            Text("Enviar" )
        }
    }
}