package com.example.aplicacioncorpaliv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactos")
data class Contacto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val mensaje: String,
    val createdAt: Long = System.currentTimeMillis()
)