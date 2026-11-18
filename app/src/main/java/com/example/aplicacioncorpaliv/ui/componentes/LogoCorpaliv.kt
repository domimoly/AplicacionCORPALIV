package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.R
import com.example.aplicacioncorpaliv.ui.theme.LocalAltoContraste

// COMPONENTE: el logo de CORPALIV en un solo lugar (Bienvenida, Inicio y menú lateral lo reutilizan).
// EXPLICAR: las letras del logo son gris oscuro, y sobre el fondo negro del alto contraste no se verían.
// Por eso, si el alto contraste está activado, el logo se muestra sobre una placa blanca con esquinas redondeadas.
@Composable
fun LogoCorpaliv(
    modifier: Modifier = Modifier
) {
    val altoContraste = LocalAltoContraste.current

    Image(
        painter = painterResource(id = R.drawable.logo_corpaliv),
        contentDescription = "Logo CORPALIV",
        modifier = if (altoContraste) {
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(8.dp)
        } else {
            modifier
        }
    )
}
