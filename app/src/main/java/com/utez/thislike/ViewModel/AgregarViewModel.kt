package com.utez.thislike.ViewModel

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID

class AgregarViewModel : ViewModel() {
    private val repository = Repository()
    var descripcion = MutableStateFlow("")
    var categoria = MutableStateFlow("")

    var fotoUri = MutableStateFlow<Uri?>(null)
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    var isRecording = MutableStateFlow(false)
    var audioGrabado = MutableStateFlow(false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess: StateFlow<Boolean> = _uploadSuccess

    var errorMessage by mutableStateOf<String?>(null)

    fun onDescripcionChange(text: String) { descripcion.value = text }
    fun onCategoriaChange(text: String) { categoria.value = text }
    fun onFotoTomada(uri: Uri) { fotoUri.value = uri }

    // Audio

    fun startRecording(context: Context) {
        try {
            // Crea arhcivo de auido
            val outputDir = context.cacheDir
            audioFile = File.createTempFile("audio_nota", ".mp3", outputDir)

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
            isRecording.value = true
        } catch (e: IOException) {
            errorMessage = "Error al iniciar grabación: ${e.message}"
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording.value = false
            audioGrabado.value = true
        } catch (e: Exception) {
            errorMessage = "Error al detener grabación"
        }
    }

    // Se sube el python

    fun subirPost(context: Context, userId: String, userName: String, userAvatar: String) {
        if (fotoUri.value == null) {
            errorMessage = "Necesitas tomar la foto"
            return
        }
        if (descripcion.value.isEmpty() || categoria.value.isEmpty()) {
            errorMessage = "Llena todos los campos"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val inputStream = context.contentResolver.openInputStream(fotoUri.value!!)
                val bytesFoto = inputStream?.readBytes()
                val base64Foto = Base64.encodeToString(bytesFoto, Base64.NO_WRAP)
                inputStream?.close()
                var base64Audio: String? = null
                if (audioFile != null && audioGrabado.value) {
                    val bytesAudio = audioFile!!.readBytes()
                    base64Audio = Base64.encodeToString(bytesAudio, Base64.NO_WRAP)
                }
                val nuevaFoto = Foto(
                    id = "",
                    userId = userId,
                    nombreUsuario = userName,
                    fotoUsuario = userAvatar,
                    imagenBase64 = base64Foto ?: "",
                    audioBase64 = base64Audio,
                    descripcion = descripcion.value,
                    categoria = categoria.value.ifEmpty { "Sin categoria" }
                )

                val result = repository.subirFoto(nuevaFoto)

                if (result.isSuccess) {
                    _uploadSuccess.value = true
                } else {
                    errorMessage = "Error al subir: ${result.exceptionOrNull()?.message}"
                }

            } catch (e: Exception) {
                errorMessage = "Error no se pudo subir el archivo : ${e.message}"
            }

            _isLoading.value = false
        }
    }
}