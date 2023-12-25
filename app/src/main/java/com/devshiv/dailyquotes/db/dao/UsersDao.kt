package com.devshiv.dailyquotes.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devshiv.dailyquotes.db.entity.UsersEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(data: UsersEntity)

    @Query("SELECT * FROM users_db WHERE id = :id")
    suspend fun findDataById(id: String): UsersEntity

    @Query("SELECT * FROM users_db")
    suspend fun getAllData(): List<UsersEntity>

    @Query("DELETE FROM users_db")
    suspend fun deleteAllData()

    @Update
    suspend fun updateData(data: UsersEntity)

    @Delete
    suspend fun deleteData(data: UsersEntity)
}