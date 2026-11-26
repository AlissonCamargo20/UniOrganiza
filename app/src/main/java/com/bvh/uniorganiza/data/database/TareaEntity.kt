package com.bvh.uniorganiza.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

// Aquí se crea la tabla de tareas
@Entity(
    tableName = "tarea",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["idUsuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id_categoria"],
            childColumns = ["id_categoria"]
        )
    ],
    indices = [
        Index(value = ["id_usuario"]),
        Index(value = ["id_categoria"])
    ]
)
data class TareaEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tarea")
    val idTarea: Int = 0,

    val nombre: String,

    val descripcion: String,

    val fecha: String,

    val hora: String,

    val completada: Boolean = false,

    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,

    @ColumnInfo(name = "id_categoria")
    val idCategoria: Int
)