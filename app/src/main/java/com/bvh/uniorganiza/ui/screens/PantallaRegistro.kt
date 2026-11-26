package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.R
import com.bvh.uniorganiza.data.database.UsuarioDao
import com.bvh.uniorganiza.data.database.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Aquí se crea la pantalla de registro
@Composable
fun PantallaRegistro(
    onRegresar: () -> Unit,
    onIniciar: () -> Unit,
    usuarioDao: UsuarioDao
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
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
            text = stringResource(R.string.crear_cuenta),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.nombre_completo)) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.correo)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmarContrasena,
            onValueChange = { confirmarContrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.confirmar_contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                android.util.Log.d("REGISTRO", "Se presionó el botón")
                when {
                    nombre.isBlank() || correo.isBlank() ||
                            contrasena.isBlank() || confirmarContrasena.isBlank() -> {
                        mensaje = "Completa todos los campos"
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> {
                        mensaje = "Ingresa un correo electrónico válido"
                    }

                    contrasena != confirmarContrasena -> {
                        mensaje = "Las contraseñas no coinciden"
                    }

                    else -> {
                        cargando = true

                        scope.launch {
                            try {
                                val resultado = withContext(Dispatchers.IO) {
                                    val correoExiste = usuarioDao.buscarPorCorreo(
                                        correo.trim().lowercase()
                                    )

                                    if (correoExiste != null) {
                                        false
                                    } else {
                                        usuarioDao.insertarUsuario(
                                            UsuarioEntity(
                                                nombre = nombre.trim(),
                                                correo = correo.trim().lowercase(),
                                                contrasena = contrasena
                                            )
                                        )
                                        true
                                    }
                                }

                                mensaje = if (resultado) {
                                    "¡Tu cuenta se creó correctamente!"
                                } else {
                                    "Este correo ya está registrado"
                                }

                            } catch (e: Exception) {
                                mensaje = "No se pudo guardar el usuario. Inténtalo nuevamente."
                            } finally {
                                cargando = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text(
                if (cargando) "Registrando..."
                else stringResource(R.string.registrarme)
            )
        }

        if (mensaje.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { mensaje = "" },
                title = {
                    Text(stringResource(R.string.registro))
                },
                text = {
                    Text(mensaje)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val registroExitoso =
                                mensaje == "¡Tu cuenta se creó correctamente!"

                            mensaje = ""

                            if (registroExitoso) {
                                onIniciar()
                            }
                        }
                    ) {
                        Text(stringResource(R.string.aceptar))
                    }
                }
            )
        }

        TextButton(onClick = onIniciar) {
            Text(stringResource(R.string.ya_tienes_cuenta))
        }

        TextButton(onClick = onRegresar) {
            Text(stringResource(R.string.regresar))
        }
    }
}