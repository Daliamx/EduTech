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
import com.example.registrousuario.data.FavoriteSubject
import com.example.registrousuario.ui.components.NavTab
import com.example.registrousuario.ui.screens.HomeScreen
import com.example.registrousuario.ui.theme.EduTechTheme
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private var userName by mutableStateOf("")
    private var edadUsuario: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EduTechTheme {
                HomeScreen(
                    userName = userName,
                    onSubjectClick = { nombreMateria ->
                        val intent = Intent(this, QuizActivity::class.java)
                        intent.putExtra("materia", nombreMateria)
                        intent.putExtra("edad", edadUsuario)
                        startActivity(intent)
                    },
                    onAddFavorite = { nombreMateria ->
                        agregarAFavoritos(nombreMateria)
                    },
                    onTabSelected = { tab ->
                        when (tab) {
                            NavTab.HOME -> {}
                            NavTab.FAVORITES -> {
                                val intent = Intent(this, FavoritesActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                            }
                            NavTab.PROFILE -> {
                                val intent = Intent(this, ProfileActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                            }
                        }
                    }
                )
            }
        }
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
                    edadUsuario = usuario.edad
                    userName = usuario.nombre
                }
            }
        }
    }

    private fun agregarAFavoritos(nombreMateria: String) {
        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        val correo = prefs.getString("correo_actual", null) ?: return

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val exist = db.favoriteSubjectDao().getFavoriteByName(correo, nombreMateria)
            if (exist != null) {
                Toast.makeText(this@HomeActivity, "'$nombreMateria' ya está en tus favoritos ⭐", Toast.LENGTH_SHORT).show()
            } else {
                val emojiMap = mapOf(
                    "Matemáticas" to "➗",
                    "Ciencias" to "🔬",
                    "Lectura" to "📚",
                    "Arte" to "🎨",
                    "Historia" to "🌎",
                    "Música" to "🎵"
                )
                val nuevoFav = FavoriteSubject(
                    userCorreo = correo,
                    nombre = nombreMateria,
                    descripcion = "Materia principal de EduTech",
                    emoji = emojiMap[nombreMateria] ?: "⭐"
                )
                db.favoriteSubjectDao().insertFavorite(nuevoFav)
                Toast.makeText(this@HomeActivity, "'$nombreMateria' añadida a favoritos ⭐", Toast.LENGTH_SHORT).show()
            }
        }
    }
}