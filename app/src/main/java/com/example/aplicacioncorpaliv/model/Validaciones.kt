package com.example.aplicacioncorpaliv.model

// REGLAS DE VALIDACIÓN de los formularios (Colaborar, Contacto y Perfil), todas en un solo lugar.
// EXPLICAR: cada función recibe lo que escribió la persona y devuelve el MENSAJE DE ERROR, o null si está todo bien.
// Son funciones simples, sin pantallas ni base de datos, así se pueden probar con pruebas unitarias.
// Para cambiar una regla (por ejemplo el largo mínimo del nombre) se edita solo este archivo.
object Validaciones {

    // Correo: algo antes de la @, algo después y un punto en el dominio (por ejemplo nombre@dominio.cl)
    private val FORMATO_CORREO = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    // Teléfono: solo dígitos, espacios, +, - y paréntesis
    private val FORMATO_TELEFONO = Regex("^[0-9+\\-() ]+$")

    const val MAX_MENSAJE = 500
    const val MONTO_MINIMO = 1000
    const val MONTO_MAXIMO = 10_000_000

    fun esCorreoValido(valor: String): Boolean = FORMATO_CORREO.matches(valor.trim())

    // Un teléfono válido tiene entre 8 y 12 dígitos (sin contar espacios ni símbolos)
    fun esTelefonoValido(valor: String): Boolean {
        val texto = valor.trim()
        val digitos = texto.count { it.isDigit() }
        return FORMATO_TELEFONO.matches(texto) && digitos in 8..12
    }

    fun nombre(valor: String): String? = when {
        valor.isBlank() -> "El nombre es obligatorio"
        valor.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
        valor.trim().length > 100 -> "El nombre no puede superar los 100 caracteres"
        else -> null
    }

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "El correo es obligatorio"
        !esCorreoValido(valor) -> "El correo no tiene un formato válido"
        else -> null
    }

    // 'Medio de contacto' = el correo O el teléfono con el que CORPALIV puede responder
    fun medioContacto(valor: String): String? = when {
        valor.isBlank() -> "El correo o teléfono es obligatorio"
        valor.contains("@") -> if (esCorreoValido(valor)) null else "El correo no tiene un formato válido"
        esTelefonoValido(valor) -> null
        else -> "Escribe un correo (nombre@dominio.cl) o un teléfono de 8 a 12 dígitos"
    }

    fun mensaje(valor: String): String? = when {
        valor.isBlank() -> "El mensaje es obligatorio"
        valor.trim().length < 5 -> "El mensaje debe tener al menos 5 caracteres"
        valor.trim().length > MAX_MENSAJE -> "El mensaje no puede superar los $MAX_MENSAJE caracteres"
        else -> null
    }

    // El monto solo se pide en las donaciones ('obligatorio' = true). En los demás tipos se ignora
    fun monto(valor: String, obligatorio: Boolean): String? {
        if (!obligatorio) return null
        val numero = valor.toIntOrNull() ?: return "Debe ingresar un número"
        return when {
            numero < MONTO_MINIMO -> "El monto mínimo es de $MONTO_MINIMO pesos"
            numero > MONTO_MAXIMO -> "El monto máximo es de $MONTO_MAXIMO pesos"
            else -> null
        }
    }

    // Para Perfil, donde los datos son opcionales: si está vacío no hay error; si tiene algo, se valida
    fun nombreOpcional(valor: String): String? = if (valor.isBlank()) null else nombre(valor)
    fun medioContactoOpcional(valor: String): String? = if (valor.isBlank()) null else medioContacto(valor)
}
