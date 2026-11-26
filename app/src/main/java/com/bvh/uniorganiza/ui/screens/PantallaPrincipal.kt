package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.*
import com.bvh.uniorganiza.ui.viewmodel.UniOrganizaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    usuarioId: Int,
    tareaDao: TareaDao,
    recordatorioDao: RecordatorioDao,
    viewModel: UniOrganizaViewModel,
    onCerrarSesion: () -> Unit
) {
    var seccion by remember { mutableStateOf("inicio") }
    var tareaSeleccionada by remember {
        mutableStateOf<TareaEntity?>(null)
    }

    val uiState by viewModel.uiState.collectAsState()
    val tareas = uiState.tareas
    val categorias = uiState.categorias

    if (seccion == "nueva") {
        PantallaNuevaTarea(
            categorias = categorias,
            onRegresar = {
                seccion = "tareas"
            },
            onGuardar = { nombre, descripcion, fecha, hora, categoriaId ->
                viewModel.insertarTarea(
                    nombre,
                    descripcion,
                    fecha,
                    hora,
                    categoriaId
                )
                seccion = "tareas"
            }
        )
        return
    }

    if (seccion == "detalle" && tareaSeleccionada != null) {
        PantallaDetalleTarea(
            tarea = tareaSeleccionada!!,
            onRegresar = {
                seccion = "tareas"
            },
            onGuardar = { tareaActualizada ->
                viewModel.actualizarTarea(tareaActualizada)
                tareaSeleccionada = null
                seccion = "tareas"
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "UniOrganiza",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = onCerrarSesion
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = seccion == "inicio",
                    onClick = { seccion = "inicio" },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Inicio")
                    },
                    label = { Text("Inicio") }
                )

                NavigationBarItem(
                    selected = seccion == "tareas",
                    onClick = { seccion = "tareas" },
                    icon = {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Tareas")
                    },
                    label = { Text("Tareas") }
                )

                NavigationBarItem(
                    selected = seccion == "calendario",
                    onClick = { seccion = "calendario" },
                    icon = {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Calendario")
                    },
                    label = { Text("Calendario") }
                )

                NavigationBarItem(
                    selected = seccion == "categorias",
                    onClick = { seccion = "categorias" },
                    icon = {
                        Icon(Icons.Default.Category, contentDescription = "Categorías")
                    },
                    label = { Text("Categorías") }
                )

                NavigationBarItem(
                    selected = seccion == "recordatorios",
                    onClick = { seccion = "recordatorios" },
                    icon = {
                        Icon(Icons.Default.Notifications, contentDescription = "Avisos")
                    },
                    label = { Text("Avisos") }
                )
            }
        },

        floatingActionButton = {
            if (seccion == "inicio" || seccion == "tareas") {
                FloatingActionButton(
                    onClick = {
                        seccion = "nueva"
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Nueva tarea"
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            when (seccion) {

                "inicio" -> {
                    Text(
                        "¡Hola! Bienvenida a UniOrganiza",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            Modifier.padding(20.dp)
                        ) {
                            Text("Mis actividades")

                            Text(
                                "${tareas.size} tareas registradas",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                "${tareas.count { !it.completada }} pendientes"
                            )

                            Text(
                                "${tareas.count { it.completada }} completadas"
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "Tareas recientes",
                        fontWeight = FontWeight.Bold
                    )

                    tareas.take(3).forEach { tarea ->
                        ListItem(
                            headlineContent = {
                                Text(tarea.nombre)
                            },
                            supportingContent = {
                                Text("${tarea.fecha} • ${tarea.hora}")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                "tareas" -> {
                    PantallaTareas(
                        tareas = tareas,
                        onSeleccionar = { tarea ->
                            tareaSeleccionada = tarea
                            seccion = "detalle"
                        },
                        onCompletar = { tarea, estado ->
                            viewModel.actualizarTarea(
                                tarea.copy(completada = estado)
                            )
                        },
                        onEliminar = { tarea ->
                            viewModel.eliminarTarea(tarea)
                        }
                    )
                }

                "calendario" -> {
                    PantallaCalendario(tareas)
                }

                "categorias" -> {
                    PantallaCategorias(categorias)
                }

                "recordatorios" -> {
                    PantallaRecordatorios(
                        tareaDao = tareaDao,
                        recordatorioDao = recordatorioDao,
                        usuarioId = usuarioId
                    )
                }
            }
        }
    }

    if (uiState.mensaje.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                viewModel.limpiarMensaje()
            },
            title = {
                Text("UniOrganiza")
            },
            text = {
                Text(uiState.mensaje)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.limpiarMensaje()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}