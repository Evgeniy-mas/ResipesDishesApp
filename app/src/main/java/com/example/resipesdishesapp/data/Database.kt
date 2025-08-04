package com.example.resipesdishesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.ui.category.CategoriesDao

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}