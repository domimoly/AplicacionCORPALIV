package com.example.aplicacioncorpaliv.model

// MODELOS que viajan entre la app y los microservicios ms-vinculacion y ms-admin.
// EXPLICAR: los nombres de los campos son EXACTAMENTE los del JSON del servidor, para que Gson los una solos.
// 'String?' e 'Int?' significan que el valor puede venir vacío (null).

// Lo que la app ENVÍA en POST /solicitudes (los 4 obligatorios: nombre, medioContacto, tipo y aceptaPrivacidad)
data class SolicitudRequest(
    val nombre: String,
    val medioContacto: String,
    val tipo: String,
    val mensaje: String? = null,
    val monto: Int? = null,
    val fechaContacto: String? = null,
    val prioritario: Boolean = false,
    val aceptaPrivacidad: Boolean
)

// Lo que el servidor RESPONDE al crear una solicitud o al consultar con el código: el estado, SIN datos personales
data class Seguimiento(
    val codigoSeguimiento: String,
    val tipo: String,
    val estado: String,
    val observacion: String? = null,
    val fechaCreacion: String? = null,
    val fechaActualizacion: String? = null
)

// Una solicitud COMPLETA, con los datos de contacto. Solo la reciben los gestores (con token)
data class Solicitud(
    val id: Int,
    val codigoSeguimiento: String,
    val nombre: String,
    val medioContacto: String,
    val tipo: String,
    val mensaje: String? = null,
    val monto: Int? = null,
    val prioritario: Boolean = false,
    val estado: String,
    val observacion: String? = null,
    val fechaCreacion: String? = null
)

// Lo que el gestor ENVÍA en PATCH /solicitudes/{id}/estado
data class CambioEstado(
    val estado: String,
    val observacion: String
)

// Login de gestores (ms-admin)
data class LoginRequest(
    val correo: String,
    val clave: String
)

data class LoginResponse(
    val token: String,
    val nombre: String,
    val correo: String,
    val rol: String
)
