package com.utez.thislike.data.remote

import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.model.LoginRequest
import com.utez.thislike.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Usuarios
    @POST("api/usuarios/registro")
    suspend fun registrarUsuario(@Body user: User): Response<Map<String, String>>
    @POST("api/usuarios/login")
    suspend fun loginUsuario(@Body request: LoginRequest): Response<User>
    @PUT("api/usuarios/update/{id}")
    suspend fun actualizarUsuario(@Path("id") id: String, @Body data: Map<String, @JvmSuppressWildcards Any>): Response<User>

    @DELETE("api/usuarios/delete/{id}")
    suspend fun eliminarUsuario(@Path("id") id: String): Response<Map<String, String>>

    //Fotos

    @GET("posts")
    suspend fun obtenerFotos(): Response<List<Foto>>
    @POST("posts")
    suspend fun subirFoto(@Body foto: Foto): Response<Foto>
    @PUT("posts/{id}")
    suspend fun editarFoto(@Path("id") id: String, @Body data: Map<String, @JvmSuppressWildcards Any>): Response<Foto>
    @DELETE("posts/{id}")
    suspend fun eliminarFoto(@Path("id") id: String): Response<Map<String, String>>

}