package com.utez.thislike.ui.screens


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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.utez.thislike.ViewModel.EditarViewModel
import com.utez.thislike.ui.AppScreens
import com.utez.thislike.ui.components.FotoCard
import com.utez.thislike.ui.components.ThisLikeButton
import com.utez.thislike.ui.components.ThisLikeInput
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import android.Manifest
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarFotoScreen(navController: NavController) {
    val viewModel: EditarViewModel = viewModel()
    val context = LocalContext.current

    val descripcion by viewModel.descripcion.collectAsState()
    val categoria by viewModel.categoria.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val updateSuccess by viewModel.updateSuccess.collectAsState()

    val grabando by viewModel.isRecording.collectAsState()
    val nuevaFotoUri by viewModel.fotoUri.collectAsState()
    val nuevoAudioGrabado by viewModel.nuevoAudioGrabado.collectAsState()

    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            viewModel.onNuevaFotoTomada(tempPhotoUri!!)
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val photoFile = File.createTempFile(
                    "IMG_EDIT_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_",
                    ".jpg",
                    context.externalCacheDir
                )
                tempPhotoUri = FileProvider.getUriForFile(
                    context, "${context.packageName}.provider", photoFile
                )
                cameraLauncher.launch(tempPhotoUri!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al abrir cámara", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara necesario", Toast.LENGTH_SHORT).show()
        }
    }
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.startRecording(context)
    }



    if (viewModel.foto == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            navController.navigate(AppScreens.Cuenta.route) {
                popUpTo(AppScreens.Cuenta.route) { inclusive = true }
            }
            //navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Publicacion", fontWeight = FontWeight.Bold, color = Color.Black) },
                actions = {
                    IconButton(onClick = { viewModel.eliminarPost() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                },
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

            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
            ) {
                if (nuevaFotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(nuevaFotoUri),
                        contentDescription = "Nueva Foto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.pointerInput(Unit) {}) {
                        FotoCard(foto = viewModel.foto!!, onClick = {})
                    }
                }
            }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF5F5F5))
                                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                                .clickable { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cambiar Foto", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Audio Boton
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (grabando) Color.Red else Color.Black) // Rojo si graba
                                .clickable {
                                    if (grabando) {
                                        viewModel.stopRecording()
                                    } else {
                                        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (grabando) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = "Audio",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (grabando)
                            Text("Grabando", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        else if (nuevoAudioGrabado)
                            Text("Audio guardado", color = Color(0xFF007AFF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        else
                            Text("Cambiar Audio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }


            Spacer(modifier = Modifier.height(32.dp))

            ThisLikeInput(
                value = descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = "Editar descripción"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ThisLikeInput(
                value = categoria,
                onValueChange = viewModel::onCategoriaChange,
                label = "Editar etiqueta"
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
                    text = "GUARDAR CAMBIOS",
                    onClick = { viewModel.actualizar(context = context) },
                    modifier = Modifier.width(250.dp)
                )
            }
        }

    }


}
