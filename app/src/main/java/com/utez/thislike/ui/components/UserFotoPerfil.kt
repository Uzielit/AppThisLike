package com.utez.thislike.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.utez.thislike.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Base64

@Composable
fun UserFotoPerfil(
    fotoString: String,
    size: Dp = 40.dp
) {
    if (fotoString.startsWith("avatar_")){
        val modelo = when (fotoString) {
            "avatar_1" -> R.drawable.gato1_jpg
            "avatar_2" -> R.drawable.gato2
            "avatar_3" -> R.drawable.gato3_png
            else -> R.drawable.im2
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

    } else{
        val imagenGaleria = produceState<ImageBitmap?>(initialValue = null, key1 = fotoString) {
            value = withContext(Dispatchers.IO) {
                try {
                    val imageBytes = Base64.decode(fotoString, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)?.asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
        }

        if (imagenGaleria.value != null) {
            Image(
                bitmap = imagenGaleria.value!!,
                contentDescription = "Imagen Galeria",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        }else {
            AsyncImage(
                model = R.drawable.gatoesperando,
                contentDescription = null,
                modifier = Modifier.size(size).clip(CircleShape)
            )
        }

    }


}