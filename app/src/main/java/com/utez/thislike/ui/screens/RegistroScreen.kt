package com.utez.thislike.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utez.thislike.ViewModel.RegistroViewModel
import com.utez.thislike.ui.components.ThisLikeButton
import com.utez.thislike.ui.components.ThisLikeInput
import com.utez.thislike.ui.components.UserAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: RegistroViewModel = viewModel()


    val nombre by viewModel.nombre.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val fotoActual by viewModel.fotoPerfil.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Foto de Perfil", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Black, CircleShape)
                        .padding(4.dp)
                ) {
                    UserAvatar(fotoString = fotoActual, size = 100.dp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    //Aqui esta esperando la galeria
                    OutlinedButton(onClick = { }) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galería", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("O elige uno:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AvatarOpcion("avatar_1", fotoActual, viewModel::onAvatarSelected)
                        AvatarOpcion("avatar_2", fotoActual, viewModel::onAvatarSelected)
                        AvatarOpcion("avatar_3", fotoActual, viewModel::onAvatarSelected)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ThisLikeInput(
                value = nombre,
                onValueChange = viewModel::onNombreChange,
                label = "Nombre de Usuario"
            )
            Spacer(modifier = Modifier.height(16.dp))

            ThisLikeInput(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = "Correo Electrónico"
            )
            Spacer(modifier = Modifier.height(16.dp))

            ThisLikeInput(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Contraseña",
                isPassword = true
            )

            if (viewModel.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                ThisLikeButton(
                    text = "Registrarme",
                    onClick = { viewModel.registrar() }
                )
            }
        }
    }
}

// Componente chiquito para los circulitos seleccionables
@Composable
fun AvatarOpcion(
    nombreAvatar: String,
    seleccionadoActual: String,
    onSelect: (String) -> Unit
) {
    val esElSeleccionado = nombreAvatar == seleccionadoActual

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onSelect(nombreAvatar) }
            .background(if (esElSeleccionado) Color.LightGray else Color.Transparent) // Resaltar si está seleccionado
            .padding(2.dp) // Margen interno
    ) {
        UserAvatar(fotoString = nombreAvatar, size = 40.dp)
    }
}