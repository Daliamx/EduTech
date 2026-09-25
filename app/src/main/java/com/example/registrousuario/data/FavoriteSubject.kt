package com.example.registrousuario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materias_favoritas")
data class FavoriteSubject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userCorreo: String,
    val nombre: String,
    val descripcion: String = "",
    val emoji: String = "⭐"
)