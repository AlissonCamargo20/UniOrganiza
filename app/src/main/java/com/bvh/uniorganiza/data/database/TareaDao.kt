package com.bvh.uniorganiza.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    @Insert
    suspend fun insertarTarea(tarea: TareaEntity)

    @Query("SELECT * FROM tarea")
    suspend fun obtenerTareas(): List<TareaEntity>

    @Query("SELECT * FROM tarea WHERE id_usuario = :idUsuario")
    suspend fun obtenerTareasPorUsuario(idUsuario: Int): List<TareaEntity>

    @Query("SELECT * FROM tarea WHERE id_usuario = :idUsuario")
    fun observarTareasPorUsuario(idUsuario: Int): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tarea WHERE completada = 0")
    suspend fun obtenerTareasPendientes(): List<TareaEntity>

    @Update
    suspend fun actualizarTarea(tarea: TareaEntity)

    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)
}