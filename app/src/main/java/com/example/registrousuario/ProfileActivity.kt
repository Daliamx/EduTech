package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import com.example.registrousuario.data.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var tilNombrePerfil: TextInputLayout
    private lateinit var tilEdadPerfil: TextInputLayout
    private lateinit var tilCorreoPerfil: TextInputLayout

    private lateinit var etNombrePerfil: TextInputEditText
    private lateinit var etEdadPerfil: TextInputEditText
    private lateinit var etCorreoPerfil: TextInputEditText

    private lateinit var tvNombreEncabezado: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tilNombrePerfil = findViewById(R.id.tilNombrePerfil)
        tilEdadPerfil = findViewById(R.id.tilEdadPerfil)
        tilCorreoPerfil = findViewById(R.id.tilCorreoPerfil)

        etNombrePerfil = findViewById(R.id.etNombrePerfil)
        etEdadPerfil = findViewById(R.id.etEdadPerfil)
        etCorreoPerfil = findViewById(R.id.etCorreoPerfil)

        tvNombreEncabezado = findViewById(R.id.tvNombreEncabezado)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_profile
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }

        val btnGuardarPerfil = findViewById<Button>(R.id.btnGuardarPerfil)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        btnGuardarPerfil.setOnClickListener { guardarCambios() }
        btnCerrarSesion.setOnClickListener { cerrarSesion() }

        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        val correo = prefs.getString("correo_actual", null)

        if (correo != null) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val usuario = db.userDao().getUserByCorreo(correo)
                if (usuario != null) {
                    currentUser = usuario
                    etNombrePerfil.setText(usuario.nombre)
                    etEdadPerfil.setText(usuario.edad.toString())
                    etCorreoPerfil.setText(usuario.correo)
                    tvNombreEncabezado.text = usuario.nombre
                }
            }
        }
    }

    private fun guardarCambios() {
        val user = currentUser ?: return

        val nuevoNombre = etNombrePerfil.text.toString().trim()
        val edadStr = etEdadPerfil.text.toString().trim()
        val nuevoCorreo = etCorreoPerfil.text.toString().trim()

        var esValido = true

        if (nuevoNombre.isEmpty()) {
            tilNombrePerfil.error = "Escribe tu nombre"
            esValido = false
        } else {
            tilNombrePerfil.error = null
        }

        val nuevaEdad = edadStr.toIntOrNull()
        if (nuevaEdad == null || nuevaEdad <= 0) {
            tilEdadPerfil.error = "Ingresa una edad válida"
            esValido = false
        } else {
            tilEdadPerfil.error = null
        }

        if (nuevoCorreo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches()) {
            tilCorreoPerfil.error = "Ingresa un correo electrónico válido"
            esValido = false
        } else {
            tilCorreoPerfil.error = null
        }

        if (!esValido) return

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            if (!nuevoCorreo.equals(user.correo, ignoreCase = true)) {
                val usuarioExistente = db.userDao().getUserByCorreo(nuevoCorreo)
                if (usuarioExistente != null && usuarioExistente.id != user.id) {
                    tilCorreoPerfil.error = "Este correo ya está registrado"
                    return@launch
                }
            }

            val usuarioActualizado = user.copy(
                nombre = nuevoNombre,
                edad = nuevaEdad!!,
                correo = nuevoCorreo
            )

            db.userDao().updateUser(usuarioActualizado)
            currentUser = usuarioActualizado

            val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
            prefs.edit().putString("correo_actual", nuevoCorreo).apply()

            tvNombreEncabezado.text = nuevoNombre

            Toast.makeText(this@ProfileActivity, "Perfil actualizado con éxito 💾", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        prefs.edit().remove("correo_actual").apply()

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}