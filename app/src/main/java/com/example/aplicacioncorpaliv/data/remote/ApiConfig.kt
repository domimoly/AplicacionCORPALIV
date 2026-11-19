package com.example.aplicacioncorpaliv.data.remote

// DIRECCIONES de los microservicios. Es el ÚNICO lugar donde se escriben.
// EXPLICAR: 10.0.2.2 es la forma en que el EMULADOR de Android llama al computador donde corre el backend.
// AVANZADO: si pruebas en un celular real, cambia HOST por la IP de tu computador en la red WiFi (por ejemplo "192.168.1.15").
// Cada microservicio tiene su propio puerto. Las direcciones deben terminar en "/".
object ApiConfig {
    const val HOST = "10.0.2.2"

    const val CONTENIDOS = "http://$HOST:9091/"    // ms-contenidos: programas, historias e indicadores
    const val VINCULACION = "http://$HOST:9092/"   // ms-vinculacion: solicitudes de colaboración
    const val ADMIN = "http://$HOST:9093/"         // ms-admin: login de gestores
    const val CATALOGO = "http://$HOST:9094/"      // ms-catalogo: productos y servicios

    // API EXTERNA (no es nuestra): Nager.Date entrega los feriados de cada país. Es gratis y no pide clave
    const val NAGER = "https://date.nager.at/"
}
