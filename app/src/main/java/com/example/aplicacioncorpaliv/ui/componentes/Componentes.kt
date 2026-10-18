package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NuevoMenu(isExpanded: Boolean,onItemClick:(String)-> Unit,onDismiss:()->Unit){
    val opciones=listOf("Quiénes somos","Programas","Productos","Colaborar")
    DropdownMenu(expanded = isExpanded, onDismissRequest = onDismiss) {
        opciones.forEach {opcion->
            DropdownMenuItem(text={
                Text(text=opcion)
            }, onClick = {onDismiss()})

        }
    }

}