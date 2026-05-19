package com.example.sqliteconroom

import android.content.Context
import java.io.File

object FileHelper {
    /**
     * Ejemplo de almacenamiento externo (Scoped Storage).
     * Guarda un archivo en el directorio privado de la app en el almacenamiento externo.
     */
    fun guardarEnAlmacenamientoExterno(context: Context, nombreArchivo: String, contenido: String): String? {
        return try {
            // getExternalFilesDir(null) devuelve la ruta a /storage/emulated/0/Android/data/com.example.sqliteconroom/files
            // No requiere permisos de escritura en Android 10+ para esta carpeta específica.
            val carpeta = context.getExternalFilesDir(null)
            val archivo = File(carpeta, nombreArchivo)
            archivo.writeText(contenido)
            archivo.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun leerDeAlmacenamientoExterno(context: Context, nombreArchivo: String): String? {
        return try {
            val carpeta = context.getExternalFilesDir(null)
            val archivo = File(carpeta, nombreArchivo)
            if (archivo.exists()) {
                archivo.readText()
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
