package com.example.resipesdishesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Parcelize
data class Category(
   @PrimaryKey val id: Int,
   @ColumnInfo("category_name") val title: String,
   @ColumnInfo("category_description") val description: String,
   @ColumnInfo("category_image") val imageUrl: String
): Parcelable
