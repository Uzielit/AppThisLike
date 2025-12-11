package com.utez.thislike.ViewModel

import android.R.attr.text
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.User
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import android.content.Context
import android.util.Base64

class RegistroViewModel : ViewModel() {
    private val repository = Repository()
    var nombre = MutableStateFlow("")
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")


    var fotoPerfil = MutableStateFlow("avatar_1")


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    var errorMessage by mutableStateOf<String?>(null)


    fun onNombreChange(text: String) { nombre.value = text }
    fun onEmailChange(text: String) { email.value = text }
    fun onPasswordChange(text: String) { password.value = text }

    //Recien agregada
    fun abrirGaleria(context: Context, uri: Uri?) {
        if (uri == null) return

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            if (bytes != null) {
                val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                fotoPerfil.value = base64String
            }
        } catch (e: Exception) {
            errorMessage = "Error al procesar imagen"
        }
    }

    fun onAvatarSelected(avatarCode: String) {
        fotoPerfil.value = avatarCode
    }

    fun registrar() {
        if (nombre.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            errorMessage = null

            val user = User(
                id = "",
                nombre = nombre.value,
                email = email.value,
                password = password.value,
                biografia = "",
                fotoPerfil = fotoPerfil.value
            )

            val result = repository.registro(user)

            if (result.isSuccess) {
                _registroExitoso.value = true
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al registrar"
            }
            _isLoading.value = false
        }
    }
}