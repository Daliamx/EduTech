package com.example.registrousuario.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun getUserByCorreo(correo: String): User?

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    suspend fun getAllUsers(): List<User>
}