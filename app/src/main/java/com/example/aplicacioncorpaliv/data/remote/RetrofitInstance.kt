package com.example.aplicacioncorpaliv.data.remote

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// EXPLICAR: aquí se construye cada conexión (igual que en el ejemplo del profesor, pero con una por microservicio).
// 'by lazy' = se crea la primera vez que se usa y después se reutiliza.
// GsonConverterFactory convierte el JSON que llega en objetos de Kotlin (data class).
// OkHttpClient es el que realmente hace la llamada de red. Aquí solo se le pone un límite de espera de 5 segundos,
// para que si el servidor está apagado la app no se quede esperando mucho rato.
object RetrofitInstance {

    private val cliente: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    private fun crear(urlBase: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(urlBase)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val contenidos: ContenidosApi by lazy {
        crear(ApiConfig.CONTENIDOS).create(ContenidosApi::class.java)
    }

    val catalogo: CatalogoApi by lazy {
        crear(ApiConfig.CATALOGO).create(CatalogoApi::class.java)
    }

    val vinculacion: VinculacionApi by lazy {
        crear(ApiConfig.VINCULACION).create(VinculacionApi::class.java)
    }

    val admin: AdminApi by lazy {
        crear(ApiConfig.ADMIN).create(AdminApi::class.java)
    }
}
