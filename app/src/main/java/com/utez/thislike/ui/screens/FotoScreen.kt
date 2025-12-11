package com.utez.thislike.ui.screens

import android.media.MediaPlayer
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utez.thislike.ViewModel.FotoViewModel
import com.utez.thislike.ui.components.FotoCard
import com.utez.thislike.ui.components.UserFotoPerfil
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FotoScreen(navController: NavController) {
    val viewModel: FotoViewModel = viewModel()
    val foto = viewModel.foto

    if (foto == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Publicacion", fontWeight = FontWeight.Bold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atras",tint = Color.Black)
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
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserFotoPerfil(fotoString = foto.fotoUsuario, size = 40.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = foto.nombreUsuario, fontWeight = FontWeight.Bold)
            }

            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                FotoCard(foto = foto, onClick = {})
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.darLike() }) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.Red
                    )
                }
                Text(text = "${viewModel.likes} Me gusta", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.Guardar() }) {
                    Icon(
                        imageVector = if (viewModel.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Guardar",
                        tint = if (viewModel.isSaved) Color(0xFFFFD700) else Color.Black
                    )
                }
            }

            Divider(thickness = 0.5.dp, color = Color.LightGray, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            if (foto.audioBase64 != null && foto.audioBase64.isNotEmpty()) {
                Text(
                    text = "Audio",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                AudioPlayer(audioBase64 = foto.audioBase64)

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Text(
                    text = "Ups no se agrego audio en esta publicacion",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Descripcion", fontSize = 12.sp, color = Color.Gray)
                Text(text = foto.descripcion, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Etiqueta", fontSize = 12.sp, color = Color.Gray)
                SuggestionChip(
                    onClick = {},
                    label = { Text(foto.categoria) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color(0xFFF0F0F0),
                        labelColor = Color.Black
                    ),
                    border = null
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

// Funcion composable para la reproduccion de audio
@Composable
fun AudioPlayer(audioBase64: String) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        android.media.MediaPlayer().apply {
            try {
                // Convertir Base64 a Archivo Temporal
                val audioBytes = Base64.decode(audioBase64, Base64.NO_WRAP)
                val tempFile = File.createTempFile("play_temp", ".mp3", context.cacheDir)
                val fos = FileOutputStream(tempFile)
                fos.write(audioBytes)
                fos.close()

                setDataSource(tempFile.absolutePath)
                prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    DisposableEffect(mediaPlayer) {
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
        }
        onDispose {}
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFEEEEEE))
            .clickable {
                if (isPlaying) {
                    mediaPlayer.pause()
                    isPlaying = false
                } else {
                    mediaPlayer.start()
                    isPlaying = true
                }
            }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = "Reproducir",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (isPlaying) "Reproducienddo." else "Escuchar audio", fontSize = 14.sp)
        Spacer(modifier = Modifier.width(16.dp))
    }
}