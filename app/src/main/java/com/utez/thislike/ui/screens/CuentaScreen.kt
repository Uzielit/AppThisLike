package com.utez.thislike.ui.screens

//creacion de cuenta screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utez.thislike.ViewModel.CuentaViewModel
import com.utez.thislike.data.model.SelectionManager
import com.utez.thislike.ui.components.FotoCard
import com.utez.thislike.ui.components.UserFotoPerfil
import com.utez.thislike.ui.AppScreens
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Brightness5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaScreen(navController: NavController) {
    val viewModel: CuentaViewModel = viewModel()
    val misFotos by viewModel.misFotos.collectAsState()
    val usuario = viewModel.usuario
    val Azul = Color(0xFF007AFF)
    val isLoading by viewModel.isLoading.collectAsState()
    val misGuardados by viewModel.guardados.collectAsState()



    //Para que los actualize de manera continua
    LaunchedEffect(Unit) {
        viewModel.cargarMisFotos()
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text("Mi Perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(AppScreens.EditarPerfil.route)
                    }) {
                        Icon(Icons.Default.Brightness5, contentDescription = "Editar", tint = Color.Black)
                    }
                },

                actions = {
                    IconButton(onClick = {
                        viewModel.cerrarSesion()
                        navController.navigate(AppScreens.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .height(70.dp)
                    .shadow(15.dp, RoundedCornerShape(50.dp)),
                shape = RoundedCornerShape(50.dp),
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.VistaPrincipal.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Buscar.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Agregar.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Black)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = true,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Azul,
                            indicatorColor = Azul.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            UserFotoPerfil(fotoString = usuario?.fotoPerfil ?: "avatar_1", size = 120.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usuario?.nombre ?: "Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = usuario?.email ?: "Correo",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = Color.LightGray, thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Mis Publicaciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))
            if (isLoading) {
                CircularProgressIndicator(color = Azul)
            }else{
                if (misFotos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aqui no nay nara", color = Color.Gray)
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(misFotos) { foto ->
                            Box(modifier = Modifier.width(160.dp)) {
                                FotoCard(foto = foto, onClick = {
                                    SelectionManager.fotoSeleccionada = foto
                                    navController.navigate(AppScreens.Editar.route)
                                })
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Guardados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (misGuardados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ups no hay nada guardado aun ", color = Color.Gray)
                    }
                }else{
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(misGuardados) { foto ->
                            Box(modifier = Modifier.width(160.dp)) {
                                FotoCard(foto = foto, onClick = {
                                    SelectionManager.fotoSeleccionada = foto
                                    navController.navigate(AppScreens.FotoScreen.route)
                                })
                            }
                        }
                    }

                }



            }


        }
    }
}


