package com.bvh.uniorganiza.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert
    suspend fun insertarCategoria(categoria: CategoriaEntity)

    @Query("SELECT * FROM categoria")
    suspend fun obtenerCategorias(): List<CategoriaEntity>

    @Query("SELECT * FROM categoria")
    fun observarCategorias(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categoria WHERE id_categoria = :idCategoria")
    suspend fun buscarCategoria(idCategoria: Int): CategoriaEntity?

    @Update
    suspend fun actualizarCategoria(categoria: CategoriaEntity)

    @Delete
    suspend fun eliminarCategoria(categoria: CategoriaEntity)
}