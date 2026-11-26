package com.bvh.uniorganiza.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

// Aquí se crea la tabla de categorías
@Entity(tableName = "categoria")
data class CategoriaEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_categoria")
    val idCategoria: Int = 0,

    val nombre: String
)