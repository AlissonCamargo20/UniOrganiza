package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.TareaEntity

@Composable
fun PantallaCalendario(tareas: List<TareaEntity>) {
    var fechaSeleccionada by remember { mutableStateOf("") }

    Column {
        Text(
            "Calendario académico",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = fechaSeleccionada,
            onValueChange = { fechaSeleccionada = it },
            label = { Text("Buscar fecha (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        val filtradas = if (fechaSeleccionada.isBlank()) {
            tareas.sortedBy { it.fecha }
        } else {
            tareas.filter {
                it.fecha.contains(fechaSeleccionada)
            }
        }

        if (filtradas.isEmpty()) {
            Text("No hay actividades para esta fecha")
        } else {
            LazyColumn {
                items(filtradas) { tarea ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                tarea.fecha,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                tarea.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Hora: ${tarea.hora}")
                            Text(
                                if (tarea.completada) "Completada"
                                else "Pendiente"
                            )
                        }
                    }
                }
            }
        }
    }
}