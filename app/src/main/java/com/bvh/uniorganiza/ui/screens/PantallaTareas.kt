package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.TareaEntity

@Composable
fun PantallaTareas(
    tareas: List<TareaEntity>,
    onSeleccionar: (TareaEntity) -> Unit,
    onCompletar: (TareaEntity, Boolean) -> Unit,
    onEliminar: (TareaEntity) -> Unit
) {
    Column {
        Text(
            "Mis tareas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        if (tareas.isEmpty()) {
            Text("Todavía no tienes tareas registradas")
        } else {
            LazyColumn {
                items(tareas, key = { it.idTarea }) { tarea ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {
                                Text(
                                    tarea.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Checkbox(
                                    checked = tarea.completada,
                                    onCheckedChange = {
                                        onCompletar(tarea, it)
                                    }
                                )
                            }

                            Text(tarea.descripcion)
                            Text("${tarea.fecha} • ${tarea.hora}")

                            Row {
                                TextButton(
                                    onClick = { onSeleccionar(tarea) }
                                ) {
                                    Text("Ver / Editar")
                                }

                                TextButton(
                                    onClick = { onEliminar(tarea) }
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}