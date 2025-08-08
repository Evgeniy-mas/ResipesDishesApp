package com.example.resipesdishesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Ingredient
import com.example.resipesdishesapp.model.Recipe
import com.example.resipesdishesapp.ui.category.CategoriesDao
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesDao
import kotlinx.serialization.json.Json

@Database(entities = [Category::class, Recipe::class], version = 2)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun RecipesDao(): RecipesDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE recipe ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 1")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipes-database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    class Converters {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        @TypeConverter
        fun fromStringList(list: List<String>): String {
            return json.encodeToString(list)
        }

        @TypeConverter
        fun toStringList(data: String): List<String> {
            return try {
                json.decodeFromString(data)
            } catch (e: Exception) {
                emptyList()
            }
        }

        @TypeConverter
        fun fromIngredientsList(ingredients: List<Ingredient>): String {
            return json.encodeToString(ingredients)
        }

        @TypeConverter
        fun toIngredientsList(data: String): List<Ingredient> {
            return try {
                json.decodeFromString(data)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}