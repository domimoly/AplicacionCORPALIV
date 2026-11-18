package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
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

// COMPONENTE: etiqueta de color con el estado de una solicitud (Recibida, En revisión, etc.).
// EXPLICAR: la usan dos pantallas (Mis solicitudes y el panel del gestor), por eso es un componente aparte.
// Uso:   EtiquetaEstado(estado = registro.estado)
// El color no es lo único que distingue cada estado: siempre lleva también el texto, para que se entienda sin depender del color.
@Composable
fun EtiquetaEstado(
    estado: String,
    modifier: Modifier = Modifier
) {
    // 'to' arma un par (color de fondo, color del texto). Se separa en dos variables con los paréntesis
    val (fondo, colorTexto) = when (estado) {
        EstadoSolicitud.RECIBIDA -> AzulClaro to AzulOscuro
        EstadoSolicitud.EN_REVISION -> TerracotaClaro to TerracotaOscuro
        EstadoSolicitud.CONTACTADO -> MoradoClaro to MoradoOscuro
        EstadoSolicitud.CONCRETADO -> VerdeAzuladoClaro to VerdeAzuladoOscuro
        EstadoSolicitud.DESCARTADO -> RosaClaro to RosaOscuro
        else -> AzulClaro to AzulOscuro
    }

    Surface(
        modifier = modifier,
        color = fondo,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = EstadoSolicitud.texto(estado),
            color = colorTexto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
