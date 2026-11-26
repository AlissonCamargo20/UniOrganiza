package com.bvh.uniorganiza.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Aquí se definen las operaciones de la tabla usuario
@Dao
interface UsuarioDao {

    // Aquí se registra un usuario
    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    // Aquí se consultan todos los usuarios
    @Query("SELECT * FROM usuario")
    suspend fun obtenerUsuarios(): List<UsuarioEntity>

    // Aquí se busca un usuario por su correo
    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    // Aquí se actualizan los datos de un usuario
    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    // Aquí se elimina un usuario
    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)
}