package com.utez.thislike.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.utez.thislike.R

@Composable
fun UserAvatar(
    fotoString: String,
    size: Dp = 40.dp
) {
    val modelo = if (fotoString.startsWith("avatar_")) {
        when(fotoString) {
            "avatar_1" -> R.drawable.gato1_jpg
            "avatar_2" -> R.drawable.gato2
            "avatar_3" -> R.drawable.gato3_png
            else -> R.drawable.im2


        }
    } else {
        fotoString
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(modelo)
            .crossfade(true)
            .build(),
        contentDescription = "Avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}