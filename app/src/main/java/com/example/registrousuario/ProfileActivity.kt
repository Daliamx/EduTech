package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import com.example.registrousuario.data.User
import com.example.registrousuario.ui.components.NavTab
import com.example.registrousuario.ui.screens.ProfileScreen
import com.example.registrousuario.ui.theme.EduTechTheme
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {

    private var currentUser by mutableStateOf<User?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EduTechTheme {
                ProfileScreen(
                    user = currentUser,
                    onSaveProfile = { nuevoNombre, nuevaEdad, nuevoCorreo ->
                        guardarCambios(nuevoNombre, nuevaEdad, nuevoCorreo)
                    },
                    onLogout = {
                        cerrarSesion()
                    },
                    onTabSelected = { tab ->
                        when (tab) {
                            NavTab.HOME -> {
                                val intent = Intent(this, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            }
                            NavTab.FAVORITES -> {
                                val intent = Intent(this, FavoritesActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            }
                            NavTab.PROFILE -> {}
                        }
                    }
                )
            }
        }

        cargarDatosUsuario()
    }

    override fun onResume() {
        super.onResume()
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
                }
            }
        }
    }

    private fun guardarCambios(nuevoNombre: String, nuevaEdad: Int, nuevoCorreo: String) {
        val user = currentUser ?: return

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)

            if (!nuevoCorreo.equals(user.correo, ignoreCase = true)) {
                val usuarioExistente = db.userDao().getUserByCorreo(nuevoCorreo)
                if (usuarioExistente != null && usuarioExistente.id != user.id) {
                    Toast.makeText(this@ProfileActivity, "Este correo ya está registrado por otro usuario", Toast.LENGTH_LONG).show()
                    return@launch
                }
            }

            val usuarioActualizado = user.copy(
                nombre = nuevoNombre,
                edad = nuevaEdad,
                correo = nuevoCorreo
            )

            db.userDao().updateUser(usuarioActualizado)
            currentUser = usuarioActualizado

            val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
            prefs.edit().putString("correo_actual", nuevoCorreo).apply()

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