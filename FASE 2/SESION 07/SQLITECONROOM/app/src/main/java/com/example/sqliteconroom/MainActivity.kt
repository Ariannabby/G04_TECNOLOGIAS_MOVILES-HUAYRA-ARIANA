package com.example.sqliteconroom

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sqliteconroom.data.AppDatabase
import com.example.sqliteconroom.data.Articulo
import com.example.sqliteconroom.data.ArticuloRepository
import com.example.sqliteconroom.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ArticuloRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Repositorio
        val dao = AppDatabase.getInstance(this).articuloDao()
        repository = ArticuloRepository(dao)

        // Listeners
        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener { eliminar() }

        // Ejercicio: Observar cambios en tiempo real usando Flow y repeatOnLifecycle
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.allArticulos.collect { lista ->
                    // Aquí podrías actualizar un RecyclerView. 
                    // Por ahora, imprimiremos el tamaño en el log para demostrar que funciona.
                    Log.d("MainActivity", "Artículos actualizados: ${lista.size}")
                }
            }
        }

        // Ejercicio: Ejemplo de uso de Almacenamiento Externo (Scoped Storage)
        ejemploAlmacenamientoExterno()
    }

    private fun ejemploAlmacenamientoExterno() {
        val nombre = "ejemplo_scoped.txt"
        val path = FileHelper.guardarEnAlmacenamientoExterno(this, nombre, "Hola desde Room Project")
        if (path != null) {
            Log.d("MainActivity", "Archivo guardado en: $path")
            val contenido = FileHelper.leerDeAlmacenamientoExterno(this, nombre)
            Log.d("MainActivity", "Contenido leído: $contenido")
        }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        binding.txtCodigo.setText("")
        binding.txtDescripcion.setText("")
        binding.txtPrecio.setText("")
    }

    private fun registrar() {
        val codigo = binding.txtCodigo.text.toString()
        val descripcion = binding.txtDescripcion.text.toString()
        val precio = binding.txtPrecio.text.toString()

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            toast("Debe llenar todos los campos")
            return
        }

        val articulo = Articulo(
            codigo = codigo.toInt(),
            descripcion = descripcion,
            precio = precio.toDouble()
        )

        lifecycleScope.launch {
            try {
                repository.insertar(articulo)
                limpiarCampos()
                toast("Registro exitoso")
            } catch (e: Exception) {
                toast("Error al registrar: ${e.message}")
            }
        }
    }

    private fun buscar() {
        val codigo = binding.txtCodigo.text.toString()
        if (codigo.isEmpty()) {
            toast("Debe introducir el código del artículo")
            return
        }

        lifecycleScope.launch {
            val articulo = repository.buscarPorCodigo(codigo.toInt())
            if (articulo != null) {
                binding.txtDescripcion.setText(articulo.descripcion)
                binding.txtPrecio.setText(articulo.precio.toString())
            } else {
                toast("No existe el artículo")
            }
        }
    }

    private fun eliminar() {
        val codigo = binding.txtCodigo.text.toString()
        if (codigo.isEmpty()) {
            toast("Debe introducir el código del artículo")
            return
        }

        lifecycleScope.launch {
            val filasEliminadas = repository.eliminarPorCodigo(codigo.toInt())
            limpiarCampos()
            if (filasEliminadas == 1) {
                toast("Artículo eliminado exitosamente")
            } else {
                toast("El artículo no existe")
            }
        }
    }

    private fun modificar() {
        val codigo = binding.txtCodigo.text.toString()
        val descripcion = binding.txtDescripcion.text.toString()
        val precio = binding.txtPrecio.text.toString()

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            toast("Debe llenar todos los campos")
            return
        }

        val articulo = Articulo(
            codigo = codigo.toInt(),
            descripcion = descripcion,
            precio = precio.toDouble()
        )

        lifecycleScope.launch {
            val filasActualizadas = repository.actualizar(articulo)
            if (filasActualizadas == 1) {
                toast("Artículo modificado correctamente")
            } else {
                toast("El artículo no existe")
            }
        }
    }
}
