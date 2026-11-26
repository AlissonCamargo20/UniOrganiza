package com.bvh.uniorganiza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvh.uniorganiza.data.database.CategoriaEntity
import com.bvh.uniorganiza.data.database.TareaEntity
import com.bvh.uniorganiza.data.repository.UniOrganizaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class UniOrganizaUiState(
    val tareas: List<TareaEntity> = emptyList(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val cargando: Boolean = true,
    val mensaje: String = ""
)

class UniOrganizaViewModel(
    private val repository: UniOrganizaRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(UniOrganizaUiState())
    val uiState: StateFlow<UniOrganizaUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            try {
                if (repository.obtenerCategorias().isEmpty()) {
                    listOf("General", "Escuela", "Personal", "Exámenes")
                        .forEach { nombre ->
                            repository.insertarCategoria(
                                CategoriaEntity(nombre = nombre)
                            )
                        }
                }

                combine(
                    repository.observarTareas(usuarioId),
                    repository.observarCategorias()
                ) { tareas, categorias ->
                    UniOrganizaUiState(
                        tareas = tareas,
                        categorias = categorias,
                        cargando = false
                    )
                }.collect { nuevoEstado ->
                    _uiState.value = nuevoEstado
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    mensaje = "No se pudieron cargar los datos"
                )
            }
        }
    }

    fun insertarTarea(
        nombre: String,
        descripcion: String,
        fecha: String,
        hora: String,
        categoriaId: Int
    ) {
        viewModelScope.launch {
            try {
                repository.insertarTarea(
                    TareaEntity(
                        nombre = nombre,
                        descripcion = descripcion,
                        fecha = fecha,
                        hora = hora,
                        idUsuario = usuarioId,
                        idCategoria = categoriaId
                    )
                )
            } catch (e: Exception) {
                mostrarError("No se pudo guardar la tarea")
            }
        }
    }

    fun actualizarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            try {
                repository.actualizarTarea(tarea)
            } catch (e: Exception) {
                mostrarError("No se pudo actualizar la tarea")
            }
        }
    }

    fun eliminarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            try {
                repository.eliminarTarea(tarea)
            } catch (e: Exception) {
                mostrarError("No se pudo eliminar la tarea")
            }
        }
    }

    fun limpiarMensaje() {
        _uiState.value = _uiState.value.copy(mensaje = "")
    }

    private fun mostrarError(texto: String) {
        _uiState.value = _uiState.value.copy(mensaje = texto)
    }
}