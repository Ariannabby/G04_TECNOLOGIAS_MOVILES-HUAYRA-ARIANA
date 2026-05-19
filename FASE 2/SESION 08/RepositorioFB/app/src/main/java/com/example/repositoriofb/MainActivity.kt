package com.example.repositoriofb

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var txtTema: EditText
    private lateinit var spinAreas: Spinner
    private lateinit var spinSecciones: Spinner
    private lateinit var btnRegistrar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var clasesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        clasesRef = FirebaseDatabase.getInstance().getReference("Clases")

        txtTema = findViewById(R.id.editTextText)
        spinAreas = findViewById(R.id.spinarea)
        spinSecciones = findViewById(R.id.spinseccion)
        btnRegistrar = findViewById(R.id.btnregistrar)
        progressBar = findViewById(R.id.progressBar)

        btnRegistrar.setOnClickListener {
            registrarClase()
        }
    }

    private fun registrarClase() {
        val seccion = spinSecciones.selectedItem.toString()
        val area = spinAreas.selectedItem.toString()
        val tema = txtTema.text.toString().trim()

        if (tema.isEmpty()) {
            Toast.makeText(this, "Escribe el tema antes de registrar", Toast.LENGTH_SHORT).show()
            return
        }

        // Desactivar botón y mostrar cargando (spinner) para evitar doble registro
        btnRegistrar.isEnabled = false
        btnRegistrar.text = "" // Oculta el texto para que no se superponga
        progressBar.visibility = View.VISIBLE

        val id = clasesRef.child("Lecciones").push().key ?: run {
            restaurarBoton()
            return
        }

        val leccion = Clase(
            claseid = id,
            seccion = seccion,
            area = area,
            tema = tema
        )

        clasesRef.child("Lecciones").child(id).setValue(leccion)
            .addOnSuccessListener {
                Toast.makeText(this, "Clase Registrada", Toast.LENGTH_LONG).show()
                txtTema.text.clear()
                restaurarBoton()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                restaurarBoton()
            }
    }

    private fun restaurarBoton() {
        btnRegistrar.isEnabled = true
        btnRegistrar.text = getString(R.string.app_clase)
        progressBar.visibility = View.GONE
    }
}