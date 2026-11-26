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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    usuarioId: Int,
    tareaDao: TareaDao,
    categoriaDao: CategoriaDao,
    recordatorioDao: RecordatorioDao,
    onCerrarSesion: () -> Unit
) {
    var seccion by remember { mutableStateOf("inicio") }
    var tareas by remember { mutableStateOf(emptyList<TareaEntity>()) }
    var categorias by remember { mutableStateOf(emptyList<CategoriaEntity>()) }
    var tareaSeleccionada by remember { mutableStateOf<TareaEntity?>(null) }
    var mensaje by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    fun cargarDatos() {
        scope.launch {
            try {
                tareas = withContext(Dispatchers.IO) {
                    tareaDao.obtenerTareasPorUsuario(usuarioId)
                }
                categorias = withContext(Dispatchers.IO) {
                    categoriaDao.obtenerCategorias()
                }
            } catch (e: Exception) {
                mensaje = "No se pudieron cargar los datos"
            }
        }
    }

    LaunchedEffect(usuarioId) {
        withContext(Dispatchers.IO) {
            if (categoriaDao.obtenerCategorias().isEmpty()) {
                listOf("General", "Escuela", "Personal", "Exámenes")
                    .forEach {
                        categoriaDao.insertarCategoria(
                            CategoriaEntity(nombre = it)
                        )
                    }
            }
        }
        cargarDatos()
    }

    if (seccion == "nueva") {
        PantallaNuevaTarea(
            categorias = categorias,
            onRegresar = { seccion = "tareas" },
            onGuardar = { nombre, descripcion, fecha, hora, categoriaId ->
                scope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            tareaDao.insertarTarea(
                                TareaEntity(
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    fecha = fecha,
                                    hora = hora,
                                    idUsuario = usuarioId,
                                    idCategoria = categoriaId
                                )
                            )
                        }
                        cargarDatos()
                        seccion = "tareas"
                    } catch (e: Exception) {
                        mensaje = "No se pudo guardar la tarea"
                    }
                }
            }
        )
        return
    }

    if (seccion == "detalle" && tareaSeleccionada != null) {
        PantallaDetalleTarea(
            tarea = tareaSeleccionada!!,
            onRegresar = { seccion = "tareas" },
            onGuardar = { tareaActualizada ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        tareaDao.actualizarTarea(tareaActualizada)
                    }
                    cargarDatos()
                    seccion = "tareas"
                }
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
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.Default.Logout, "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = seccion == "inicio",
                    onClick = { seccion = "inicio" },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = seccion == "tareas",
                    onClick = { seccion = "tareas" },
                    icon = { Icon(Icons.Default.CheckCircle, null) },
                    label = { Text("Tareas") }
                )
                NavigationBarItem(
                    selected = seccion == "calendario",
                    onClick = { seccion = "calendario" },
                    icon = { Icon(Icons.Default.CalendarMonth, null) },
                    label = { Text("Calendario") }
                )
                NavigationBarItem(
                    selected = seccion == "categorias",
                    onClick = { seccion = "categorias" },
                    icon = { Icon(Icons.Default.Category, null) },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = seccion == "recordatorios",
                    onClick = { seccion = "recordatorios" },
                    icon = { Icon(Icons.Default.Notifications, null) },
                    label = { Text("Avisos") }
                )
            }
        },
        floatingActionButton = {
            if (seccion == "inicio" || seccion == "tareas") {
                FloatingActionButton(
                    onClick = { seccion = "nueva" }
                ) {
                    Icon(Icons.Default.Add, "Nueva tarea")
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
                        Column(Modifier.padding(20.dp)) {
                            Text("Mis actividades")
                            Text(
                                "${tareas.size} tareas registradas",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text("${tareas.count { !it.completada }} pendientes")
                            Text("${tareas.count { it.completada }} completadas")
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Tareas recientes", fontWeight = FontWeight.Bold)

                    tareas.take(3).forEach { tarea ->
                        ListItem(
                            headlineContent = { Text(tarea.nombre) },
                            supportingContent = {
                                Text("${tarea.fecha} • ${tarea.hora}")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                "tareas" -> PantallaTareas(
                    tareas = tareas,
                    onSeleccionar = {
                        tareaSeleccionada = it
                        seccion = "detalle"
                    },
                    onCompletar = { tarea, estado ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                tareaDao.actualizarTarea(
                                    tarea.copy(completada = estado)
                                )
                            }
                            cargarDatos()
                        }
                    },
                    onEliminar = { tarea ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                tareaDao.eliminarTarea(tarea)
                            }
                            cargarDatos()
                        }
                    }
                )

                "calendario" -> PantallaCalendario(tareas)

                "categorias" -> PantallaCategorias(categorias)

                "recordatorios" -> PantallaRecordatorios(
                    tareaDao = tareaDao,
                    recordatorioDao = recordatorioDao,
                    usuarioId = usuarioId
                )
            }
        }
    }

    if (mensaje.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { mensaje = "" },
            title = { Text("UniOrganiza") },
            text = { Text(mensaje) },
            confirmButton = {
                TextButton(onClick = { mensaje = "" }) {
                    Text("Aceptar")
                }
            }
        )
    }
}