package com.utez.thislike.ui

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utez.thislike.ui.screens.*

//Se define la rura a donde ira
sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object Registro : AppScreens("registro")
    object VistaPrincipal : AppScreens("home")
    object Buscar : AppScreens("buscar")
    object Agregar : AppScreens("agregar")
    object Cuenta : AppScreens("cuenta")
}


@Composable
fun Navigation (){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route
    ) {

        composable(AppScreens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreens.VistaPrincipal.route) {
                        popUpTo(AppScreens.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppScreens.Registro.route)
                }
            )
        }

        composable(AppScreens.Registro.route) {
            RegistroScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppScreens.VistaPrincipal.route) {
            VistaPrincipalScreen(navController)
        }



       /*
        composable(AppScreens.Buscar.route) {
            BuscarScreen(navController)
        }

        composable(AppScreens.Agregar.route) {
            AgregarScreen(navController) // ¡Aquí haremos la magia de la cámara!
        }

        composable(AppScreens.Cuenta.route) {
            CuentaScreen(navController)
        }
        */
    }


}