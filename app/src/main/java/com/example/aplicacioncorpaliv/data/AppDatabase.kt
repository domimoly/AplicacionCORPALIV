package com.example.aplicacioncorpaliv.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// IMPORTAAANTE | Si se agrega una tabla hay que sumarla en 'entities' y subir 'version'
@Database(entities = [Registro::class, Contacto::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun registroDao(): RegistroDao
    abstract fun contactoDao(): ContactoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        // Singleton | asegura que exista una sola base de datos en toda la app
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "corpaliv_database"
                ).build().also { INSTANCE = it }
            }
    }
}