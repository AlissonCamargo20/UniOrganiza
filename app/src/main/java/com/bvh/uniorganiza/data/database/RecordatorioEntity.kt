package com.bvh.uniorganiza.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

// Aquí se crea la tabla de recordatorios
@Entity(
    tableName = "recordatorio",
    foreignKeys = [
        ForeignKey(
            entity = TareaEntity::class,
            parentColumns = ["id_tarea"],
            childColumns = ["id_tarea"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_tarea"])]
)
data class RecordatorioEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_recordatorio")
    val idRecordatorio: Int = 0,

    @ColumnInfo(name = "fecha_recordatorio")
    val fechaRecordatorio: String,

    @ColumnInfo(name = "hora_recordatorio")
    val horaRecordatorio: String,

    val activo: Boolean = true,

    @ColumnInfo(name = "id_tarea")
    val idTarea: Int
)