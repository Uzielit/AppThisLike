package com.utez.thislike.ViewModel

//creacion de nuestra cuenta viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.utez.thislike.data.model.SessionManager

import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.model.SelectionManager
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CuentaViewModel : ViewModel() {
    private val repository = Repository()

    // Obtenemos al usuario actual de la sesion
    var usuario by mutableStateOf(SessionManager.currentUser)


    // Lista de fotos solo las mias
    private val _misFotos = MutableStateFlow<List<Foto>>(emptyList())
    val misFotos: StateFlow<List<Foto>> = _misFotos


    private val _guardados = MutableStateFlow<List<Foto>>(emptyList())
    val guardados: StateFlow<List<Foto>> = _guardados

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        cargarMisFotos()
    }

    fun refrescarUsuario() {
        usuario = SessionManager.currentUser
    }
    fun cargarMisFotos() {
        refrescarUsuario()

        val userId = usuario?.id ?: return

        val listaGuardadosIds = usuario?.postGuardados ?: emptyList()
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repository.obtenerFeed()
            val listaGuardadosIds = usuario?.postGuardados ?: emptyList()

            if (resultado.isSuccess) {
                val todas = resultado.getOrDefault(emptyList())
                _misFotos.value = todas.filter {
                    it.userId == userId }
                _guardados.value = todas.filter { foto ->
                    listaGuardadosIds.contains(foto.id)
                }
            }
            _isLoading.value = false
        }
    }



    fun cerrarSesion() {
        SessionManager.currentUser = null
    }
}