package com.bvh.uniorganiza.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Aquí se definen las operaciones de la tabla recordatorio
@Dao
interface RecordatorioDao {

    @Insert
    suspend fun insertarRecordatorio(recordatorio: RecordatorioEntity)

    @Query("SELECT * FROM recordatorio")
    suspend fun obtenerRecordatorios(): List<RecordatorioEntity>

    @Query("SELECT * FROM recordatorio WHERE id_tarea = :idTarea")
    suspend fun obtenerRecordatoriosPorTarea(idTarea: Int): List<RecordatorioEntity>

    @Update
    suspend fun actualizarRecordatorio(recordatorio: RecordatorioEntity)

    @Delete
    suspend fun eliminarRecordatorio(recordatorio: RecordatorioEntity)
}