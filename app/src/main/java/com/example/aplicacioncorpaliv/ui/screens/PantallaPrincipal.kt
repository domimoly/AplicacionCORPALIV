package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.viewmodel.EstadoViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaPrincipal(
    modifier: Modifier = Modifier,
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val estadoGuardado by
    estadoViewModel.estadoActivado.collectAsState()

    val cargando by
    estadoViewModel.cargando.collectAsState()

    val guardadoCorrectamente by
    estadoViewModel.guardadoCorrectamente.collectAsState()

    val mensajeError by
    estadoViewModel.mensajeError.collectAsState()

    var estadoActivado by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(cargando, estadoGuardado) {
        if (!cargando) {
            estadoActivado = estadoGuardado
        }
    }

    LaunchedEffect(guardadoCorrectamente) {
        if (guardadoCorrectamente) {
            delay(2000)
            estadoViewModel.ocultarMensajeGuardado()
        }
    }

    val textoEstado by remember {
        derivedStateOf {
            if (estadoActivado) {
                "Novedades activadas"
            } else {
                "Novedades desactivadas"
            }
        }
    }

    val colorBoton by animateColorAsState(
        targetValue = if (estadoActivado) {
            Color(0xFF2E7D32)
        } else {
            Color(0xFF1565C0)
        },
        label = "Color del botón"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (cargando) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Cargando preferencia...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Novedades de CORPALIV",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = textoEstado,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (estadoActivado) {
                        Color(0xFF2E7D32)
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {
                        val nuevoEstado = !estadoActivado
                        estadoActivado = nuevoEstado
                        estadoViewModel.guardarEstado(nuevoEstado)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorBoton
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(220.dp)
                ) {
                    Text(
                        text = if (estadoActivado) {
                            "Desactivar novedades"
                        } else {
                            "Activar novedades"
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                AnimatedVisibility(
                    visible = guardadoCorrectamente,
                    enter = fadeIn() +
                            expandVertically(),
                    exit = fadeOut() +
                            shrinkVertically()
                ) {
                    Text(
                        text = "¡Preferencia guardada correctamente!",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(
                    visible = mensajeError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = mensajeError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}