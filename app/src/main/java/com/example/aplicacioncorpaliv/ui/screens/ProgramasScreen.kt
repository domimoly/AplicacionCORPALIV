package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.aplicacioncorpaliv.ui.componentes.AvisoConexion
import com.example.aplicacioncorpaliv.ui.componentes.BotonEscuchar
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa
import com.example.aplicacioncorpaliv.ui.componentes.TarjetaExpandible
import com.example.aplicacioncorpaliv.ui.theme.AzulClaro
import com.example.aplicacioncorpaliv.ui.theme.AzulOscuro
import com.example.aplicacioncorpaliv.ui.theme.MoradoClaro
import com.example.aplicacioncorpaliv.ui.theme.MoradoOscuro
import com.example.aplicacioncorpaliv.ui.theme.RosaClaro
import com.example.aplicacioncorpaliv.ui.theme.RosaOscuro
import com.example.aplicacioncorpaliv.ui.theme.TerracotaClaro
import com.example.aplicacioncorpaliv.ui.theme.TerracotaOscuro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoClaro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoOscuro
import com.example.aplicacioncorpaliv.viewmodel.ProgramasViewModel

// Ícono y colores de cada programa según su categoría
private data class EstiloPrograma(
    val icono: ImageVector,
    val colorFondo: Color,
    val colorIcono: Color
)

// EXPLICAR: el servidor entrega cada programa con su categoría (por ejemplo "Educación").
// Esta función elige el ícono y los colores según esa categoría. Si llega una categoría nueva, usa el estilo del 'else'.
private fun estiloPrograma(categoria: String): EstiloPrograma = when (categoria) {
    "Educación" -> EstiloPrograma(Icons.Filled.School, MoradoClaro, MoradoOscuro)
    "Formación laboral" -> EstiloPrograma(Icons.Filled.Groups, VerdeAzuladoClaro, VerdeAzuladoOscuro)
    "Inclusión laboral" -> EstiloPrograma(Icons.Filled.Work, AzulClaro, AzulOscuro)
    "Servicios a empresas" -> EstiloPrograma(Icons.Filled.Business, TerracotaClaro, TerracotaOscuro)
    else -> EstiloPrograma(Icons.Filled.Info, RosaClaro, RosaOscuro)
}

// PANTALLA PROGRAMAS: cada programa es una tarjeta que se despliega al tocarla (componente TarjetaExpandible).
// EXPLICAR: los programas ya NO están escritos en la pantalla: se piden al microservicio ms-contenidos con Retrofit
// a través de ProgramasViewModel. Si el servidor no responde, se muestran datos de respaldo y un botón 'Reintentar'.
// Para agregar un programa nuevo ya no se toca el código: se crea en el backend.
// onPantallaSeleccionada es la función de App.kt que cambia de pantalla (aquí lleva a 'Contacto').
@Composable
fun ProgramasScreen(
    modifier: Modifier = Modifier,
    onPantallaSeleccionada: (String) -> Unit = {},
    viewModel: ProgramasViewModel = viewModel()
) {
    val programas by viewModel.programas.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // La misma ola del Inicio, más baja, para que todas las pantallas se vean parte de la misma app
        OlaDecorativa(alto = 100.dp)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Nuestros programas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Así promovemos la autonomía, la participación y la inclusión social y laboral. Toca cada programa para ver más detalles.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Barra de carga y mensaje de error (con botón Reintentar)
            AvisoConexion(
                cargando = cargando,
                error = error,
                onReintentar = { viewModel.cargar() }
            )

            // El texto a leer se arma con los programas que llegaron (joinToString junta los nombres en una frase)
            BotonEscuchar(
                texto = "Nuestros programas. " + programas.joinToString(". ") { programa ->
                    programa.nombre + ", " + programa.resumen
                }
            )

            // EXPLICAR: forEach repite una tarjeta por cada programa de la lista, sin importar cuántos haya
            programas.forEach { programa ->
                val estilo = estiloPrograma(programa.categoria)
                TarjetaExpandible(
                    icono = estilo.icono,
                    colorFondoIcono = estilo.colorFondo,
                    colorIcono = estilo.colorIcono,
                    titulo = programa.nombre,
                    resumen = programa.resumen
                ) {
                    // Coil (AsyncImage) descarga y muestra la imagen del programa solo si el servidor entrega una URL
                    if (!programa.imagenUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = programa.imagenUrl,
                            contentDescription = programa.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Text(programa.descripcion)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botón final: lleva a la pantalla Contacto
            Button(
                onClick = { onPantallaSeleccionada("Contacto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quiero que me contacten")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
