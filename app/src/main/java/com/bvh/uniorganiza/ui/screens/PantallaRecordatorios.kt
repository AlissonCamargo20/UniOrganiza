package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.RecordatorioDao
import com.bvh.uniorganiza.data.database.RecordatorioEntity
import com.bvh.uniorganiza.data.database.TareaDao
import com.bvh.uniorganiza.data.database.TareaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PantallaRecordatorios(
    tareaDao: TareaDao,
    recordatorioDao: RecordatorioDao,
    usuarioId: Int
) {
    var recordatorios by remember {
        mutableStateOf(emptyList<RecordatorioEntity>())
    }

    var tareas by remember {
        mutableStateOf(emptyList<TareaEntity>())
    }

    var cargando by remember { mutableStateOf(true) }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var recordatorioEditar by remember {
        mutableStateOf<RecordatorioEntity?>(null)
    }

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var tareaSeleccionada by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    suspend fun cargarDatos() {
        val resultado = withContext(Dispatchers.IO) {
            val tareasUsuario = tareaDao.obtenerTareasPorUsuario(usuarioId)
            val todosRecordatorios = recordatorioDao.obtenerRecordatorios()

            val idsTareas = tareasUsuario.map { it.idTarea }.toSet()

            val recordatoriosUsuario = todosRecordatorios.filter {
                it.idTarea in idsTareas
            }

            Pair(tareasUsuario, recordatoriosUsuario)
        }

        tareas = resultado.first
        recordatorios = resultado.second
        cargando = false
    }

    LaunchedEffect(usuarioId) {
        cargarDatos()
    }

    fun abrirFormulario(recordatorio: RecordatorioEntity? = null) {
        recordatorioEditar = recordatorio

        if (recordatorio != null) {
            fecha = recordatorio.fechaRecordatorio
            hora = recordatorio.horaRecordatorio
            tareaSeleccionada = recordatorio.idTarea
        } else {
            fecha = ""
            hora = ""
            tareaSeleccionada = tareas.firstOrNull()?.idTarea ?: 0
        }

        mostrarFormulario = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Mis recordatorios",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = { abrirFormulario() },
                enabled = tareas.isNotEmpty()
            ) {
                Text("+ Nuevo")
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            cargando -> {
                CircularProgressIndicator()
            }

            tareas.isEmpty() -> {
                Text("Primero necesitas registrar una tarea.")
            }

            recordatorios.isEmpty() -> {
                Text("Todavía no tienes recordatorios registrados.")
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = recordatorios,
                        key = { it.idRecordatorio }
                    ) { recordatorio ->

                        val nombreTarea = tareas.find {
                            it.idTarea == recordatorio.idTarea
                        }?.nombre ?: "Tarea"

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    nombreTarea,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.height(6.dp))

                                Text("Fecha: ${recordatorio.fechaRecordatorio}")
                                Text("Hora: ${recordatorio.horaRecordatorio}")

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        if (recordatorio.activo) "Activo" else "Inactivo"
                                    )

                                    Switch(
                                        checked = recordatorio.activo,
                                        onCheckedChange = { activo ->
                                            scope.launch {
                                                withContext(Dispatchers.IO) {
                                                    recordatorioDao.actualizarRecordatorio(
                                                        recordatorio.copy(activo = activo)
                                                    )
                                                }
                                                cargarDatos()
                                            }
                                        }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            abrirFormulario(recordatorio)
                                        }
                                    ) {
                                        Text("Editar")
                                    }

                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                withContext(Dispatchers.IO) {
                                                    recordatorioDao.eliminarRecordatorio(
                                                        recordatorio
                                                    )
                                                }
                                                cargarDatos()
                                            }
                                        }
                                    ) {
                                        Text(
                                            "Eliminar",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulario) {
        AlertDialog(
            onDismissRequest = {
                mostrarFormulario = false
            },
            title = {
                Text(
                    if (recordatorioEditar == null) {
                        "Nuevo recordatorio"
                    } else {
                        "Editar recordatorio"
                    }
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha (AAAA-MM-DD)") },
                        placeholder = { Text("2026-11-26") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora (HH:MM)") },
                        placeholder = { Text("14:30") },
                        singleLine = true
                    )

                    Text(
                        "Selecciona la tarea:",
                        style = MaterialTheme.typography.titleSmall
                    )

                    tareas.forEach { tarea ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = tareaSeleccionada == tarea.idTarea,
                                onClick = {
                                    tareaSeleccionada = tarea.idTarea
                                }
                            )

                            Text(
                                tarea.nombre,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (
                            fecha.isNotBlank() &&
                            hora.isNotBlank() &&
                            tareaSeleccionada != 0
                        ) {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    val anterior = recordatorioEditar

                                    if (anterior == null) {
                                        recordatorioDao.insertarRecordatorio(
                                            RecordatorioEntity(
                                                fechaRecordatorio = fecha.trim(),
                                                horaRecordatorio = hora.trim(),
                                                activo = true,
                                                idTarea = tareaSeleccionada
                                            )
                                        )
                                    } else {
                                        recordatorioDao.actualizarRecordatorio(
                                            anterior.copy(
                                                fechaRecordatorio = fecha.trim(),
                                                horaRecordatorio = hora.trim(),
                                                idTarea = tareaSeleccionada
                                            )
                                        )
                                    }
                                }

                                mostrarFormulario = false
                                cargarDatos()
                            }
                        }
                    },
                    enabled = fecha.isNotBlank() &&
                            hora.isNotBlank() &&
                            tareaSeleccionada != 0
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarFormulario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}