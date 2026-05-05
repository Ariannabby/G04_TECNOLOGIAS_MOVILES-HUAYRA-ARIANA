package com.example.parte_a

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parte_a.ui.theme.Laboratorio06Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio06Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormularioUsuario(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FormularioUsuario(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 200.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nuevo -> if (nuevo.length <= 8) nombre = nuevo },
            label = { Text("Ingresa tu nombre") },
            supportingText = { Text("Aquí ingresa tu nombre · ${nombre.length}/8") },
            trailingIcon = {
                if (nombre.isNotEmpty()) {
                    IconButton(onClick = { nombre = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormularioUsuarioPreview() {
    Laboratorio06Theme { FormularioUsuario() }
}
