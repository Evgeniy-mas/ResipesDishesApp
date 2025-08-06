package com.example.resipesdishesapp.ui.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.resipesdishesapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    suspend fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)
}