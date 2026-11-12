package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.ui.componentes.LogoCorpaliv
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa
import com.example.aplicacioncorpaliv.ui.theme.AzulClaro
import com.example.aplicacioncorpaliv.ui.theme.AzulOscuro
import com.example.aplicacioncorpaliv.ui.theme.MoradoClaro
import com.example.aplicacioncorpaliv.ui.theme.MoradoOscuro
import com.example.aplicacioncorpaliv.ui.theme.RosaClaro
import com.example.aplicacioncorpaliv.ui.theme.RosaOscuro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoClaro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoOscuro

// Una cifra ficticia de impacto | el número grande y su descripción
private data class Cifra(
    val valor: String,
    val detalle: String
)

// Un acceso rápido (círculo con ícono): qué ícono, qué texto, de qué color y a qué pantalla lleva
private data class Acceso(
    val icono: ImageVector,
    val texto: String,
    val pantalla: String,
    val fondo: Color,
    val colorIcono: Color
)

// onPantallaSeleccionada es una función que recibe desde App.kt y al llamarla con un nombre ('Colaborar'), App cambia de pantalla
@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    onPantallaSeleccionada: (String) -> Unit
) {
    // Datos de EJEMPLO (ficticios). Para cambiar una cifra o su texto, se edita esta lista
    val cifras = listOf(
        Cifra("40", "personas en formación"),
        Cifra("8", "empresas aliadas"),
        Cifra("250", "productos elaborados")
    )

    // EXPLICAR: los 4 círculos de acceso rápido salen de esta lista. Cada uno lleva a una pantalla
    // (el texto de 'pantalla' debe ser igual al del 'when' de App.kt). Cada círculo usa un color distinto
    val accesos = listOf(
        Acceso(Icons.Filled.Favorite, "Donar", "Colaborar", RosaClaro, RosaOscuro),
        Acceso(Icons.Filled.School, "Quiénes", "Quiénes somos", MoradoClaro, MoradoOscuro),
        Acceso(Icons.Filled.Groups, "Programas", "Programas", VerdeAzuladoClaro, VerdeAzuladoOscuro),
        Acceso(Icons.Filled.ShoppingBag, "Productos", "Productos", AzulClaro, AzulOscuro)
    )

    // Todo el contenido va en una columna con scroll vertical por si la pantalla es pequeña
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // La ola va pegada a la barra superior (tienen el mismo color) y ocupa todo el ancho
        OlaDecorativa(alto = 130.dp)

        // El resto del contenido sí lleva margen a los lados
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // El logo es ancho: se fija el ANCHO y el alto se calcula solo respetando su proporción
            LogoCorpaliv(modifier = Modifier.width(220.dp))

            Text(
                text = "Hola, ¿cómo quieres ayudar?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Fila de círculos de acceso rápido. forEach dibuja uno por cada elemento de la lista
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                accesos.forEach { acceso ->
                    AccesoRapido(
                        acceso = acceso,
                        onClick = { onPantallaSeleccionada(acceso.pantalla) }
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Hace 32 años promovemos la inclusión social y laboral de personas con discapacidad múltiple. Tu aporte puede marcar la diferencia.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Nuestro impacto (datos de ejemplo)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // forEach recorre la lista | por cada cifra dibuja una tarjeta. Weight(1f) reparte el ancho en partes iguales
                cifras.forEach { cifra ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = cifra.valor,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = cifra.detalle,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Botón 'Quiero colaborar' | llama a la función con el nombre de la pantalla destino
            Button(
                onClick = { onPantallaSeleccionada("Colaborar") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quiero colaborar")
            }

            OutlinedButton(
                onClick = { onPantallaSeleccionada("Contacto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contáctanos")
            }
        }
    }
}

// Un acceso rápido: círculo de color con un ícono y su nombre abajo. Toda la columna es clickeable
@Composable
private fun AccesoRapido(
    acceso: Acceso,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Box = caja que apila su contenido. clip(CircleShape) la recorta en círculo
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(acceso.fondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = acceso.icono,
                contentDescription = null,
                tint = acceso.colorIcono
            )
        }
        Text(
            text = acceso.texto,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}
