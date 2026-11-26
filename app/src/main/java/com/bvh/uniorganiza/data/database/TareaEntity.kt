package com.bvh.uniorganiza.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Aquí se crea la tabla de tareas
@Entity(
    tableName = "tarea",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idUsuario"])]
)
data class TareaEntity(

    // Aquí se genera el identificador de cada tarea
    @PrimaryKey(autoGenerate = true)
    val idTarea: Int = 0,

    // Aquí se guarda el nombre de la tarea
    val nombre: String,

    // Aquí se guarda la descripción
    val descripcion: String,

    // Aquí se guarda la fecha de entrega
    val fecha: String,

    // Aquí se guarda la hora de entrega
    val hora: String,

    // Aquí se indica si la tarea está completada
    val completada: Boolean = false,

    // Aquí se relaciona la tarea con su usuario
    val idUsuario: Int
)