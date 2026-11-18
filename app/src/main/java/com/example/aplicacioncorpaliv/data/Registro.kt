package com.example.aplicacioncorpaliv.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aplicacioncorpaliv.model.EstadoSolicitud

// Una solicitud de colaboración guardada en la base de datos (tabla 'registros').
// EXPLICAR: se agregaron dos campos nuevos al final:
//  - estado: en qué etapa va la solicitud. Al crearla es RECIBIDA (valor por defecto) y el gestor la cambia.
//  - observacion: mensaje del gestor para la persona (por ejemplo, 'Te llamaremos esta semana').
// Como cambió la tabla, la versión de la base de datos subió a 3 (ver AppDatabase.kt).
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
    val createdAt: Long = System.currentTimeMillis()
)
