package com.utez.thislike.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.SessionManager
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarPerfilViewModel : ViewModel() {
    private val repository = Repository()
    val usuario = SessionManager.currentUser

    // Iniciamos con los datos que ya tenemos
    var nombre = MutableStateFlow(usuario?.nombre ?: "")
    var email = MutableStateFlow(usuario?.email ?: "")
    var password = MutableStateFlow(usuario?.password ?: "")


    // Foto de perfil (Inicia con la actual)
    var fotoPerfil = MutableStateFlow(usuario?.fotoPerfil ?: "avatar_1")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    var errorMessage by mutableStateOf<String?>(null)

    // Setters
    fun onNombreChange(text: String) { nombre.value = text }
    fun onEmailChange(text: String) { email.value = text }
    fun onPassChange(text: String) { password.value = text }

    // Lógica de Galería (La misma del Registro mejorada)
    fun abrirGaleria(context: Context, uri: Uri?) {
        if (uri == null) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                val outputStream = java.io.ByteArrayOutputStream()
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 50, outputStream)
                val bytes = outputStream.toByteArray()
                val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                fotoPerfil.value = base64String
            } catch (e: Exception) {
                errorMessage = "Error al procesar imagen"
            }
        }
    }
    fun onAvatarSelected(avatarCode: String) {
        fotoPerfil.value = avatarCode
    }

    fun guardarCambios() {
        if (usuario == null) return
        val currentUser = SessionManager.currentUser

        viewModelScope.launch {
            _isLoading.value = true

            val datos = mapOf(
                "nombre" to nombre.value,
                "email" to email.value,
                "password" to password.value,
                "fotoPerfil" to fotoPerfil.value
            )

            val resultado = repository.actualizarUsuario(usuario.id, datos)

            if (resultado.isSuccess) {

                val userActualizado = resultado.getOrNull()
                if (userActualizado != null) {
                    userActualizado.postGuardados = currentUser?.postGuardados ?: arrayListOf()
                    SessionManager.currentUser = userActualizado
                }
                _updateSuccess.value = true
            } else {
                errorMessage = "Error: ${resultado.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    fun eliminarCuenta() {
        val id = usuario?.id ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val res = repository.eliminarUsuario(id)
            if (res.isSuccess) {
                SessionManager.currentUser = null
                _updateSuccess.value = true
            }
            _isLoading.value = false
        }
    }
}