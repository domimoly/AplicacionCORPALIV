package com.example.aplicacioncorpaliv.ui.componentes

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

// =====================================================================================================
// LEER EN VOZ ALTA (Text To Speech = "texto a voz")
//
// CÓMO FUNCIONA, PASO A PASO (para la defensa):
// 1) Android ya trae un motor de voz instalado en el teléfono (el de Google). Esta app NO tiene una voz
//    propia: le entrega un texto a ese motor y el motor lo dice en voz alta. Por eso no pide ningún permiso.
// 2) La clase de Android que se usa se llama TextToSpeech. Al crearla, el motor tarda un instante en
//    prepararse y avisa cuando está listo (en el bloque { estado -> ... } de más abajo).
// 3) Cuando está listo, se le elige el idioma español. Se prueban varias variantes (Chile, Estados Unidos,
//    España, español genérico) porque cada teléfono tiene instaladas voces distintas.
// 4) Para hablar se llama a speak(texto, ...). Con QUEUE_FLUSH, si ya estaba hablando, corta lo anterior y
//    dice lo nuevo.
// 5) Un "listener" (UtteranceProgressListener) avisa cuando la voz termina; así el botón vuelve de
//    "Detener" a "Escuchar".
// 6) Al cerrar la app hay que llamar a shutdown() para liberar el motor (eso hace apagar()).
//
// Hay UN SOLO LectorVoz para toda la app. Se crea en MainActivity y se comparte con un CompositionLocal
// (LocalLectorVoz), igual que LocalAltoContraste. Los botones 'Escuchar' lo reciben desde ahí.
// =====================================================================================================
class LectorVoz(context: Context) {

    private var tts: TextToSpeech? = null
    private var listo = false

    // EXPLICAR: guarda QUÉ texto se está leyendo ahora (o null si no se lee nada).
    // Es un estado de Compose: cuando cambia, los botones se redibujan y cambian entre 'Escuchar' y 'Detener'.
    var textoActual by mutableStateOf<String?>(null)
        private set

    init {
        // Se crea el motor. El bloque { estado -> ... } se ejecuta cuando el motor terminó de prepararse
        tts = TextToSpeech(context) { estado ->
            if (estado == TextToSpeech.SUCCESS) {
                listo = elegirIdiomaEspanol()

                // AVANZADO: listener que avisa cuando la lectura termina o falla.
                // (No se usa onStop: cuando una lectura nueva corta a la anterior, no queremos borrar el estado)
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        textoActual = null
                    }

                    @Deprecated("Requerido por la clase base")
                    override fun onError(utteranceId: String?) {
                        textoActual = null
                    }
                })
            }
        }
    }

    // Prueba español de Chile, luego otras variantes. Devuelve true si alguna está disponible en el teléfono
    private fun elegirIdiomaEspanol(): Boolean {
        val opciones = listOf(
            Locale("es", "CL"),
            Locale("es", "US"),
            Locale("es", "ES"),
            Locale("es")
        )
        for (idioma in opciones) {
            val resultado = tts?.setLanguage(idioma)
            // Los valores negativos significan 'falta la voz' o 'idioma no soportado'
            if (resultado != null && resultado >= TextToSpeech.LANG_AVAILABLE) {
                return true
            }
        }
        return false
    }

    // Lee el texto en voz alta. Devuelve false si la voz no está lista o no hay voz en español
    fun leer(texto: String): Boolean {
        if (!listo) return false
        textoActual = texto
        tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "corpaliv_lectura")
        return true
    }

    // Corta la lectura que esté en curso
    fun detener() {
        tts?.stop()
        textoActual = null
    }

    // Libera el motor de voz (se llama al cerrar la app)
    fun apagar() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        listo = false
        textoActual = null
    }
}

// AVANZADO: CompositionLocal = dato que cualquier pantalla puede leer sin recibirlo por parámetro.
// Aquí viaja el lector de voz. Si es null, no hay lector (los botones simplemente no se muestran).
val LocalLectorVoz = staticCompositionLocalOf<LectorVoz?> { null }

// Crea el LectorVoz una sola vez (remember) y lo apaga cuando la app se cierra (DisposableEffect + onDispose).
// Se llama una vez desde MainActivity.
@Composable
fun rememberLectorVoz(): LectorVoz {
    val context = LocalContext.current
    val lector = remember { LectorVoz(context.applicationContext) }
    DisposableEffect(Unit) {
        onDispose { lector.apagar() }
    }
    return lector
}
