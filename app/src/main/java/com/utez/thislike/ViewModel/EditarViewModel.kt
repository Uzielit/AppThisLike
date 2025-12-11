package com.utez.thislike.ViewModel

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.SelectionManager
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditarViewModel : ViewModel() {
    private val repository = Repository()


    val foto = SelectionManager.fotoSeleccionada
    var descripcion = MutableStateFlow(foto?.descripcion ?: "")
    var categoria = MutableStateFlow(foto?.categoria ?: "")


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    var errorMessage by mutableStateOf<String?>(null)

    //Para la imagen y audio combertido en formato
    var fotoUri = MutableStateFlow<Uri?>(null)
    var audioGrabado = MutableStateFlow(false)
    // Audio
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    var isRecording = MutableStateFlow(false)
    var nuevoAudioGrabado = MutableStateFlow(false)

    fun onDescripcionChange(text: String) { descripcion.value = text }
    fun onCategoriaChange(text: String) { categoria.value = text }

    fun onNuevaFotoTomada(uri: Uri) { fotoUri.value = uri }


    fun eliminarPost() {
        if (foto == null) return
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repository.eliminarFoto(foto.id)
            if (resultado.isSuccess) {
                _updateSuccess.value = true
            } else {
                errorMessage = "No se pudo eliminar: ${resultado.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    fun actualizar(context: Context) {
        if (foto == null) return

        viewModelScope.launch {
            _isLoading.value = true
            val datosActualizados = mutableMapOf<String, Any>(
                "descripcion" to descripcion.value,
                "categoria" to categoria.value
            )
            if (fotoUri.value != null) {
                withContext(Dispatchers.IO) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(fotoUri.value!!)
                        val bytes = inputStream?.readBytes()
                        if (bytes != null) {
                            val base64Foto = Base64.encodeToString(bytes, Base64.NO_WRAP)
                            datosActualizados["imagenBase64"] = base64Foto
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (nuevoAudioGrabado.value && audioFile != null) {
                withContext(Dispatchers.IO) {
                    try {
                        val bytes = audioFile!!.readBytes()
                        val base64Audio = Base64.encodeToString(bytes, Base64.NO_WRAP)
                        datosActualizados["audioBase64"] = base64Audio
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // 4. Enviamos al servidor
            val resultado = repository.actualizarFoto(foto.id, datosActualizados)

            if (resultado.isSuccess) {
                _updateSuccess.value = true
            } else {
                errorMessage = "Error ${resultado.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }
    fun startRecording(context: Context) {
        try {
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
            nuevoAudioGrabado.value = true
        } catch (e: Exception) {
            errorMessage = "Error al detener grabación"
        }
    }
}