package com.example.aplicacioncorpaliv.model

// Los datos del formulario Contacto.
// 'aceptaPrivacidad' solo sirve para validar el formulario (no se guarda en la base de datos).
data class ContactoModel(
    val nombre: String = "",
    val correo: String = "",
    val mensaje: String = "",
    val aceptaPrivacidad: Boolean = false
)
