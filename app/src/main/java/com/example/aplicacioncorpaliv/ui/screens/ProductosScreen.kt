package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.aplicacioncorpaliv.viewmodel.ProductosViewModel

// Ícono y colores del círculo de cada producto, según su categoría
private data class EstiloProducto(
    val icono: ImageVector,
    val colorFondo: Color,
    val colorIcono: Color
)

// EXPLICAR: el servidor entrega cada producto con el NOMBRE de su categoría. Esta función elige el ícono y los colores
// según ese nombre. Si llega una categoría nueva, usa el estilo del 'else'.
private fun estiloProducto(categoria: String): EstiloProducto = when (categoria) {
    "Bienestar" -> EstiloProducto(Icons.Filled.Spa, VerdeAzuladoClaro, VerdeAzuladoOscuro)
    "Servicios" -> EstiloProducto(Icons.Filled.School, AzulClaro, AzulOscuro)
    "Otros" -> EstiloProducto(Icons.Filled.ShoppingBag, MoradoClaro, MoradoOscuro)
    else -> EstiloProducto(Icons.Filled.Lightbulb, TerracotaClaro, TerracotaOscuro)
}

// PANTALLA PRODUCTOS: catálogo con filtro por categoría.
// EXPLICAR: los productos ya NO están escritos en la pantalla: vienen del microservicio ms-catalogo (Retrofit) a través
// de ProductosViewModel. Son DATOS DE EJEMPLO (precios referenciales), no un catálogo oficial.
// Si el servidor no responde, se muestran datos de respaldo y un botón 'Reintentar'.
@Composable
fun ProductosScreen(
    modifier: Modifier = Modifier,
    onPantallaSeleccionada: (String) -> Unit = {},
    viewModel: ProductosViewModel = viewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    // EXPLICAR: las categorías salen de la propia lista (map + distinct), así que si se agrega un producto
    // con una categoría nueva, aparece sola como filtro. 'Todos' se agrega al principio.
    val categorias = listOf("Todos") + productos.map { it.categoria }.distinct()

    // Guarda cuál filtro está elegido. Cuando cambia, la pantalla se redibuja con la lista filtrada
    var filtro by rememberSaveable { mutableStateOf("Todos") }

    // filter deja solo los productos de la categoría elegida (o todos si el filtro es 'Todos')
    val visibles = if (filtro == "Todos") productos else productos.filter { it.categoria == filtro }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OlaDecorativa(alto = 100.dp)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Productos y servicios con impacto social",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Cada compra apoya la formación y el trabajo de jóvenes y adultos con discapacidad múltiple. Catálogo de ejemplo con precios referenciales.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Barra de carga y mensaje de error (con botón Reintentar)
            AvisoConexion(
                cargando = cargando,
                error = error,
                onReintentar = { viewModel.cargar() }
            )

            // EXPLICAR: el texto a leer se arma con los productos que se ven en ese momento (según el filtro).
            // joinToString junta cada 'nombre, precio' en una sola frase separada por puntos.
            BotonEscuchar(
                texto = "Productos y servicios. " + visibles.joinToString(". ") { producto ->
                    // Se cambia el signo $ por la palabra 'pesos' para que la voz lo lea bien
                    val precioHablado = if (producto.precio.startsWith("$")) producto.precio.drop(1) + " pesos" else producto.precio
                    "${producto.nombre}, $precioHablado"
                }
            )

            // Filtros: una fila con scroll horizontal por si no caben todos (por ejemplo con texto grande)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { categoria ->
                    FilterChip(
                        selected = filtro == categoria,
                        onClick = { filtro = categoria },
                        label = { Text(categoria) }
                    )
                }
            }

            // animateContentSize anima el cambio de altura cuando el filtro muestra más o menos productos
            Column(
                modifier = Modifier.animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                visibles.forEach { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            val estilo = estiloProducto(producto.categoria)
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(estilo.colorFondo),
                                contentAlignment = Alignment.Center
                            ) {
                                // Si el servidor entrega una imagen (Coil la descarga) se muestra; si no, el ícono de la categoría
                                if (!producto.imagenUrl.isNullOrBlank()) {
                                    AsyncImage(
                                        model = producto.imagenUrl,
                                        contentDescription = producto.nombre,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(48.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = estilo.icono,
                                        contentDescription = null,
                                        tint = estilo.colorIcono
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = producto.categoria,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = producto.descripcion,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = producto.precio,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    // 'Solicitar' lleva al formulario de Colaborar
                                    TextButton(onClick = { onPantallaSeleccionada("Colaborar") }) {
                                        Text("Solicitar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
