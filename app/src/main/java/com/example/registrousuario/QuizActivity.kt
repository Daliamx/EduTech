package com.example.registrousuario

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.registrousuario.data.Question
import com.example.registrousuario.data.QuestionBank

class QuizActivity : AppCompatActivity() {

    private lateinit var preguntas: List<Question>
    private var indiceActual = 0
    private var correctas = 0
    private var materia = ""

    private lateinit var tvMateria: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var tvPregunta: TextView
    private lateinit var cardFeedback: CardView
    private lateinit var tvFeedbackTitulo: TextView
    private lateinit var tvFeedbackTexto: TextView
    private lateinit var btnSiguiente: Button
    private lateinit var opciones: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        materia = intent.getStringExtra("materia") ?: "Matemáticas"
        val edad = intent.getIntExtra("edad", 8)
        preguntas = QuestionBank.getPreguntas(materia, edad)

        tvMateria = findViewById(R.id.tvMateria)
        tvProgreso = findViewById(R.id.tvProgreso)
        tvPregunta = findViewById(R.id.tvPregunta)
        cardFeedback = findViewById(R.id.cardFeedback)
        tvFeedbackTitulo = findViewById(R.id.tvFeedbackTitulo)
        tvFeedbackTexto = findViewById(R.id.tvFeedbackTexto)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        opciones = listOf(
            findViewById(R.id.opcionA),
            findViewById(R.id.opcionB),
            findViewById(R.id.opcionC),
            findViewById(R.id.opcionD)
        )

        tvMateria.text = materia

        opciones.forEachIndexed { index, boton ->
            boton.setOnClickListener { responder(index) }
        }

        btnSiguiente.setOnClickListener { siguientePregunta() }

        mostrarPregunta()
    }

    private fun mostrarPregunta() {
        val pregunta = preguntas[indiceActual]
        tvProgreso.text = "${indiceActual + 1}/${preguntas.size}"
        tvPregunta.text = pregunta.texto

        opciones.forEachIndexed { index, boton ->
            boton.text = pregunta.opciones[index]
            boton.isEnabled = true
            boton.alpha = 1f
        }

        cardFeedback.visibility = android.view.View.GONE
        btnSiguiente.visibility = android.view.View.GONE
    }

    private fun responder(indiceSeleccionado: Int) {
        val pregunta = preguntas[indiceActual]
        val esCorrecta = indiceSeleccionado == pregunta.correctaIndex

        opciones.forEach { it.isEnabled = false }

        if (esCorrecta) {
            correctas++
            tvFeedbackTitulo.text = "¡Excelente!"
            tvFeedbackTexto.text = pregunta.feedbackCorrecto
        } else {
            opciones[indiceSeleccionado].alpha = 0.5f
            tvFeedbackTitulo.text = " ¡Casi! Sigue intentando"
            tvFeedbackTexto.text = pregunta.feedbackIncorrecto
        }

        cardFeedback.visibility = android.view.View.VISIBLE
        btnSiguiente.visibility = android.view.View.VISIBLE
    }

    private fun siguientePregunta() {
        if (indiceActual < preguntas.size - 1) {
            indiceActual++
            mostrarPregunta()
        } else {
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("correctas", correctas)
            intent.putExtra("total", preguntas.size)
            intent.putExtra("materia", materia)
            intent.putExtra("edad", intent.getIntExtra("edad", 8))
            startActivity(intent)
            finish()
        }
    }
}