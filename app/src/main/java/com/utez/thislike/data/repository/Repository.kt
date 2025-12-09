package com.utez.thislike.data.repository

import com.utez.thislike.data.model.Foto
import com.utez.thislike.data.model.LoginRequest
import com.utez.thislike.data.model.User
import com.utez.thislike.data.remote.RetrofitClient

class Repository {
    // Instancia de la API
    private val api = RetrofitClient.apiService


    suspend fun login(usuario: String, pass: String): Result<User> {
        return try {
            val request = LoginRequest(usuario = usuario, password = pass)
            val response = api.loginUsuario(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error, Algun dato esta mal "))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registro(user: User): Result<String> {
        return try {
            val response = api.registrarUsuario(user)
            if (response.isSuccessful) {
                Result.success("Usuario creado")
            } else {
                Result.failure(Exception("Error al registrar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarUsuario(id: String, datos: Map<String, Any>): Result<User> {
        return try {
            val response = api.actualizarUsuario(id, datos)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun obtenerFeed(): Result<List<Foto>> {
        return try {
            val response = api.obtenerFotos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar fotos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun subirFoto(foto: Foto): Result<Foto> {
        return try {
            val response = api.subirFoto(foto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al subir foto"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarFoto(id: String): Result<Boolean> {
        return try {
            val response = api.eliminarFoto(id)
            if (response.isSuccessful) Result.success(true) else Result.failure(Exception("Error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}