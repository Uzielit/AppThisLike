package com.utez.thislike.data.model

data class Foto(
    val id: String,
    val userId: String,
    val nombreUsuario: String,
    val fotoUsuario: String,
    //Para mandar al servidor
    val imagenBase64: String,
    val audioBase64: String?,

    val descripcion: String,
    val categoria: String,
    var likes: Int = 0,
    val fecha: String? = null
)