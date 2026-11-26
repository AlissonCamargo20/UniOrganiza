package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.TareaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleTarea(
    tarea: TareaEntity,
    onRegresar: () -> Unit,
    onGuardar: (TareaEntity) -> Unit
) {
    var nombre by remember(tarea.idTarea) {
        mutableStateOf(tarea.nombre)
    }
    var descripcion by remember(tarea.idTarea) {
        mutableStateOf(tarea.descripcion)
    }
    var fecha by remember(tarea.idTarea) {
        mutableStateOf(tarea.fecha)
    }
    var hora by remember(tarea.idTarea) {
        mutableStateOf(tarea.hora)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de tarea") },
                navigationIcon = {
                    TextButton(onClick = onRegresar) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                if (tarea.completada) "Estado: Completada"
                else "Estado: Pendiente"
            )

            Button(
                onClick = {
                    onGuardar(
                        tarea.copy(
                            nombre = nombre.trim(),
                            descripcion = descripcion.trim(),
                            fecha = fecha.trim(),
                            hora = hora.trim()
                        )
                    )
                },
                enabled = nombre.isNotBlank() &&
                        fecha.isNotBlank() &&
                        hora.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}