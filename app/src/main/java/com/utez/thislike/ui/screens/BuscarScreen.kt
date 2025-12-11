package com.utez.thislike.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
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
import com.utez.thislike.ViewModel.BuscarViewModel
import com.utez.thislike.ui.components.FotoCard
import com.utez.thislike.ui.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarScreen(navController: NavController) {
    val viewModel: BuscarViewModel = viewModel()
    val textoBusqueda by viewModel.query.collectAsState()
    val resultados by viewModel.resultados.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val AzulApple = Color(0xFF007AFF)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Busqueda", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = viewModel::onSearchChange,
                    placeholder = { Text("Busca categoria , usuarios ", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    trailingIcon = {
                        if (textoBusqueda.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Borrar", tint = Color.Gray)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulApple,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(70.dp)
                    .shadow(15.dp, RoundedCornerShape(50.dp)),
                shape = RoundedCornerShape(50.dp),
                color = Color.White
            ) {
                NavigationBar(containerColor = Color.Transparent, contentColor = Color.Black, tonalElevation = 0.dp) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.VistaPrincipal.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = true,
                        onClick = { },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AzulApple,
                            indicatorColor = AzulApple.copy(alpha = 0.1f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Agregar.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Black)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp)) },
                        selected = false,
                        onClick = { navController.navigate(AppScreens.Cuenta.route) },
                        colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray)
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AzulApple)
            } else {
                if (resultados.isEmpty()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No se Encontraron Resultados", color = Color.Gray)
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        content = {
                            items(resultados) { foto ->
                                FotoCard(foto = foto, onClick = {})
                            }
                        }
                    )
                }
            }
        }
    }
}

