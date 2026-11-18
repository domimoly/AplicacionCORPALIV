package com.example.aplicacioncorpaliv.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// EXPLICAR: version = 4 porque la tabla 'registros' ahora tiene los campos estado, observacion, medioContacto y codigoSeguimiento.
// Cada vez que cambia la estructura de una tabla hay que subir el número de versión.
// IMPORTAAANTE | Si se agrega una tabla hay que sumarla en 'entities' y subir 'version'
@Database(entities = [Registro::class, Contacto::class], version = 4, exportSchema = false)
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
                )
                    // AVANZADO: al subir de versión, en vez de escribir una migración (ALTER TABLE),
                    // se borran y se vuelven a crear las tablas. Es aceptable ahora porque solo hay datos de prueba.
                    // En una app real con datos de verdad se escribiría una Migration para no perderlos.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
