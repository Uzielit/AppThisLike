package com.utez.thislike.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BuscarViewModel : ViewModel() {
    private val repository = Repository()
    var query = MutableStateFlow("")

    private var todasLasFotos = listOf<Foto>()
    private val _resultados = MutableStateFlow<List<Foto>>(emptyList())
    val resultados: StateFlow<List<Foto>> = _resultados

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        viewModelScope.launch {
            _isLoading.value = true
            val res = repository.obtenerFeed()
            if (res.isSuccess) {
                todasLasFotos = res.getOrDefault(emptyList())
                _resultados.value = todasLasFotos
            }
            _isLoading.value = false
        }
    }

    fun onSearchChange(texto: String) {
        query.value = texto
        if (texto.isEmpty()) {
            _resultados.value = todasLasFotos
            return
        }
        _resultados.value = todasLasFotos.filter { foto ->
            foto.nombreUsuario.contains(texto, ignoreCase = true) ||
                    foto.descripcion.contains(texto, ignoreCase = true) ||
                    foto.categoria.contains(texto, ignoreCase = true)
        }
    }
}


