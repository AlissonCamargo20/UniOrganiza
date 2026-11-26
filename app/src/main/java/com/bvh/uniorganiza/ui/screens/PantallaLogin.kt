package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.data.database.UsuarioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Aquí se crea la pantalla de inicio de sesión
@Composable
fun PantallaLogin(
    onRegresar: () -> Unit,
    onRegistrar: () -> Unit,
    onEntrar: (Int) -> Unit,
    usuarioDao: UsuarioDao
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (correo.isBlank() || contrasena.isBlank()) {
                    mensaje = "Completa todos los campos"
                } else {
                    cargando = true

                    scope.launch {
                        try {
                            val usuario = withContext(Dispatchers.IO) {
                                usuarioDao.buscarPorCorreo(
                                    correo.trim().lowercase()
                                )
                            }

                            when {
                                usuario == null -> {
                                    mensaje = "No existe una cuenta con ese correo"
                                }

                                usuario.contrasena != contrasena -> {
                                    mensaje = "La contraseña es incorrecta"
                                }

                                else -> {
                                    mensaje = ""
                                    onEntrar(usuario.idUsuario)
                                }
                            }
                        } catch (e: Exception) {
                            mensaje = "Ocurrió un error al iniciar sesión"
                        } finally {
                            cargando = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text(
                if (cargando) "Verificando..." else "Entrar"
            )
        }

        if (mensaje.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { mensaje = "" },
                title = { Text("Inicio de sesión") },
                text = { Text(mensaje) },
                confirmButton = {
                    TextButton(onClick = { mensaje = "" }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onRegistrar) {
            Text("¿No tienes cuenta? Regístrate")
        }

        TextButton(onClick = onRegresar) {
            Text("Regresar")
        }
    }
}