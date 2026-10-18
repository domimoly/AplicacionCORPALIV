package com.example.aplicacioncorpaliv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val createdAt: Long = System.currentTimeMillis()
)