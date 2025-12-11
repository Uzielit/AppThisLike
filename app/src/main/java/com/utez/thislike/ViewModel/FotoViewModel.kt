package com.utez.thislike.ViewModel

import android.system.Os.remove
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utez.thislike.data.model.SelectionManager
import com.utez.thislike.data.model.SessionManager
import com.utez.thislike.data.repository.Repository
import kotlinx.coroutines.launch

class FotoViewModel : ViewModel() {
    private val repository = Repository()

    val foto = SelectionManager.fotoSeleccionada
    val usuario = SessionManager.currentUser

    var likes by mutableStateOf(foto?.likes ?: 0)
    var isSaved by mutableStateOf(
        usuario?.postGuardados?.contains(foto?.id) == true
    )

    fun darLike() {
        if (foto == null) return
        likes += 1
        foto.likes = likes
        viewModelScope.launch {
            val datos = mapOf("likes" to likes)
            repository.actualizarFoto(foto.id, datos)
        }
    }

    fun Guardar() {
        val user = SessionManager.currentUser

        if (foto == null || user == null) return

        viewModelScope.launch {
            if (user.postGuardados == null) {
                user.postGuardados = arrayListOf()
            }

            if (isSaved) {
                isSaved = false

                try {
                    user.postGuardados.remove(foto.id)
                } catch (e: Exception) {

                    val listaMutable = user.postGuardados.toMutableList()
                    listaMutable.remove(foto.id)
                    user.postGuardados = ArrayList(listaMutable)
                }

                val datos = mapOf("borrar_guardado_id" to foto.id)
                repository.actualizarUsuario(user.id, datos)

            } else {
                isSaved = true
                try {
                    user.postGuardados.add(foto.id)
                } catch (e: Exception) {
                    val listaMutable = user.postGuardados.toMutableList()
                    listaMutable.add(foto.id)
                    user.postGuardados = ArrayList(listaMutable)
                }
                val datos = mapOf("guardar_foto_id" to foto.id)
                repository.actualizarUsuario(user.id, datos)
            }
        }
    }
}