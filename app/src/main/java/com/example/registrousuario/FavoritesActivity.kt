package com.example.registrousuario

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.registrousuario.data.AppDatabase
import com.example.registrousuario.data.FavoriteSubject
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var rvFavoritos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fabAgregarFavorito: FloatingActionButton

    private lateinit var favoritesAdapter: FavoritesAdapter
    private var currentUserCorreo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        rvFavoritos = findViewById(R.id.rvFavoritos)
        layoutVacio = findViewById(R.id.layoutVacio)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        fabAgregarFavorito = findViewById(R.id.fabAgregarFavorito)

        rvFavoritos.layoutManager = LinearLayoutManager(this)
        favoritesAdapter = FavoritesAdapter(
            favorites = emptyList(),
            onEditClick = { favorite -> mostrarDialogoEditar(favorite) },
            onDeleteClick = { favorite -> confirmarEliminacion(favorite) }
        )
        rvFavoritos.adapter = favoritesAdapter

        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        currentUserCorreo = prefs.getString("correo_actual", null)

        bottomNavigation.selectedItemId = R.id.nav_favorites
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
                R.id.nav_favorites -> true
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        fabAgregarFavorito.setOnClickListener { mostrarDialogoAgregar() }

        cargarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.nav_favorites
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val correo = currentUserCorreo ?: return
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val lista = db.favoriteSubjectDao().getFavoritesByUser(correo)
            favoritesAdapter.updateList(lista)

            if (lista.isEmpty()) {
                layoutVacio.visibility = View.VISIBLE
                rvFavoritos.visibility = View.GONE
            } else {
                layoutVacio.visibility = View.GONE
                rvFavoritos.visibility = View.VISIBLE
            }
        }
    }

    private fun mostrarDialogoAgregar() {
        mostrarDialogoFormulario(
            titulo = "Agregar Materia Favorita",
            nombreInicial = "",
            descripcionInicial = "",
            emojiInicial = "⭐",
            btnTexto = "Agregar"
        ) { nombre, descripcion, emoji ->
            val correo = currentUserCorreo ?: return@mostrarDialogoFormulario
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
    }

    private fun mostrarDialogoEditar(favorite: FavoriteSubject) {
        mostrarDialogoFormulario(
            titulo = "Editar Materia Favorita",
            nombreInicial = favorite.nombre,
            descripcionInicial = favorite.descripcion,
            emojiInicial = favorite.emoji,
            btnTexto = "Guardar"
        ) { nombre, descripcion, emoji ->
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val favoritoActualizado = favorite.copy(
                    nombre = nombre,
                    descripcion = descripcion,
                    emoji = emoji
                )
                db.favoriteSubjectDao().updateFavorite(favoritoActualizado)
                Toast.makeText(this@FavoritesActivity, "Materia actualizada ✏️", Toast.LENGTH_SHORT).show()
                cargarFavoritos()
            }
        }
    }

    private fun mostrarDialogoFormulario(
        titulo: String,
        nombreInicial: String,
        descripcionInicial: String,
        emojiInicial: String,
        btnTexto: String,
        onGuardar: (nombre: String, descripcion: String, emoji: String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_favorite_subject, null)
        val tvTitulo = dialogView.findViewById<TextView>(R.id.tvTituloDialog)
        val actvNombre = dialogView.findViewById<AutoCompleteTextView>(R.id.actvNombreDialog)
        val etDescripcion = dialogView.findViewById<TextInputEditText>(R.id.etDescripcionDialog)
        val layoutEmojis = dialogView.findViewById<LinearLayout>(R.id.layoutEmojis)

        tvTitulo.text = titulo
        etDescripcion.setText(descripcionInicial)

        val materiasLista = mutableListOf(
            "Matemáticas",
            "Ciencias",
            "Lectura",
            "Arte",
            "Historia",
            "Música",
            "Geografía",
            "Computación",
            "Idiomas",
            "Educación Física"
        )

        if (nombreInicial.isNotEmpty() && !materiasLista.contains(nombreInicial)) {
            materiasLista.add(0, nombreInicial)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, materiasLista)
        actvNombre.setAdapter(adapter)

        if (nombreInicial.isNotEmpty()) {
            actvNombre.setText(nombreInicial, false)
        } else {
            actvNombre.setText(materiasLista[0], false)
        }

        val emojiMateriaMap = mapOf(
            "Matemáticas" to "➗",
            "Ciencias" to "🔬",
            "Lectura" to "📚",
            "Arte" to "🎨",
            "Historia" to "🌎",
            "Música" to "🎵",
            "Geografía" to "🌎",
            "Computación" to "💻",
            "Idiomas" to "🧠",
            "Educación Física" to "🚀"
        )

        val emojisDisponibles = listOf("⭐", "➗", "🔬", "📚", "🎨", "🌎", "🎵", "💻", "🧠", "📐", "🧪", "🚀")
        var emojiSeleccionado = emojiInicial

        val cardViewsEmoji = mutableListOf<CardView>()

        fun actualizarVisualizacionEmojis() {
            cardViewsEmoji.forEachIndexed { index, c ->
                if (emojisDisponibles[index] == emojiSeleccionado) {
                    c.setCardBackgroundColor(Color.parseColor("#DDD6FE"))
                } else {
                    c.setCardBackgroundColor(Color.WHITE)
                }
            }
        }

        emojisDisponibles.forEach { emoji ->
            val card = CardView(this).apply {
                radius = 30f
                elevation = 2f
                useCompatPadding = true
            }

            val tv = TextView(this).apply {
                text = emoji
                textSize = 24f
                setPadding(20, 20, 20, 20)
            }

            if (emoji == emojiSeleccionado) {
                card.setCardBackgroundColor(Color.parseColor("#DDD6FE"))
            } else {
                card.setCardBackgroundColor(Color.WHITE)
            }

            cardViewsEmoji.add(card)

            card.setOnClickListener {
                emojiSeleccionado = emoji
                actualizarVisualizacionEmojis()
            }

            card.addView(tv)
            layoutEmojis.addView(card)
        }

        actvNombre.setOnItemClickListener { _, _, position, _ ->
            val materiaSeleccionada = adapter.getItem(position) ?: ""
            val emojiSugerido = emojiMateriaMap[materiaSeleccionada]
            if (emojiSugerido != null) {
                emojiSeleccionado = emojiSugerido
                actualizarVisualizacionEmojis()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(btnTexto) { _, _ ->
                val nombre = actvNombre.text.toString().trim()
                val descripcion = etDescripcion.text.toString().trim()

                if (nombre.isEmpty()) {
                    Toast.makeText(this, "Selecciona una materia", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                onGuardar(nombre, descripcion, emojiSeleccionado)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarEliminacion(favorite: FavoriteSubject) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar materia")
            .setMessage("¿Estás seguro de eliminar '${favorite.nombre}' de tus favoritos?")
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.favoriteSubjectDao().deleteFavorite(favorite)
                    Toast.makeText(this@FavoritesActivity, "Materia eliminada de favoritos", Toast.LENGTH_SHORT).show()
                    cargarFavoritos()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}