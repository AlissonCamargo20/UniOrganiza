package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.RecordatorioDao
import com.bvh.uniorganiza.data.database.TareaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PantallaRecordatorios(
    tareaDao: TareaDao,
    recordatorioDao: RecordatorioDao,
    usuarioId: Int
) {
    var recordatorios by remember {
        mutableStateOf(emptyList<com.bvh.uniorganiza.data.database.RecordatorioEntity>())
    }
    var nombresTareas by remember { mutableStateOf(emptyMap<Int, String>()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(usuarioId) {
        cargando = true

        val datos = withContext(Dispatchers.IO) {
            val tareas = tareaDao.obtenerTareasPorUsuario(usuarioId)
            val todos = recordatorioDao.obtenerRecordatorios()

            val tareasUsuario = tareas.associate {
                it.idTarea to it.nombre
            }

            val avisosUsuario = todos.filter {
                tareasUsuario.containsKey(it.idTarea)
            }

            Pair(avisosUsuario, tareasUsuario)
        }

        recordatorios = datos.first
        nombresTareas = datos.second
        cargando = false
    }

    Column {
        Text(
            "Mis recordatorios",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        when {
            cargando -> CircularProgressIndicator()

            recordatorios.isEmpty() ->
                Text("Todavía no tienes recordatorios registrados")

            else -> LazyColumn {
                items(recordatorios) { recordatorio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                nombresTareas[recordatorio.idTarea]
                                    ?: "Tarea",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "${recordatorio.fechaRecordatorio} • " +
                                        recordatorio.horaRecordatorio
                            )

                            Text(
                                if (recordatorio.activo) "Activo"
                                else "Inactivo"
                            )
                        }
                    }
                }
            }
        }
    }
}