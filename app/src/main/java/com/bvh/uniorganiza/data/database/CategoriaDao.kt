package com.bvh.uniorganiza.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Aquí se definen las operaciones de la tabla categoría
@Dao
interface CategoriaDao {

    @Insert
    suspend fun insertarCategoria(categoria: CategoriaEntity)

    @Query("SELECT * FROM categoria")
    suspend fun obtenerCategorias(): List<CategoriaEntity>

    @Query("SELECT * FROM categoria WHERE id_categoria = :idCategoria")
    suspend fun buscarCategoria(idCategoria: Int): CategoriaEntity?

    @Update
    suspend fun actualizarCategoria(categoria: CategoriaEntity)

    @Delete
    suspend fun eliminarCategoria(categoria: CategoriaEntity)
}