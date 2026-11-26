package com.bvh.uniorganiza.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Aquí se crea la tabla de usuarios
@Entity(
    tableName = "usuario",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity(

    // Aquí se genera el identificador de cada usuario
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Int = 0,

    // Aquí se guarda el nombre completo
    val nombre: String,

    // Aquí se guarda el correo electrónico
    val correo: String,

    // Aquí se guarda la contraseña
    val contrasena: String
)