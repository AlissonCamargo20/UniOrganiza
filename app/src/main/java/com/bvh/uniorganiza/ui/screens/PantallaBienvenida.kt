package com.bvh.uniorganiza.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bvh.uniorganiza.R

// Aquí se crea la pantalla de bienvenida
@Composable
fun PantallaBienvenida(onCerrar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Aquí se muestra el mensaje de bienvenida
        Text(
            text = stringResource(R.string.bienvenida),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Aquí se muestra un mensaje de bienvenida
        Text(
            text = stringResource(R.string.mensaje_bienvenida),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Aquí se crea el botón para cerrar sesión
        Button(onClick = onCerrar) {
            Text(stringResource(R.string.cerrar_sesion))
        }
    }
}