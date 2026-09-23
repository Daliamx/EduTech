package com.example.registrousuario.data

data class Question(
    val texto: String,
    val opciones: List<String>,
    val correctaIndex: Int,
    val feedbackCorrecto: String,
    val feedbackIncorrecto: String
)

object QuestionBank {

    fun getPreguntas(materia: String, edad: Int): List<Question> {
        val esDificil = edad >= 12
        return when (materia) {
            "Matemáticas" -> if (esDificil) matematicasDificil else matematicasFacil
            "Ciencias" -> if (esDificil) cienciasDificil else cienciasFacil
            "Lectura" -> if (esDificil) lecturaDificil else lecturaFacil
            "Arte" -> if (esDificil) arteDificil else arteFacil
            "Historia" -> if (esDificil) historiaDificil else historiaFacil
            "Música" -> if (esDificil) musicaDificil else musicaFacil
            else -> emptyList()
        }
    }

    // ---------- MATEMÁTICAS ----------
    private val matematicasFacil = listOf(
        Question("¿Cuánto es 10 - 4?", listOf("4", "5", "6", "7"), 2,
            "¡Excelente! 10 - 4 = 6", "10 - 4 = 6. ¡Sigue intentando!"),
        Question("Si tienes 12 dulces y regalas 5, ¿cuántos te quedan?", listOf("5", "6", "7", "8"), 2,
            "¡Muy bien! 12 - 5 = 7", "12 - 5 = 7. ¡Tú puedes!"),
        Question("¿Cuánto es 3 + 4?", listOf("6", "7", "8", "9"), 1,
            "¡Genial! 3 + 4 = 7", "3 + 4 = 7. ¡Casi!")
    )
    private val matematicasDificil = listOf(
        Question("¿Cuál es el resultado de 12 × 8?", listOf("86", "96", "106", "112"), 1,
            "¡Correcto! 12 × 8 = 96", "12 × 8 = 96. Revisa tu multiplicación."),
        Question("Resuelve: 2x + 5 = 17, ¿cuánto vale x?", listOf("4", "5", "6", "7"), 2,
            "¡Exacto! x = 6", "2x + 5 = 17 → x = 6"),
        Question("¿Cuánto es 144 ÷ 12?", listOf("10", "11", "12", "13"), 2,
            "¡Bien! 144 ÷ 12 = 12", "144 ÷ 12 = 12")
    )

    // ---------- CIENCIAS ----------
    private val cienciasFacil = listOf(
        Question("¿Qué necesitan las plantas para crecer?", listOf("Solo agua", "Sol, agua y tierra", "Solo tierra", "Nada"), 1,
            "¡Correcto! Las plantas necesitan sol, agua y tierra", "Las plantas necesitan sol, agua y tierra"),
        Question("¿Cuántas patas tiene una araña?", listOf("6", "8", "10", "4"), 1,
            "¡Sí! Las arañas tienen 8 patas", "Las arañas tienen 8 patas"),
        Question("¿Qué planeta es el más cercano al Sol?", listOf("Tierra", "Venus", "Mercurio", "Marte"), 2,
            "¡Correcto! Mercurio es el más cercano", "Mercurio es el planeta más cercano al Sol")
    )
    private val cienciasDificil = listOf(
        Question("¿Cuál es la fórmula química del agua?", listOf("CO2", "H2O", "O2", "NaCl"), 1,
            "¡Correcto! El agua es H2O", "El agua es H2O"),
        Question("¿Qué proceso usan las plantas para producir energía con luz solar?", listOf("Respiración", "Fotosíntesis", "Digestión", "Evaporación"), 1,
            "¡Exacto! Es la fotosíntesis", "El proceso se llama fotosíntesis"),
        Question("¿Cuál es la unidad básica de la vida?", listOf("Átomo", "Molécula", "Célula", "Tejido"), 2,
            "¡Correcto! Es la célula", "La unidad básica de la vida es la célula")
    )

    // ---------- LECTURA ----------
    private val lecturaFacil = listOf(
        Question("¿Cuál palabra rima con 'gato'?", listOf("Perro", "Pato", "Casa", "Sol"), 1,
            "¡Sí! Pato rima con gato", "'Pato' rima con 'gato'"),
        Question("¿Cómo se llama quien escribe un libro?", listOf("Lector", "Autor", "Editor", "Actor"), 1,
            "¡Correcto! Se llama autor", "Quien escribe un libro es el autor"),
        Question("¿Cuál es el sinónimo de 'feliz'?", listOf("Triste", "Contento", "Enojado", "Cansado"), 1,
            "¡Bien! 'Contento' es sinónimo de feliz", "'Contento' es sinónimo de feliz")
    )
    private val lecturaDificil = listOf(
        Question("¿Qué figura literaria compara dos cosas usando 'como'?", listOf("Metáfora", "Símil", "Hipérbole", "Personificación"), 1,
            "¡Exacto! Es el símil", "La comparación con 'como' es un símil"),
        Question("¿Cuál es el antónimo de 'efímero'?", listOf("Pasajero", "Duradero", "Breve", "Rápido"), 1,
            "¡Correcto! 'Duradero' es el antónimo", "El antónimo de 'efímero' es 'duradero'"),
        Question("¿Qué es una hipérbole?", listOf("Una exageración", "Una rima", "Una pregunta", "Un resumen"), 0,
            "¡Sí! Es una exageración literaria", "Una hipérbole es una exageración")
    )

    // ---------- ARTE ----------
    private val arteFacil = listOf(
        Question("¿Qué colores se mezclan para hacer verde?", listOf("Rojo y azul", "Azul y amarillo", "Rojo y amarillo", "Blanco y negro"), 1,
            "¡Correcto! Azul + amarillo = verde", "Azul y amarillo forman el verde"),
        Question("¿Con qué herramienta se pinta?", listOf("Tijeras", "Pincel", "Regla", "Lápiz de grafito"), 1,
            "¡Sí! Se usa el pincel", "Se pinta con un pincel"),
        Question("¿Qué figura tiene 3 lados?", listOf("Cuadrado", "Círculo", "Triángulo", "Rectángulo"), 2,
            "¡Exacto! El triángulo tiene 3 lados", "El triángulo tiene 3 lados")
    )
    private val arteDificil = listOf(
        Question("¿Quién pintó la Mona Lisa?", listOf("Van Gogh", "Da Vinci", "Picasso", "Dalí"), 1,
            "¡Correcto! Fue Leonardo Da Vinci", "La Mona Lisa la pintó Da Vinci"),
        Question("¿Qué movimiento artístico se asocia con Picasso?", listOf("Impresionismo", "Cubismo", "Surrealismo", "Barroco"), 1,
            "¡Sí! Es el Cubismo", "Picasso se asocia con el Cubismo"),
        Question("¿Qué son los colores complementarios?", listOf("Los que están opuestos en el círculo cromático", "Los colores primarios", "Los tonos de gris", "Colores oscuros"), 0,
            "¡Exacto!", "Son los que están opuestos en el círculo cromático")
    )

    // ---------- HISTORIA ----------
    private val historiaFacil = listOf(
        Question("¿En qué continente está México?", listOf("Europa", "América", "Asia", "África"), 1,
            "¡Correcto! México está en América", "México está en el continente americano"),
        Question("¿Quién descubrió América en 1492?", listOf("Cristóbal Colón", "Hernán Cortés", "Marco Polo", "Magallanes"), 0,
            "¡Sí! Fue Cristóbal Colón", "Cristóbal Colón llegó a América en 1492"),
        Question("¿Cómo se llama la bandera de tu país?", listOf("Símbolo patrio", "Escudo", "Himno", "Emblema"), 0,
            "¡Correcto!", "La bandera es un símbolo patrio")
    )
    private val historiaDificil = listOf(
        Question("¿En qué año comenzó la Independencia de México?", listOf("1810", "1821", "1910", "1521"), 0,
            "¡Correcto! Fue en 1810", "La Independencia de México inició en 1810"),
        Question("¿Qué imperio conquistó Hernán Cortés?", listOf("Maya", "Azteca", "Inca", "Olmeca"), 1,
            "¡Exacto! El Imperio Azteca", "Hernán Cortés conquistó el Imperio Azteca"),
        Question("¿En qué año terminó la Segunda Guerra Mundial?", listOf("1939", "1942", "1945", "1950"), 2,
            "¡Correcto! En 1945", "La Segunda Guerra Mundial terminó en 1945")
    )

    // ---------- MÚSICA ----------
    private val musicaFacil = listOf(
        Question("¿Cuántas cuerdas tiene una guitarra normal?", listOf("4", "5", "6", "7"), 2,
            "¡Sí! Tiene 6 cuerdas", "La guitarra normal tiene 6 cuerdas"),
        Question("¿Qué instrumento se toca soplando?", listOf("Piano", "Flauta", "Tambor", "Guitarra"), 1,
            "¡Correcto! La flauta se sopla", "La flauta se toca soplando"),
        Question("¿Cómo se llama la nota más baja de la escala?", listOf("Do", "Re", "Mi", "Sol"), 0,
            "¡Exacto! Es el Do", "La nota más baja es el Do")
    )
    private val musicaDificil = listOf(
        Question("¿Quién compuso la 'Novena Sinfonía'?", listOf("Mozart", "Bach", "Beethoven", "Chopin"), 2,
            "¡Correcto! Fue Beethoven", "La Novena Sinfonía es de Beethoven"),
        Question("¿Qué significa 'forte' en música?", listOf("Suave", "Fuerte", "Rápido", "Lento"), 1,
            "¡Sí! 'Forte' significa fuerte", "'Forte' significa fuerte"),
        Question("¿Cuántos tiempos tiene un compás de 3/4?", listOf("2", "3", "4", "6"), 1,
            "¡Correcto! Tiene 3 tiempos", "Un compás de 3/4 tiene 3 tiempos")
    )
}