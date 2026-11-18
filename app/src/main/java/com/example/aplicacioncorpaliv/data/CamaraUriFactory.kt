package com.example.aplicacioncorpaliv.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
// Crea el archivo temporal donde la cámara guardará la foto y entrega su dirección (URI) mediante FileProvider, que la comparte de forma segura
object CamaraUriFactory {
    fun crearUri(context: Context): Uri {
        val cameraDir = File(context.cacheDir, "camera")
        if (!cameraDir.exists() && !cameraDir.mkdirs()) {
            throw IllegalStateException(
                "No se pudo crear el directorio de la cámara"
            )
        }
        val photoFile = File.createTempFile(
            "perfil_",
            ".jpg",
            cameraDir
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }
}