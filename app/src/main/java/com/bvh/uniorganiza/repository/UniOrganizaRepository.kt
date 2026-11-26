package com.bvh.uniorganiza.data.repository

import com.bvh.uniorganiza.data.database.*
import kotlinx.coroutines.flow.Flow

class UniOrganizaRepository(
    private val tareaDao: TareaDao,
    private val categoriaDao: CategoriaDao
) {

    fun observarTareas(idUsuario: Int): Flow<List<TareaEntity>> {
        return tareaDao.observarTareasPorUsuario(idUsuario)
    }

    fun observarCategorias(): Flow<List<CategoriaEntity>> {
        return categoriaDao.observarCategorias()
    }

    suspend fun insertarTarea(tarea: TareaEntity) {
        tareaDao.insertarTarea(tarea)
    }

    suspend fun actualizarTarea(tarea: TareaEntity) {
        tareaDao.actualizarTarea(tarea)
    }

    suspend fun eliminarTarea(tarea: TareaEntity) {
        tareaDao.eliminarTarea(tarea)
    }

    suspend fun insertarCategoria(categoria: CategoriaEntity) {
        categoriaDao.insertarCategoria(categoria)
    }

    suspend fun obtenerCategorias(): List<CategoriaEntity> {
        return categoriaDao.obtenerCategorias()
    }
}