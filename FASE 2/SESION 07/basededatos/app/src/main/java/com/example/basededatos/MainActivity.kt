package com.example.basededatos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.basededatos.data.AppDatabase
import com.example.basededatos.data.Articulo
import com.example.basededatos.data.ArticuloDao
import com.example.basededatos.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: ArticuloDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener instancia del DAO (Room)
        dao = AppDatabase.getInstance(this).articuloDao()

        // Listeners - Los IDs en XML son btnRegistrar, btnBuscar, etc.
        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener { eliminar() }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        // En ViewBinding: txt_codigo -> txtCodigo
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

        try {
            val articulo = Articulo(
                codigo = codigo.toInt(),
                descripcion = descripcion,
                precio = precio.toDouble()
            )

            lifecycleScope.launch {
                try {
                    dao.insertar(articulo)
                    limpiarCampos()
                    toast("Registro exitoso")
                } catch (e: Exception) {
                    toast("Error al registrar: ${e.message}")
                }
            }
        } catch (e: NumberFormatException) {
            toast("Formato de número inválido")
        }
    }

    private fun buscar() {
        val codigo = binding.txtCodigo.text.toString()
        if (codigo.isEmpty()) {
            toast("Debe introducir el código del artículo")
            return
        }

        lifecycleScope.launch {
            try {
                val articulo = dao.buscarPorCodigo(codigo.toInt())
                if (articulo != null) {
                    binding.txtDescripcion.setText(articulo.descripcion)
                    binding.txtPrecio.setText(articulo.precio.toString())
                } else {
                    toast("No existe el artículo")
                }
            } catch (e: Exception) {
                toast("Error al buscar")
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
            try {
                val filasEliminadas = dao.eliminarPorCodigo(codigo.toInt())
                limpiarCampos()
                if (filasEliminadas == 1) {
                    toast("Artículo eliminado exitosamente")
                } else {
                    toast("El artículo no existe")
                }
            } catch (e: Exception) {
                toast("Error al eliminar")
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

        try {
            val articulo = Articulo(
                codigo = codigo.toInt(),
                descripcion = descripcion,
                precio = precio.toDouble()
            )

            lifecycleScope.launch {
                try {
                    val filasActualizadas = dao.actualizar(articulo)
                    if (filasActualizadas == 1) {
                        toast("Artículo modificado correctamente")
                    } else {
                        toast("El artículo no existe")
                    }
                } catch (e: Exception) {
                    toast("Error al modificar")
                }
            }
        } catch (e: NumberFormatException) {
            toast("Formato de número inválido")
        }
    }
}
