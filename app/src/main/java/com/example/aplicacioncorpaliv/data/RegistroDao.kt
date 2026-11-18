package com.example.aplicacioncorpaliv.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
// las consultas a la tabla. observeAll entrega la lista y se actualiza sola cuando cambian los datos
@Dao
interface RegistroDao {
    @Query("SELECT * FROM registros ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Registro>>
    // EXPLICAR: busca UNA solicitud por su código de seguimiento (LIMIT 1 = solo la primera). Si no existe devuelve null (el '?')
    @Query("SELECT * FROM registros WHERE codigoSeguimiento = :codigo LIMIT 1")
    suspend fun findByCodigo(codigo: String): Registro?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(registro: Registro)
    // EXPLICAR: @Update busca en la tabla la fila con el mismo id y reemplaza sus datos por los del objeto recibido.
    // Se usa para cambiar el estado de una solicitud (el U de CRUD: Update)
    @Update
    suspend fun update(registro: Registro)
    @Delete
    suspend fun delete(registro: Registro)
}
