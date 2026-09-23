package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val correctas = intent.getIntExtra("correctas", 0)
        val total = intent.getIntExtra("total", 1)
        val materia = intent.getStringExtra("materia") ?: "Matemáticas"
        val edad = intent.getIntExtra("edad", 8)

        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvMedalla = findViewById<TextView>(R.id.tvMedalla)
        val estrella1 = findViewById<TextView>(R.id.estrella1)
        val estrella2 = findViewById<TextView>(R.id.estrella2)
        val estrella3 = findViewById<TextView>(R.id.estrella3)

        tvResultado.text = "$correctas/$total correctas"

        val porcentaje = correctas.toFloat() / total.toFloat()

        when {
            porcentaje >= 0.8f -> {
                tvMedalla.text = "🥇"
                tvTitulo.text = "¡Increíble!"
                tvMensaje.text = " ¡Eres una estrella!"
                estrella1.text = "⭐"; estrella2.text = "⭐"; estrella3.text = "⭐"
            }
            porcentaje >= 0.5f -> {
                tvMedalla.text = "🥈"
                tvTitulo.text = "¡Muy bien!"
                tvMensaje.text = "¡Sigue así, estrella!"
                estrella1.text = "⭐"; estrella2.text = "⭐"; estrella3.text = "☆"
            }
            else -> {
                tvMedalla.text = "🥉"
                tvTitulo.text = "¡Sigue practicando!"
                tvMensaje.text = " ¡Tú puedes lograrlo!"
                estrella1.text = "⭐"; estrella2.text = "☆"; estrella3.text = "☆"
            }
        }

        findViewById<Button>(R.id.btnReintentar).setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("materia", materia)
            intent.putExtra("edad", edad)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnOtraMateria).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.tvIrInicio).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}