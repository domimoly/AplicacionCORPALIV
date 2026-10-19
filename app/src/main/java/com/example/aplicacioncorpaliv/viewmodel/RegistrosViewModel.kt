package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.Contacto
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.data.RegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RegistrosViewModel(application: Application) :
    AndroidViewModel(application) {

    private val baseDeDatos = AppDatabase.getInstance(application)

    private val repositorio = RegistroRepository(baseDeDatos.registroDao())
    private val repositorioContactos = ContactoRepository(baseDeDatos.contactoDao())

    val registros: Flow<List<Registro>> = repositorio.registros
    val contactos: Flow<List<Contacto>> = repositorioContactos.contactos

    fun eliminar(registro: Registro) {
        viewModelScope.launch {
            repositorio.delete(registro)
        }
    }

    fun eliminarContacto(contacto: Contacto) {
        viewModelScope.launch {
            repositorioContactos.delete(contacto)
        }
    }
}