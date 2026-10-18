package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.data.RegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RegistrosViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repositorio = RegistroRepository(
        AppDatabase.getInstance(application).registroDao()
    )

    val registros: Flow<List<Registro>> = repositorio.registros

    fun eliminar(registro: Registro) {
        viewModelScope.launch {
            repositorio.delete(registro)
        }
    }
}