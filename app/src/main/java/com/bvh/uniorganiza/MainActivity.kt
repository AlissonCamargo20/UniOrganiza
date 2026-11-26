package com.bvh.uniorganiza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bvh.uniorganiza.data.database.DatabaseProvider
import com.bvh.uniorganiza.data.repository.UniOrganizaRepository
import com.bvh.uniorganiza.ui.screens.*
import com.bvh.uniorganiza.ui.theme.AppTheme
import com.bvh.uniorganiza.ui.viewmodel.UniOrganizaViewModel
import com.bvh.uniorganiza.ui.viewmodel.UniOrganizaViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val baseDatos =
            DatabaseProvider.obtenerBaseDatos(applicationContext)

        setContent {
            AppTheme {
                var pantalla by remember {
                    mutableStateOf("inicio")
                }

                var usuarioId by remember {
                    mutableIntStateOf(0)
                }

                when (pantalla) {

                    "inicio" -> PantallaInicio(
                        onIniciar = {
                            pantalla = "login"
                        },
                        onRegistrar = {
                            pantalla = "registro"
                        }
                    )

                    "login" -> PantallaLogin(
                        onRegresar = {
                            pantalla = "inicio"
                        },
                        onRegistrar = {
                            pantalla = "registro"
                        },
                        onEntrar = { id ->
                            usuarioId = id
                            pantalla = "principal"
                        },
                        usuarioDao = baseDatos.usuarioDao()
                    )

                    "registro" -> PantallaRegistro(
                        onRegresar = {
                            pantalla = "inicio"
                        },
                        onIniciar = {
                            pantalla = "login"
                        },
                        usuarioDao = baseDatos.usuarioDao()
                    )

                    "principal" -> {
                        PrincipalConViewModel(
                            usuarioId = usuarioId,
                            baseDatos = baseDatos,
                            onCerrarSesion = {
                                usuarioId = 0
                                pantalla = "inicio"
                            }
                        )
                    }

                    "bienvenida" -> PantallaBienvenida(
                        onCerrar = {
                            pantalla = "inicio"
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PrincipalConViewModel(
    usuarioId: Int,
    baseDatos: com.bvh.uniorganiza.data.database.UniOrganizaDatabase,
    onCerrarSesion: () -> Unit
) {
    val repository = remember(usuarioId) {
        UniOrganizaRepository(
            baseDatos.tareaDao(),
            baseDatos.categoriaDao()
        )
    }

    val factory = remember(usuarioId) {
        UniOrganizaViewModelFactory(
            repository,
            usuarioId
        )
    }

    val principalViewModel: UniOrganizaViewModel = viewModel(
        key = "UniOrganiza-$usuarioId",
        factory = factory
    )

    PantallaPrincipal(
        usuarioId = usuarioId,
        tareaDao = baseDatos.tareaDao(),
        recordatorioDao = baseDatos.recordatorioDao(),
        viewModel = principalViewModel,
        onCerrarSesion = onCerrarSesion
    )
}