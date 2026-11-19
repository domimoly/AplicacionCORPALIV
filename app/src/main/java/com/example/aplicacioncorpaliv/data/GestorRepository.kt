package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.AdminApi
import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.model.CambioEstado
import com.example.aplicacioncorpaliv.model.LoginRequest
import com.example.aplicacioncorpaliv.model.LoginResponse
import com.example.aplicacioncorpaliv.model.Solicitud

// REPOSITORIO DEL GESTOR: habla con ms-admin (login) y con ms-vinculacion (solicitudes).
// EXPLICAR: ya no hay una clave de demostración dentro de la app. La clave se envía al servidor (ms-admin), que la compara
// con la guardada (cifrada con BCrypt) y, si es correcta, devuelve un TOKEN JWT. Ese token se envía en cada llamada
// del gestor con el formato "Bearer <token>" para demostrar que ya inició sesión.
class GestorRepository(
    private val adminApi: AdminApi = RetrofitInstance.admin,
    private val vinculacionApi: VinculacionApi = RetrofitInstance.vinculacion
) {
    suspend fun iniciarSesion(correo: String, clave: String): LoginResponse {
        return adminApi.login(LoginRequest(correo.trim().lowercase(), clave))
    }

    suspend fun listar(token: String): List<Solicitud> = vinculacionApi.listar(bearer(token))

    suspend fun cambiarEstado(token: String, id: Int, estado: String, observacion: String): Solicitud {
        return vinculacionApi.cambiarEstado(bearer(token), id.toLong(), CambioEstado(estado, observacion.trim()))
    }

    suspend fun eliminar(token: String, id: Int) {
        vinculacionApi.eliminar(bearer(token), id.toLong())
    }

    private fun bearer(token: String) = "Bearer $token"
}
