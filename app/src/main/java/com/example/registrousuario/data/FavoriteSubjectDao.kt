package com.example.registrousuario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteSubjectDao {

    @Insert
    suspend fun insertFavorite(favorite: FavoriteSubject): Long

    @Update
    suspend fun updateFavorite(favorite: FavoriteSubject)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteSubject)

    @Query("SELECT * FROM materias_favoritas WHERE userCorreo = :userCorreo ORDER BY id DESC")
    suspend fun getFavoritesByUser(userCorreo: String): List<FavoriteSubject>

    @Query("SELECT * FROM materias_favoritas WHERE userCorreo = :userCorreo AND nombre = :nombre LIMIT 1")
    suspend fun getFavoriteByName(userCorreo: String, nombre: String): FavoriteSubject?
}