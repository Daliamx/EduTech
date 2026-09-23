package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.Normalizer

class HomeActivity : AppCompatActivity() {

    private var edadUsuario: Int = 8
    private lateinit var tvSaludo: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvSaludo = findViewById(R.id.tvSaludo)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.selectedItemId = R.id.nav_home
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        val materias = mapOf(
            R.id.cardMatematicas to "Matemáticas",
            R.id.cardCiencias to "Ciencias",
            R.id.cardLectura to "Lectura",
            R.id.cardArte to "Arte",
            R.id.cardHistoria to "Historia",
            R.id.cardMusica to "Música"
        )

        materias.forEach { (viewId, nombreMateria) ->
            findViewById<CardView>(viewId).setOnClickListener {
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("materia", nombreMateria)
                intent.putExtra("edad", edadUsuario)
                startActivity(intent)
            }
        }

        setupSearchView(materias)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.selectedItemId = R.id.nav_home
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
                    tvSaludo.text = "¡Hola, ${usuario.nombre}! 👋"
                }
            }
        }
    }

    private fun setupSearchView(materias: Map<Int, String>) {
        val svBuscarMateria = findViewById<SearchView>(R.id.svBuscarMateria)
        val tvSinResultados = findViewById<TextView>(R.id.tvSinResultados)

        val row1 = findViewById<View>(R.id.rowMaterias1)
        val row2 = findViewById<View>(R.id.rowMaterias2)
        val row3 = findViewById<View>(R.id.rowMaterias3)

        val rowMap = mapOf(
            row1 to listOf(R.id.cardMatematicas, R.id.cardCiencias),
            row2 to listOf(R.id.cardLectura, R.id.cardArte),
            row3 to listOf(R.id.cardHistoria, R.id.cardMusica)
        )

        svBuscarMateria?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val queryNormalizada = normalizeString(newText.orEmpty())
                var algunaMateriaVisible = false

                materias.forEach { (cardId, nombreMateria) ->
                    val card = findViewById<CardView>(cardId)
                    val coincide = normalizeString(nombreMateria).contains(queryNormalizada)
                    if (coincide) {
                        card.visibility = View.VISIBLE
                        algunaMateriaVisible = true
                    } else {
                        card.visibility = View.GONE
                    }
                }

                rowMap.forEach { (rowView, cardIds) ->
                    val algunHijoVisible = cardIds.any { id ->
                        findViewById<CardView>(id).visibility == View.VISIBLE
                    }
                    rowView?.visibility = if (algunHijoVisible) View.VISIBLE else View.GONE
                }

                tvSinResultados?.visibility = if (algunaMateriaVisible) View.GONE else View.VISIBLE
                return true
            }
        })
    }

    private fun normalizeString(text: String): String {
        val unaccented = Normalizer.normalize(text, Normalizer.Form.NFD)
        return unaccented.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "").lowercase()
    }
}