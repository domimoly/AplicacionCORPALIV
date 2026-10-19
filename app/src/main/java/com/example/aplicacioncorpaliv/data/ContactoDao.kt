package com.example.aplicacioncorpaliv.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface ContactoDao {
    @Query("SELECT * FROM contactos ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Contacto>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contacto: Contacto)
    @Delete
    suspend fun delete(contacto: Contacto)
}