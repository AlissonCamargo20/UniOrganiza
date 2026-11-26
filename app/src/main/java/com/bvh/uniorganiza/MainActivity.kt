package com.bvh.uniorganiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bvh.uniorganiza.data.database.DatabaseProvider
import com.bvh.uniorganiza.ui.screens.PantallaBienvenida
import com.bvh.uniorganiza.ui.screens.PantallaInicio
import com.bvh.uniorganiza.ui.screens.PantallaLogin
import com.bvh.uniorganiza.ui.screens.PantallaRegistro
import com.bvh.uniorganiza.ui.screens.PantallaPrincipal
import com.bvh.uniorganiza.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val baseDatos = DatabaseProvider.obtenerBaseDatos(applicationContext)

        setContent {
            AppTheme {
                val pantalla = remember { mutableStateOf("inicio") }
                val usuarioId = remember { mutableIntStateOf(0) }

                when (pantalla.value) {

                    "inicio" -> PantallaInicio(
                        onIniciar = {
                            pantalla.value = "login"
                        },
                        onRegistrar = {
                            pantalla.value = "registro"
                        }
                    )

                    "login" -> PantallaLogin(
                        onRegresar = {
                            pantalla.value = "inicio"
                        },
                        onRegistrar = {
                            pantalla.value = "registro"
                        },
                        onEntrar = { id ->
                            usuarioId.intValue = id
                            pantalla.value = "principal"
                        },
                        usuarioDao = baseDatos.usuarioDao()
                    )

                    "registro" -> PantallaRegistro(
                        onRegresar = {
                            pantalla.value = "inicio"
                        },
                        onIniciar = {
                            pantalla.value = "login"
                        },
                        usuarioDao = baseDatos.usuarioDao()
                    )

                    "principal" -> PantallaPrincipal(
                        usuarioId = usuarioId.intValue,
                        tareaDao = baseDatos.tareaDao(),
                        categoriaDao = baseDatos.categoriaDao(),
                        recordatorioDao = baseDatos.recordatorioDao(),
                        onCerrarSesion = {
                            usuarioId.intValue = 0
                            pantalla.value = "inicio"
                        }
                    )

                    "bienvenida" -> PantallaBienvenida(
                        onCerrar = {
                            pantalla.value = "inicio"
                        }
                    )
                }
            }
        }
    }
}