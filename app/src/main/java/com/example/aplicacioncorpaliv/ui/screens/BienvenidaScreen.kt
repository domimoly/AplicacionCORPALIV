package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.ui.componentes.LogoCorpaliv
import com.example.aplicacioncorpaliv.ui.componentes.BotonEscuchar
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa

// PANTALLA DE BIENVENIDA: se muestra una sola vez al abrir la app y da contexto de qué es CORPALIV.
// EXPLICAR: onContinuar es una función que recibe desde App.kt. Al presionar el botón se llama,
// y App.kt cambia su estado para mostrar la app normal (ver 'mostrarBienvenida' en App.kt).
@Composable
fun BienvenidaScreen(
    onContinuar: () -> Unit
) {
    // Todo va en una columna con scroll (así funciona también en pantallas chicas o en horizontal)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // La ola ocupa el ancho completo (queda detrás de la barra de estado del teléfono)
        OlaDecorativa(alto = 200.dp)

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // El logo es ancho: se fija el ANCHO y el alto se calcula solo respetando su proporción
            LogoCorpaliv(modifier = Modifier.width(260.dp))

            Text(
                text = "Bienvenido a CORPALIV",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // El "32 años" viene del logo oficial de la corporación
            Text(
                text = "Hace 32 años trabajamos por la inclusión social y laboral de personas con discapacidad múltiple.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // EXPLICAR: lee en voz alta el mensaje de bienvenida (solo se ve si 'Leer en voz alta' está activado)
            BotonEscuchar(
                texto = "Bienvenido a CORPALIV. Hace 32 años trabajamos por la inclusión social y laboral de personas con discapacidad múltiple. Con esta app puedes conocer nuestros programas y productos, colaborar con una donación o como voluntario, y escribirnos cuando quieras."
            )

            Spacer(modifier = Modifier.size(4.dp))

            // Lo que se puede hacer en la app (reutiliza el componente de más abajo)
            ItemBienvenida(Icons.Filled.Info, "Conoce nuestros programas y productos")
            ItemBienvenida(Icons.Filled.Favorite, "Colabora con una donación o como voluntario")
            ItemBienvenida(Icons.Filled.Email, "Escríbenos cuando quieras")
        }

        Spacer(modifier = Modifier.size(28.dp))

        Button(
            onClick = onContinuar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()   // deja espacio para los botones del sistema del teléfono
                .padding(bottom = 24.dp)
        ) {
            Text("Continuar")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

// Una línea con un ícono y un texto. Se usa 3 veces arriba; así no se repite el código
@Composable
private fun ItemBienvenida(
    icono: ImageVector,
    texto: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
