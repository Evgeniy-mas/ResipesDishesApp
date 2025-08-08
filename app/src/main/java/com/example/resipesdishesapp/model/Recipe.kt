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
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo ("recipe_title") val title: String,
    @ColumnInfo ("recipe_ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo ("recipe_method") val method:List<String> = listOf("""
            "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
            "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
            "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
            "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
            "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."          
            
            """.trimIndent()),
    @ColumnInfo ("recipe_imageUrl") val imageUrl: String,
    @ColumnInfo ("recipe_imageUrlHeader") val imageUrlHeader: String? = null,
    @ColumnInfo val categoryId: Int? = null
):Parcelable









