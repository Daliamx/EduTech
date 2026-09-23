package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tilContrasena: TextInputLayout
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tilCorreo = findViewById(R.id.tilCorreo)
        tilContrasena = findViewById(R.id.tilContrasena)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)

        val btnEntrar = findViewById<android.widget.Button>(R.id.btnEntrar)
        val btnTabRegistro = findViewById<android.widget.Button>(R.id.btnTabRegistro)

        btnEntrar.setOnClickListener { iniciarSesion() }

        val irARegistro = {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnTabRegistro.setOnClickListener { irARegistro() }
    }

    private fun iniciarSesion() {
        val correo = etCorreo.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        var esValido = true

        if (correo.isEmpty()) {
            tilCorreo.error = "Escribe tu correo"
            esValido = false
        } else {
            tilCorreo.error = null
        }

        if (contrasena.isEmpty()) {
            tilContrasena.error = "Escribe tu contraseña"
            esValido = false
        } else {
            tilContrasena.error = null
        }

        if (!esValido) return

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val usuario = db.userDao().getUserByCorreo(correo)

            when {
                usuario == null -> {
                    tilCorreo.error = "No existe una cuenta con este correo"
                }
                usuario.contrasena != contrasena -> {
                    tilContrasena.error = "Contraseña incorrecta"
                }
                else -> {
                    val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
                    prefs.edit().putString("correo_actual", usuario.correo).apply()

                    Toast.makeText(
                        this@LoginActivity,
                        "¡Bienvenido de nuevo, ${usuario.nombre}! 👋",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }
}