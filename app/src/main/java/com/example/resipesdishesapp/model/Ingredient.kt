package com.example.resipesdishesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Ingredient(
    var quantity: String,
    val unitOfMeasure: String,
    val description: String
):Parcelable
