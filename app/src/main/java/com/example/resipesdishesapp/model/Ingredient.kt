package com.example.resipesdishesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    var quantity: String,
    val unitOfMeasure: String,
    val description: String
):Parcelable
