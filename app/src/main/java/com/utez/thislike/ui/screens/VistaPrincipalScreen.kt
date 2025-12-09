package com.utez.thislike.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import com.utez.thislike.ViewModel.VistaPrincipalViewModel
import com.utez.thislike.ui.components.FotoCard
import com.utez.thislike.ui.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaPrincipalScreen(navController: NavController) {
    val viewModel: VistaPrincipalViewModel = viewModel()
    val fotos by viewModel.fotos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    val AzulApple = Color(0xFF007AFF)

    Scaffold(
        containerColor = Color.White,

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Fotos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },

       //Barrita
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .height(65.dp)
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(40.dp)
                    ),
                shape = RoundedCornerShape(40.dp),
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    tonalElevation = 0.dp
                ) {

                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Inicio",
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        selected = true,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AzulApple,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = AzulApple.copy(alpha = 0.1f)
                        )
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Buscar.route) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = "Agregar",
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Agregar.route) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Black
                        )
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Perfil",
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Cuenta.route) },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AzulApple
                )
            } else {
                if (fotos.isEmpty()) {
                    Text(
                        text = "No hay fotos aún.\nComienza A Publicar ",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        content = {
                            items(fotos) { foto ->
                                FotoCard(foto = foto, onClick = {})
                            }
                        }
                    )
                }
            }
        }
    }
}