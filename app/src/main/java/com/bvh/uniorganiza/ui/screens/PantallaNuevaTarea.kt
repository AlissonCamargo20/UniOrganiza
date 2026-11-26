package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.CategoriaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNuevaTarea(
    categorias: List<CategoriaEntity>,
    onRegresar: () -> Unit,
    onGuardar: (String, String, String, String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var categoriaId by remember(categorias) {
        mutableIntStateOf(categorias.firstOrNull()?.idCategoria ?: 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva tarea") },
                navigationIcon = {
                    TextButton(onClick = onRegresar) {
                        Text("Cancelar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la tarea") },
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
                label = { Text("Fecha (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:MM)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría", style = MaterialTheme.typography.titleMedium)

            categorias.forEach { categoria ->
                Row {
                    RadioButton(
                        selected = categoriaId == categoria.idCategoria,
                        onClick = { categoriaId = categoria.idCategoria }
                    )
                    Text(
                        categoria.nombre,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    onGuardar(
                        nombre.trim(),
                        descripcion.trim(),
                        fecha.trim(),
                        hora.trim(),
                        categoriaId
                    )
                },
                enabled = nombre.isNotBlank() &&
                        fecha.isNotBlank() &&
                        hora.isNotBlank() &&
                        categoriaId != 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar tarea")
            }
        }
    }
}