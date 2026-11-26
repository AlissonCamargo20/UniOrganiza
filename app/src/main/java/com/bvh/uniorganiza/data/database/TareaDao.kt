package com.bvh.uniorganiza.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Aquí se definen las operaciones de la tabla tarea
@Dao
interface TareaDao {

    // Aquí se registra una tarea
    @Insert
    suspend fun insertarTarea(tarea: TareaEntity)

    // Aquí se consultan todas las tareas
    @Query("SELECT * FROM tarea")
    suspend fun obtenerTareas(): List<TareaEntity>

    // Aquí se consultan las tareas de un usuario
    @Query("SELECT * FROM tarea WHERE id_usuario = :idUsuario")
    suspend fun obtenerTareasPorUsuario(idUsuario: Int): List<TareaEntity>

    // Aquí se consultan las tareas pendientes
    @Query("SELECT * FROM tarea WHERE completada = 0")
    suspend fun obtenerTareasPendientes(): List<TareaEntity>

    // Aquí se actualizan los datos de una tarea
    @Update
    suspend fun actualizarTarea(tarea: TareaEntity)

    // Aquí se elimina una tarea
    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)
}