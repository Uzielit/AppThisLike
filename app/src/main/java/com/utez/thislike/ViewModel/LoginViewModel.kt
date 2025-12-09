package com.utez.thislike.ViewModel

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

class LoginViewModel : ViewModel() {
    //Instancia del repositorio
    private val repository = Repository()
    var usuario = MutableStateFlow("")
    var password = MutableStateFlow("")


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _loginResult = MutableStateFlow<Result<User>?>(null)
    val loginResult: StateFlow<Result<User>?> = _loginResult
    fun onUsuarioChange(text: String) { usuario.value = text }
    fun onPasswordChange(text: String) { password.value = text }
    var errorMessage by mutableStateOf<String?>(null)



    fun login() {
        if (usuario.value.isEmpty() || password.value.isEmpty()){
            errorMessage = "Ingresa matrícula y contraseña"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            errorMessage = null
            val resultado = repository.login(usuario.value, password.value)
            _loginResult.value = resultado
            if (resultado.isFailure) {
                errorMessage = resultado.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }
}