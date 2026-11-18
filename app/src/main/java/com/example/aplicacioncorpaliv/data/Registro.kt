package com.example.aplicacioncorpaliv.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aplicacioncorpaliv.model.EstadoSolicitud

// Una solicitud de colaboración guardada en la base de datos (tabla 'registros').
// EXPLICAR: se agregaron campos nuevos al final:
//  - estado: en qué etapa va la solicitud. Al crearla es RECIBIDA (valor por defecto) y el gestor la cambia.
//  - observacion: mensaje del gestor para la persona (por ejemplo, 'Te llamaremos esta semana').
//  - medioContacto: el correo o teléfono con el que CORPALIV puede responder.
//  - codigoSeguimiento: el código (por ejemplo 'COL-7KQ4M2XA') con el que la persona consulta el estado de su solicitud.
// 'tipo' ahora guarda el CÓDIGO del tipo de colaboración (por ejemplo 'DONACION'), igual que el backend.
// Como cambió la tabla, la versión de la base de datos subió a 4 (ver AppDatabase.kt).
@Entity(tableName = "registros")
data class Registro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val mensaje: String,
    val monto: Int,
    val tipo: String,
    val fechaContacto: Long?,
    val aceptaContacto: Boolean,
    val prioritario: Boolean,
    val estado: String = EstadoSolicitud.RECIBIDA,
    val observacion: String = "",
    val medioContacto: String = "",
    val codigoSeguimiento: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
