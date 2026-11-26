package com.bvh.uniorganiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.bvh.uniorganiza.data.database.DatabaseProvider
import com.bvh.uniorganiza.ui.screens.PantallaBienvenida
import com.bvh.uniorganiza.ui.screens.PantallaInicio
import com.bvh.uniorganiza.ui.screens.PantallaLogin
import com.bvh.uniorganiza.ui.screens.PantallaRegistro
import com.bvh.uniorganiza.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Aquí se activa la pantalla de apertura
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Aquí se conecta la base de datos
        val baseDatos = DatabaseProvider.obtenerBaseDatos(applicationContext)

        // Aquí se comprueba la conexión con las dos tablas
        lifecycleScope.launch(Dispatchers.IO) {
            baseDatos.usuarioDao().obtenerUsuarios()
            baseDatos.tareaDao().obtenerTareas()
        }

        setContent {
            AppTheme {

                // Aquí se controla qué pantalla se muestra
                var pantalla by remember { mutableStateOf("inicio") }

                // Aquí se cambia entre las pantallas de la aplicación
                when (pantalla) {

                    // Aquí se muestra la pantalla de inicio
                    "inicio" -> PantallaInicio(
                        onIniciar = { pantalla = "login" },
                        onRegistrar = { pantalla = "registro" }
                    )

                    // Aquí se muestra la pantalla de inicio de sesión
                    "login" -> PantallaLogin(
                        onRegresar = { pantalla = "inicio" },
                        onRegistrar = { pantalla = "registro" },
                        onEntrar = { pantalla = "bienvenida" }
                    )

                    // Aquí se muestra la pantalla de registro
                    "registro" -> PantallaRegistro(
                        onRegresar = { pantalla = "inicio" },
                        onIniciar = { pantalla = "login" },
                        usuarioDao = baseDatos.usuarioDao()
                    )

                    // Aquí se muestra la pantalla de bienvenida
                    "bienvenida" -> PantallaBienvenida(
                        onCerrar = { pantalla = "inicio" }
                    )
                }
            }
        }
    }
}