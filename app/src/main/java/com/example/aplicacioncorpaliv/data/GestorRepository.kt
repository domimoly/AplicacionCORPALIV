package com.example.aplicacioncorpaliv.data

import java.security.MessageDigest

// REPOSITORIO DEL GESTOR: decide si el correo y la clave ingresados corresponden a un gestor de vinculación.
// EXPLICAR: por ahora hay UN gestor de demostración (datos ficticios). Más adelante esto se reemplaza por una llamada
// al microservicio ms-admin (POST /auth/login), que devuelve un token JWT, igual que el auth-service del ejemplo.
// IMPORTANTE: la clave NO está escrita en el código; solo está su 'hash'. Un hash es el resultado de pasar la clave
// por una función matemática de una sola vía (SHA-256): de la clave se obtiene el hash, pero del hash no se puede volver a la clave.
// Al iniciar sesión se calcula el hash de lo que escribió la persona y se compara con el guardado.
class GestorRepository {

    private val correoGestor = "gestor@ejemplo.cl"
    private val nombreGestor = "Gestor de vinculación (demo)"
    // Hash SHA-256 de la clave de demostración
    private val hashClave = "1ea6483ea38259c29dd48a7b82de36ea39ef1907f5f1ac84638d527fe18feb8e"

    // Devuelve el nombre del gestor si los datos son correctos, o null si no lo son
    fun iniciarSesion(correo: String, clave: String): String? {
        val correoCorrecto = correo.trim().lowercase() == correoGestor
        val claveCorrecta = calcularHash(clave) == hashClave
        return if (correoCorrecto && claveCorrecta) nombreGestor else null
    }

    // Convierte un texto en su hash SHA-256 (64 caracteres hexadecimales)
    private fun calcularHash(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        // Cada byte se escribe con 2 caracteres hexadecimales ("%02x") y se unen todos en un solo texto
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
