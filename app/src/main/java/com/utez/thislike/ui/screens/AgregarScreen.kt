package com.utez.thislike.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.utez.thislike.ViewModel.AgregarViewModel
import com.utez.thislike.data.model.SessionManager
import com.utez.thislike.ui.components.ThisLikeButton
import com.utez.thislike.ui.components.ThisLikeInput
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarScreen(navController: NavController) {
    val viewModel: AgregarViewModel = viewModel()
    val context = LocalContext.current

    val descripcion by viewModel.descripcion.collectAsState()
    val categoria by viewModel.categoria.collectAsState()
    val fotoUri by viewModel.fotoUri.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val audioGrabado by viewModel.audioGrabado.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()


    // Permiso de AUDIO
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startRecording(context)
        } else {
            Toast.makeText(context, "Se necesita autorizacion de microfono", Toast.LENGTH_SHORT).show()
        }
    }

    // Permiso camara
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            viewModel.onFotoTomada(tempPhotoUri!!)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val photoFile = File.createTempFile(
                    "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                    ".jpg",
                    context.externalCacheDir
                )
                tempPhotoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                cameraLauncher.launch(tempPhotoUri!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al preparar archivo", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }


    LaunchedEffect(uploadSuccess) {
        if (uploadSuccess) {
            navController.popBackStack()
        }
    }


    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Añade una publicaion", fontSize = 24.sp,fontWeight = FontWeight.Bold, color = Color.Black) },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atras", tint = Color.Black)
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

            // Camara
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(20.dp))
                    .background(Color(0xFFF5F5F5))
                    .clickable {
                        val hasCameraPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasCameraPermission) {
                            try {
                                val photoFile = File.createTempFile(
                                    "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                                    ".jpg",
                                    context.externalCacheDir
                                )
                                tempPhotoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    photoFile
                                )
                                cameraLauncher.launch(tempPhotoUri!!)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(fotoUri),
                        contentDescription = "Foto tomada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Gray)
                        Text("Toca para tomar foto", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Audio

            Text("Nota de voz", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (isRecording) Color.Red else Color.Black)
                    .clickable {
                        if (isRecording) {
                            viewModel.stopRecording()
                        } else {
                            val hasAudioPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasAudioPermission) {
                                viewModel.startRecording(context)
                            } else {
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Grabar",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isRecording) {
                Text("Grabando", color = Color.Black, fontWeight = FontWeight.Bold)
                Text("Toca para detener", fontSize = 12.sp, color = Color.Gray)
            } else if (audioGrabado) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Audio guardado correctamente", color = Color(0xFF007AFF), fontWeight = FontWeight.Bold)
                }
                Text("Toca para grabar de nuevo", fontSize = 12.sp, color = Color.Gray)
            } else {
                Text("Toca para iniciar ", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            ThisLikeInput(
                value = descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = "Escribe una descripción"
            )
            Spacer(modifier = Modifier.height(16.dp))
            ThisLikeInput(
                value = categoria,
                onValueChange = viewModel::onCategoriaChange,
                label = "Etiqueta (ropa, comida , lo que imagines)"
            )

            if (viewModel.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = viewModel.errorMessage!!, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(32.dp))


            if (isLoading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                ThisLikeButton(
                    text = "PUBLICAR FOTO",
                    onClick = {
                        val user = SessionManager.currentUser
                        if (user != null) {
                            viewModel.subirPost(
                                context = context,
                                userId = user.id,
                                userName = user.nombre,
                                userAvatar = user.fotoPerfil
                            )
                        } else {
                            Toast.makeText(context, "Error de sesión", Toast.LENGTH_LONG).show()
                            navController.navigate("login")
                        }
                    },
                    modifier = Modifier.width(250.dp)
                )
            }
        }
    }
}