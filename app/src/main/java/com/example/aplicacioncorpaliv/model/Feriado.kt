package com.example.aplicacioncorpaliv.model

// MODELO de la API EXTERNA Nager.Date (feriados públicos).
// EXPLICAR: el servidor responde una lista de feriados en JSON; aquí solo se declaran los campos que se usan.
// Los demás campos del JSON (countryCode, fixed, global...) se ignoran solos.
// Los nombres coinciden con los del JSON para que Gson los una sin configuración.
data class Feriado(
    val date: String,       // fecha con formato 'aaaa-mm-dd', por ejemplo "2026-12-08"
    val localName: String,  // nombre en español, por ejemplo "Inmaculada Concepción"
    val name: String        // nombre en inglés
)
