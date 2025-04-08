package com.example.resipesdishesapp

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>
) {

    fun cookingMethod() {
        val method = """
            "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
            "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
            "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
            "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
            "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."          
            
            """.trimIndent()

        println(method)
    }

    val imageUrl = "Https://images.google.com"
}