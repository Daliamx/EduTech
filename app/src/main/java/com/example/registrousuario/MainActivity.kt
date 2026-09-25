package com.example.registrousuario

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import com.example.registrousuario.data.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tilNombre: TextInputLayout
    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tilContrasena: TextInputLayout
    private lateinit var tilEdad: TextInputLayout

    private lateinit var etNombre: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText
    private lateinit var etEdad: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tilNombre = findViewById(R.id.tilNombre)
        tilCorreo = findViewById(R.id.tilCorreo)
        tilContrasena = findViewById(R.id.tilContrasena)
        tilEdad = findViewById(R.id.tilEdad)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        etEdad = findViewById(R.id.etEdad)

        val btnGuardar = findViewById<android.widget.Button>(R.id.btnGuardar)

        val btnTabEntrar = findViewById<android.widget.Button>(R.id.btnTabEntrar)
        btnTabEntrar.setOnClickListener {
            startActivity(android.content.Intent(this, LoginActivity::class.java))
        }
        btnGuardar.setOnClickListener {
            guardarUsuario()
        }
    }

    private fun guardarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()
        val edadTexto = etEdad.text.toString().trim()

        var esValido = true

        if (nombre.isEmpty()) {
            tilNombre.error = "Escribe tu nombre"
            esValido = false
        } else {
            tilNombre.error = null
        }

        if (correo.isEmpty()) {
            tilCorreo.error = "Escribe tu correo"
            esValido = false
        } else {
            tilCorreo.error = null
        }

        if (contrasena.isEmpty()) {
            tilContrasena.error = "Escribe una contraseña"
            esValido = false
        } else {
            tilContrasena.error = null
        }

        if (edadTexto.isEmpty()) {
            tilEdad.error = "Escribe tu edad"
            esValido = false
        } else {
            tilEdad.error = null
        }

        if (!esValido) return

        val edad = edadTexto.toIntOrNull()
        if (edad == null) {
            tilEdad.error = "Escribe solo números"
            return
        }

        val nuevoUsuario = User(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            edad = edad
        )

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            val usuarioExistente = db.userDao().getUserByCorreo(correo)
            if (usuarioExistente != null) {
                tilCorreo.error = "Ya existe una cuenta con este correo"
                return@launch
            }

            db.userDao().insertUser(nuevoUsuario)

            Toast.makeText(
                this@MainActivity,
                "¡Cuenta creada con éxito, $nombre!",
                Toast.LENGTH_LONG
            ).show()
            startActivity(android.content.Intent(this@MainActivity, LoginActivity::class.java))
            finish()
            etNombre.text?.clear()
            etCorreo.text?.clear()
            etContrasena.text?.clear()
            etEdad.text?.clear()
        }
    }
}