package com.utez.thislike.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utez.thislike.ViewModel.EditarPerfilViewModel
import com.utez.thislike.data.model.SessionManager.currentUser
import com.utez.thislike.ui.AppScreens
import com.utez.thislike.ui.components.ThisLikeButton
import com.utez.thislike.ui.components.ThisLikeInput
import com.utez.thislike.ui.components.UserFotoPerfil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController) {
    val viewModel: EditarPerfilViewModel = viewModel()
    val context = LocalContext.current

    val nombre by viewModel.nombre.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val fotoActual by viewModel.fotoPerfil.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()
    var alertaBorrado by remember { mutableStateOf(false) }

    if (alertaBorrado) {
        Alerta(
            onDismissRequest = {
                alertaBorrado = false
            },
            onConfirmation = {
                alertaBorrado = false
                viewModel.eliminarCuenta()
            }
        )
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? -> viewModel.abrirGaleria(context, uri) }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            if (currentUser == null) {
                navController.navigate(AppScreens.Login.route) { popUpTo(0) { inclusive = true } }
            } else {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold , color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás",tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        alertaBorrado = true
                        //viewModel.eliminarCuenta()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Cuenta", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
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
            Box(
                modifier = Modifier
                    .border(2.dp, Color.Black, CircleShape)
                    .padding(4.dp)
            ) {
                UserFotoPerfil(fotoString = fotoActual, size = 120.dp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    galleryLauncher.launch("image/*") },
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cambiar Foto", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("O cambia por uno de ellos:", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AvatarOpcion("avatar_1", fotoActual, viewModel::onAvatarSelected)
                AvatarOpcion("avatar_2", fotoActual, viewModel::onAvatarSelected)
                AvatarOpcion("avatar_3", fotoActual, viewModel::onAvatarSelected)
            }

            Spacer(modifier = Modifier.height(32.dp))

            ThisLikeInput(value = nombre, onValueChange = viewModel::onNombreChange, label = "Nombre")
            Spacer(modifier = Modifier.height(16.dp))
            ThisLikeInput(value = email, onValueChange = viewModel::onEmailChange, label = "Correo")
            Spacer(modifier = Modifier.height(16.dp))
            ThisLikeInput(value = password, onValueChange = viewModel::onPassChange, label = "Contraseña")
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = viewModel.errorMessage!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                ThisLikeButton(
                    text = "GUARDAR CAMBIOS",
                    onClick = { viewModel.guardarCambios() },
                    modifier = Modifier.width(250.dp)
                )
            }
        }
    }
}
@Composable
fun Alerta(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Borrado definitivo de tu cuenta") },
        text = { Text("¿Estas seguro de que quieres borrar tu cuenta?") },
        confirmButton = {
            Button(onClick = onConfirmation) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}