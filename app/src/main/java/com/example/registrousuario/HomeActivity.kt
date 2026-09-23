package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.registrousuario.data.AppDatabase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private var edadUsuario: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvSaludo = findViewById<android.widget.TextView>(R.id.tvSaludo)

        val prefs = getSharedPreferences("eduplay_prefs", MODE_PRIVATE)
        val correo = prefs.getString("correo_actual", null)

        if (correo != null) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val usuario = db.userDao().getUserByCorreo(correo)
                if (usuario != null) {
                    edadUsuario = usuario.edad
                    tvSaludo.text = "¡Hola, ${usuario.nombre}! "
                }
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
    }
}