package com.example.aplicacioncorpaliv.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface RegistroDao {
    @Query("SELECT * FROM registros ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Registro>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(registro: Registro)
    @Delete
    suspend fun delete(registro: Registro)
}