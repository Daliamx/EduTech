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
import com.example.registrousuario.ui.screens.FavoritesScreen
import com.example.registrousuario.ui.theme.EduTechTheme
import kotlinx.coroutines.launch

class FavoritesActivity : ComponentActivity() {

    private var favoritesList by mutableStateOf<List<FavoriteSubject>>(emptyList())
    private var currentUserCorreo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        currentUserCorreo = prefs.getString("correo_actual", null)

        setContent {
            EduTechTheme {
                FavoritesScreen(
                    favoritesList = favoritesList,
                    onAddFavorite = { nombre, descripcion, emoji ->
                        agregarFavorito(nombre, descripcion, emoji)
                    },
                    onUpdateFavorite = { favorite ->
                        actualizarFavorito(favorite)
                    },
                    onDeleteFavorite = { favorite ->
                        eliminarFavorito(favorite)
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
                            NavTab.FAVORITES -> {}
                            NavTab.PROFILE -> {
                                val intent = Intent(this, ProfileActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            }
                        }
                    }
                )
            }
        }

        cargarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val correo = currentUserCorreo ?: return
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            favoritesList = db.favoriteSubjectDao().getFavoritesByUser(correo)
        }
    }

    private fun agregarFavorito(nombre: String, descripcion: String, emoji: String) {
        val correo = currentUserCorreo ?: return
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val nuevoFavorito = FavoriteSubject(
                userCorreo = correo,
                nombre = nombre,
                descripcion = descripcion,
                emoji = emoji
            )
            db.favoriteSubjectDao().insertFavorite(nuevoFavorito)
            Toast.makeText(this@FavoritesActivity, "Materia agregada a favoritos ⭐", Toast.LENGTH_SHORT).show()
            cargarFavoritos()
        }
    }

    private fun actualizarFavorito(favorite: FavoriteSubject) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.favoriteSubjectDao().updateFavorite(favorite)
            Toast.makeText(this@FavoritesActivity, "Materia actualizada ✏️", Toast.LENGTH_SHORT).show()
            cargarFavoritos()
        }
    }

    private fun eliminarFavorito(favorite: FavoriteSubject) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.favoriteSubjectDao().deleteFavorite(favorite)
            Toast.makeText(this@FavoritesActivity, "Materia eliminada de favoritos", Toast.LENGTH_SHORT).show()
            cargarFavoritos()
        }
    }
}