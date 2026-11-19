package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.FeriadosApi
import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.model.Feriado

// REPOSITORIO de la API externa. Solo consulta internet: NO guarda nada en Room ni habla con los microservicios.
// EXPLICAR: así lo que falle con esta API (sin internet, servicio caído) no afecta a las solicitudes ni a los datos propios.
class FeriadosRepository(
    private val api: FeriadosApi = RetrofitInstance.feriados
) {
    // Devuelve los próximos feriados de Chile desde 'hoy' (texto 'aaaa-mm-dd').
    // AVANZADO: como las fechas están escritas 'año-mes-día', comparar los textos equivale a comparar las fechas.
    suspend fun proximos(hoy: String, cantidad: Int = 3): List<Feriado> {
        val anio = hoy.take(4).toInt()
        return api.feriados(anio, "CL")
            .filter { it.date >= hoy }
            .sortedBy { it.date }
            .take(cantidad)
    }
}
