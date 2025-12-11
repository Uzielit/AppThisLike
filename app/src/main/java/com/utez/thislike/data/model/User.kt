package com.utez.thislike.data.model

data class User(
    val id: String,
    val nombre: String,
    val email: String,
    val password: String,
    val biografia: String,
    val fotoPerfil: String,
    var postGuardados: ArrayList<String> = arrayListOf()
)