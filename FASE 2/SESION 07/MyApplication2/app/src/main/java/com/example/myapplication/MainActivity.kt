package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    // Punto 8: ViewBinding genera referencias tipadas
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val FILE_NAME = "textfile.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Punto 8: Uso de referencias de ViewBinding
        binding.btnSave.setOnClickListener { onClickSave() }
        binding.btnLoad.setOnClickListener { onClickLoad() }
    }

    private fun onClickSave() {
        // Punto 8: binding.editText evita errores en tiempo de compilación
        val texto = binding.editText.text.toString()
        try {
            // Punto 9: use { } cierra automáticamente el stream
            openFileOutput(FILE_NAME, MODE_PRIVATE).use { fos ->
                fos.write(texto.toByteArray())
            }
            Toast.makeText(this, "Archivo grabado satisfactoriamente!", Toast.LENGTH_SHORT).show()
            binding.editText.setText("")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickLoad() {
        try {
            // Punto 9: use { } cierra automáticamente el reader
            val contenido = openFileInput(FILE_NAME).bufferedReader().use { reader ->
                reader.readText()
            }
            
            // Punto 8: Referencia tipada
            binding.editText.setText(contenido)
            Toast.makeText(this, "Archivo cargado satisfactoriamente!", Toast.LENGTH_SHORT).show()
        } catch (_: FileNotFoundException) {
            Toast.makeText(this, "El archivo no existe aún", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al cargar: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
