package com.utez.thislike.data.model

import com.utez.thislike.data.model.User

//Sirve para administrar al la hora de guardar el objeto
object SessionManager {
    var currentUser: User? = null
}

object SelectionManager {
    var fotoSeleccionada: Foto? = null
}