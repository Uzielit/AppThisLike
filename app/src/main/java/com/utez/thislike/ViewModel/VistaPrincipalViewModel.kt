package com.utez.thislike.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VistaPrincipalViewModel : ViewModel() {
    private val repository = Repository()
    private val _fotos = MutableStateFlow<List<Foto>>(emptyList())
    val fotos: StateFlow<List<Foto>> = _fotos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cargarFeed()
    }

    fun cargarFeed() {
        viewModelScope.launch {
            _isLoading.value = true
            val resultado = repository.obtenerFeed()

            if (resultado.isSuccess) {
                _fotos.value = resultado.getOrDefault(emptyList())
            } else {
                println("Error cargando feed: ${resultado.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }
}