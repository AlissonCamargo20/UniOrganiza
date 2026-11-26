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

// Aquí se crea la pantalla de iniciar sesión
@Composable
fun PantallaLogin(
    onRegresar: () -> Unit,
    onRegistrar: () -> Unit,
    onEntrar: () -> Unit
) {
    // Aquí se guardan los datos que escribo
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Aquí se muestra el título de inicio de sesión
        Text(
            text = stringResource(R.string.iniciar_sesion),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Aquí se escribe el correo electrónico
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

        Spacer(modifier = Modifier.height(16.dp))

        // Aquí se escribe la contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.contrasena)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Aquí se crea el botón para entrar
        Button(
            onClick = {
                if (correo.isNotBlank() && contrasena.isNotBlank()) {
                    onEntrar()
                } else {
                    mensaje = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.entrar))
        }

        // Aquí se muestra un aviso si faltan datos
        if (mensaje) {
            AlertDialog(
                onDismissRequest = { mensaje = false },
                title = { Text(stringResource(R.string.iniciar_sesion)) },
                text = {
                    Text(stringResource(R.string.completar_campos))
                },
                confirmButton = {
                    TextButton(onClick = { mensaje = false }) {
                        Text(stringResource(R.string.aceptar))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Aquí se crea el acceso a la pantalla de registro
        TextButton(onClick = onRegistrar) {
            Text(stringResource(R.string.sin_cuenta))
        }

        // Aquí se crea el botón para regresar
        TextButton(onClick = onRegresar) {
            Text(stringResource(R.string.regresar))
        }
    }
}