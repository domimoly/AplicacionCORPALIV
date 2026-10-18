package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.R
import com.example.aplicacioncorpaliv.ui.theme.Negro

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Greeting(
            name = "CORPALIV"
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.logo_corpaliv),
            contentDescription = "Logo CORPALIV",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Bienvenido a $name!",
        color = Negro,
        modifier = modifier
    )
}