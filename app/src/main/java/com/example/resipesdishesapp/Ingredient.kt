package com.example.resipesdishesapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    var quantity: String,
    val unitOfMeasure: String,
    val description: String
):Parcelable
